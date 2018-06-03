package hackathon;

import java.util.Arrays;
import java.util.Date;

import org.json.JSONObject;

public class AI {
	private int consecutiveRejectCount = 0;
	private int consecutiveCancelCount = 0;
	private int consecutiveExecCount = 0;

	public JSONObject processInputEvent(JSONObject jsonObject) {
		JSONObject reply;

		String issue_type = (String) jsonObject.getString("issue type");
		System.out.println("Received event[" + issue_type + "]");
		if (issue_type.equals("Reject")) {
			reply = handleReject(jsonObject);
		} else if (issue_type.equals("Cancel")) {
			reply = handleCancel(jsonObject);
		} else if (issue_type.equals("Exec")) {
			reply = handleExec(jsonObject);
		} else {
			// default case == passthrough:
			reply = handlePassthrough(jsonObject);
		}
		
		return reply;
	}

	private JSONObject handlePassthrough(JSONObject jsonObject) {
		JSONObject reply = new JSONObject();
		try {
			reply.put("issue type", jsonObject.getString("issue type"));
			reply.put("impacted systems", Arrays.asList(jsonObject.getString("impacted systems").split(",")));
			reply.put("hostname", jsonObject.getString("hostname"));
			reply.put("impacted markets", Arrays.asList(jsonObject.getString("impacted markets").split(",")));
			reply.put("impacted flows", Arrays.asList(jsonObject.getString("impacted flows").split(",")));
			reply.put("impacted clients", Arrays.asList(jsonObject.getString("impacted clients").split(",")));
			reply.put("pnl", Integer.parseInt(jsonObject.getString("pnl")));
			reply.put("timestamp", new Date().toString());
		} catch (Exception ex) {
			System.out.println("Unknown exception while processing a reply");
		}
		return reply;
	}

	private JSONObject handleExec(JSONObject jsonObject) {
		consecutiveRejectCount = 0;
		consecutiveCancelCount = 0;
		consecutiveExecCount++;
		if (consecutiveExecCount >= 500) {
			try {
				JSONObject reply = new JSONObject();
				reply.put("issue type", "HighVolume");
				reply.put("impacted systems", Arrays.asList(jsonObject.getString("impacted systems").split(",")));
				reply.put("hostname", jsonObject.getString("hostname"));
				reply.put("impacted markets", Arrays.asList(jsonObject.getString("impacted markets").split(",")));
				reply.put("impacted flows", Arrays.asList(jsonObject.getString("impacted flows").split(",")));
				reply.put("impacted clients", Arrays.asList(jsonObject.getString("impacted clients").split(",")));
				reply.put("pnl", Integer.parseInt(jsonObject.getString("pnl")));
				reply.put("timestamp", new Date().toString());
				consecutiveExecCount = 0;
				return reply;
			} catch (Exception ex) {
				System.out.println("Unknown exception while processing a reply in handleExec()");
				return null;
			}
		} else {
			return null;
		}
	}

	private JSONObject handleReject(JSONObject jsonObject) {
		consecutiveRejectCount++;
		consecutiveCancelCount = 0;
		consecutiveExecCount = 0;
		if (consecutiveRejectCount >= 500) {
			try {
				JSONObject reply = new JSONObject();
				reply.put("issue type", "RepeatedRejects");
				reply.put("impacted systems", Arrays.asList(jsonObject.getString("impacted systems").split(",")));
				reply.put("hostname", jsonObject.getString("hostname"));
				reply.put("impacted markets", Arrays.asList(jsonObject.getString("impacted markets").split(",")));
				reply.put("impacted flows", Arrays.asList(jsonObject.getString("impacted flows").split(",")));
				reply.put("impacted clients", Arrays.asList(jsonObject.getString("impacted clients").split(",")));
				reply.put("pnl", Integer.parseInt(jsonObject.getString("pnl")));
				reply.put("timestamp", new Date().toString());
				consecutiveRejectCount = 0;
				return reply;
			} catch (Exception ex) {
				System.out.println("Unknown exception while processing a reply in handleReject()");
				return null;
			}
		} else {
			return null;
		}
	}

	private JSONObject handleCancel(JSONObject jsonObject) {
		consecutiveRejectCount = 0;
		consecutiveCancelCount++;
		consecutiveExecCount = 0;
		if (consecutiveCancelCount >= 500) {
			try {
				JSONObject reply = new JSONObject();
				reply.put("issue type", "RepeatedCancels");
				reply.put("impacted systems", Arrays.asList(jsonObject.getString("impacted systems").split(",")));
				reply.put("hostname", jsonObject.getString("hostname"));
				reply.put("impacted markets", Arrays.asList(jsonObject.getString("impacted markets").split(",")));
				reply.put("impacted flows", Arrays.asList(jsonObject.getString("impacted flows").split(",")));
				reply.put("impacted clients", Arrays.asList(jsonObject.getString("impacted clients").split(",")));
				reply.put("pnl", Integer.parseInt(jsonObject.getString("pnl")));
				reply.put("timestamp", new Date().toString());
				consecutiveCancelCount = 0;
				return reply;
			} catch (Exception ex) {
				System.out.println("Unknown exception while processing a reply in handleReject()");
				return null;
			}
		} else {
			return null;
		}
	}

}
