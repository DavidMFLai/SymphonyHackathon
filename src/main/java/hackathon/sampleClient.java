package hackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class sampleClient {

	public static void main(String[] args) {
		int portNumber = Integer.parseInt(args[0]);
		try (
			    Socket kkSocket = new Socket("localhost", portNumber);
			    PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
			    BufferedReader in = new BufferedReader(
			        new InputStreamReader(kkSocket.getInputStream()));
			)
		{
			String str = "{\"Type\":\"RhinosSelfTest\"}";
			out.write(str);
			out.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
