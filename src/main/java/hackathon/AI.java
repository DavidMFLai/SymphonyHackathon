package hackathon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.json.JSONObject;

public class AI {
	public JSONObject processInputEvent(JSONObject jsonObject) {
		
		JSONObject reply = null;
		
		String type = (String)jsonObject.getString("Type");
		if(type.equals("ExecutionEvent")) {
			reply = handleExecutionEvent(jsonObject);
		}
		else if(type.equals("FailoverEvent")) {
			reply = handleFailoverEvent(jsonObject);
		}
		else if(type.equals("RhinosSelfTest")) {
			reply = handleRhinosSelfTest(jsonObject);
		}
		else if(type.equals("PassThroughNetworkDisconnection")) {
			reply = handlePassThroughNetworkDisconnection(jsonObject);
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
		impactedFlows.put("origin", "institutional clients");
		impactedFlows.put("flow type", "FnO");
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
