package hackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Main {

	
	
	public static void main(String[] args) {
		// process inputs
		final int portNumber = Integer.parseInt(args[0]);
		final String url = args[1];
		
		//read configuration
		Configuration conf = null; 
		try {
			File fXmlFile = new File("rules.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			String latencyThreshold = doc.getElementsByTagName("LatencyThreshold").item(0).getTextContent();
			String rejectionsPerSecondThreshold = doc.getElementsByTagName("RejectionsPerSecondThreshold").item(0).getTextContent();
			conf = new Configuration(Integer.parseInt(latencyThreshold),
					Integer.parseInt(rejectionsPerSecondThreshold));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// AI module
		AI ai = new AI(conf);

		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		ProcessJsonRunnable task = new ProcessJsonRunnable(ai, portNumber, url);
		singleThreadExecutor.submit(task);

		System.out.println("Reading xml configuration....");
		
		

		
		
		System.out.println("AI module is up!");
		System.out.println(">");

		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				while(!scanner.hasNextLine()) {
					Thread.sleep(100);
				}
				String command = scanner.nextLine();
				if (command.equals("quit")) {
					task.stop();
					break;
				}
			} catch (InterruptedException e) {
				shutdownAndAwaitTermination(singleThreadExecutor);
			}
		}
		scanner.close();
		shutdownAndAwaitTermination(singleThreadExecutor);
	}

	private static class ProcessJsonRunnable implements Runnable {
		private AI ai;
		private int portNumber;
		private String url;
		private volatile boolean stop;

		public ProcessJsonRunnable(AI ai, int portNumber, String url) {
			this.ai = ai;
			this.portNumber = portNumber;
			this.url = url;
			this.stop = false;
		}

		@Override
		public void run() {

			try (ServerSocket listeningSocket = new ServerSocket(portNumber);) {
				listeningSocket.setSoTimeout(100);
				while (!stop) {
					Socket socket = null;
					while (!stop) {
						try {
							socket = listeningSocket.accept();
							if(socket != null && socket.isConnected())
							{
								break;
							}
						} catch (SocketTimeoutException ex) {
							// do nothing
						}
					}
					if (socket != null && socket.isConnected()) {
						socket.setSoTimeout(100);
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						try {
							while (!stop) {
								try {
									String recvData = in.readLine();
									if(recvData == null) {
										break;
									}
									JSONObject recv = null;
									try {
										recv = new JSONObject(recvData);
									}
									catch(Exception ex)	{
										continue;
									}
									JSONObject answer = ai.processInputEvent(recv);
									try {
										if(answer != null) {
											HttpPost request = new HttpPost(url);
											StringEntity params = new StringEntity(answer.toString());
											request.addHeader("content-type", "application/json");
											request.setEntity(params);
											System.out.println();
											System.out.println("Sending Message[" + request.toString() + "]");
											System.out.println(">");
											CloseableHttpResponse response = httpClient.execute(request);
											EntityUtils.consume(response.getEntity());
										}
									}
									catch(ClientProtocolException e) {
										System.out.println("ClientProtocolException[" + e.getMessage() + "]");
										continue;
									}
									catch(IOException e) {
										System.out.println("IO Exception while reading from Socket. Is socket to Symphony closed?");
										continue;
									}
								}
								catch (SocketTimeoutException ex)
								{
									//do nothing
								}
							}
						}
						catch(IOException e) {
							System.out.println("IO Exception while reading from Socket. Is socket closed?");
							continue;
						}
						in.close();
					}
				}
			} 
			catch (IOException e) {
				System.out.println("IOException: " + e.getMessage() + ". Terminating AI Module");
				stop();
			} 
			catch (Exception e) {
				System.out.println("Exception: " + e.getMessage() + "");
			}
			finally {
//				System.out.println("wth");
			}
		}

		public void stop() {
			stop = true;
		}
	}
	private static void shutdownAndAwaitTermination(ExecutorService executorService) {
		executorService.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
				executorService.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!executorService.awaitTermination(1, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executorService.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}
