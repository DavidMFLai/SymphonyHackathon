package hackathon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class AI {
	public JSONObject processInputEvent(JSONObject jsonObject) {
		
		JSONObject reply = null;
		
		
		
		
		String issue_type = (String)jsonObject.getString("issue type");
		if(issue_type.equals("ExecutionEvent")) {
			reply = handleExecutionEvent(jsonObject);
		}
		else if(issue_type.equals("FailoverEvent")) {
			reply = handleFailoverEvent(jsonObject);
		}
		else if(issue_type.equals("RhinosSelfTest")) {
			reply = handleRhinosSelfTest(jsonObject);
		}
		else if(issue_type.equals("NetworkDown")) {
			reply = handleNetworkDown(jsonObject);
		}
		else if(issue_type.equals("PassThroughNetworkDisconnection")) {
			reply = handlePassThroughNetworkDisconnection(jsonObject);
		}
		else {
			//default case == passthrough:
			reply = handlePassthrough(jsonObject);
		}

		return reply;
	}
//	static class JsonKeys
//	{
//		static String issueTypeKey 		 	= new String("issue type"); 
//		static String impactedSystemsKey 	= new String("impacted systems");
//		static String hostnameKey 		 	= new String("hostname");
//		static String impactedMarketsKey 	= new String("impacted markets");
//		static String impactedFlowsKey 		= new String("impacted flows");
//		static String impactedClientsKey 	= new String("impacted clients");
//		static String originKey 			= new String("origin");
//		static String flowTypeKey 			= new String("flow type");
//		static String pnlKey				= new String("pnl");
//		static String timestampKey			= new String("timestamp");
//	}
	private JSONObject handlePassthrough(JSONObject jsonObject) {
		JSONObject reply = new JSONObject();
		try {
			reply.put("issue type", jsonObject.getString("issue type"));
			reply.put("impacted systems", Arrays.asList(jsonObject.getString("impacted systems").split(",")));
			reply.put("hostname",  jsonObject.getString("hostname"));
			reply.put("impacted markets",  Arrays.asList(jsonObject.getString("impacted markets").split(",")));
			reply.put("impacted flows",  Arrays.asList(jsonObject.getString("impacted flows").split(",")));
			reply.put("impacted clients", Arrays.asList(jsonObject.getString("impacted clients").split(",")));
			reply.put("pnl", Integer.parseInt(jsonObject.getString("pnl")));
			reply.put("timestamp", new Date().toString());
		}
		catch (Exception ex) {
			System.out.println("Unknown exception while processing a reply");
		}
		return reply;
	}
	
	private JSONObject handleNetworkDown(JSONObject jsonObject) {
		JSONObject reply = new JSONObject();
		try {
			reply.put("issue type", jsonObject.getString("issue type"));
			reply.put("impacted systems", Arrays.asList(jsonObject.getString("impacted systems").split(",")));
			reply.put("hostname",  jsonObject.getString("hostname"));
			reply.put("impacted markets",  Arrays.asList(jsonObject.getString("impacted markets").split(",")));
			reply.put("impacted flows",  Arrays.asList(jsonObject.getString("impacted flows").split(",")));
			reply.put("impacted clients", Arrays.asList(jsonObject.getString("impacted clients").split(",")));
			reply.put("pnl", Integer.parseInt(jsonObject.getString("pnl")));
			reply.put("timestamp", new Date().toString());
		}
		catch (Exception ex) {
			System.out.println("Unknown exception while processing a reply");
		}
		return reply;
	}
	
	private JSONObject handlePassThroughNetworkDisconnection(JSONObject jsonObject) {
		JSONObject reply = new JSONObject();
		reply.put("issue type", "NetworkDisconnection");
		reply.put("impacted systems", new ArrayList<>(Arrays.asList("OMS", "AlgoEngine")));
		reply.put("hostname", "bnpserver001");
		reply.put("impacted markets",  new ArrayList<>(Arrays.asList("HK", "JP")));
		JSONObject impactedFlows = new JSONObject();
//		impactedFlows.put("origin", "institutional clients");
//		impactedFlows.put("flow type", "FnO");
//		reply.put("impacted flows",  impactedFlows);
		reply.put("impacted flows",  impactedFlows);
		reply.put("impacted clients", new ArrayList<>(Arrays.asList("jasmine", "aladdin")));
		reply.put("pnl", 10000);
		reply.put("timestamp", new Date().toString());
		return reply;
	}
	
	
	
	private JSONObject handleExecutionEvent(JSONObject jsonObject) {
		JSONObject reply = new JSONObject();
		//TODO: processing logic
		return reply;
	}
	
	private JSONObject handleFailoverEvent(JSONObject jsonObject) {
		JSONObject reply = new JSONObject();
		//TODO: processing logic
		return reply;
	}
	
	private JSONObject handleRhinosSelfTest(JSONObject jsonObject) {
		JSONObject reply = new JSONObject();
		reply.put("RhionsTest", "JSON_FROM_AI_MODULE");
		return reply;
	}
	
}
