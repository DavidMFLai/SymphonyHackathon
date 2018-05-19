package hackathon;

import java.util.Map;

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
