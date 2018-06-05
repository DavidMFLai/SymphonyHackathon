package hackathon;

import java.util.Arrays;
import java.util.Date;

import org.json.JSONObject;

public class AI {
	private int consecutiveCancelCount = 0;
	private int consecutiveExecCount = 0;
	long []timestampsOfRecentRejectMessages;
	int currentIdxTimestampsOfRecentRejectMessages = 0;
	private Configuration config;

	public AI(Configuration config) {
		this.config = config;
		timestampsOfRecentRejectMessages = new long[this.config.getRejectionsPerSecondThreshold()];
	}
	
	public JSONObject processInputEvent(JSONObject jsonObject) {
		JSONObject reply;

		String issue_type = (String) jsonObject.getString("issue type");
		System.out.println("Received event[" + issue_type + "]");
		if (issue_type.equals("Reject")) {
			reply = handleReject(jsonObject);
		} else if (issue_type.equals("MarketDataSlowness")) {
			reply = handleMarketDataSlowness(jsonObject);
		} else if (issue_type.equals("RepeatedCancels")) {
			reply = null;
		} else if (issue_type.equals("Exec")) {
			reply = null;
		} else if (issue_type.equals("IncorrectPriceRange")) {
			reply = null;
		} else if (issue_type.equals("Failover")) {
			reply = null;
		} else {
			// default case == passthrough:
			reply = handlePassthrough(jsonObject);
		}
		
		return reply;
	}

	private JSONObject handleMarketDataSlowness(JSONObject jsonObject) {
		
		JSONObject reply = null;
		try {
			if(jsonObject.getLong("LatencyInMs") > this.config.getLatencyThresholdMilliseconds()) {
				reply = new JSONObject();
				reply.put("issue type", jsonObject.getString("issue type"));
				reply.put("impacted systems", Arrays.asList(jsonObject.getString("impacted systems").split(",")));
				reply.put("hostname", jsonObject.getString("hostname"));
				reply.put("impacted markets", Arrays.asList(jsonObject.getString("impacted markets").split(",")));
				reply.put("impacted flows", Arrays.asList(jsonObject.getString("impacted flows").split(",")));
				reply.put("impacted clients", Arrays.asList(jsonObject.getString("impacted clients").split(",")));
				reply.put("pnl", Integer.parseInt(jsonObject.getString("pnl")));
				reply.put("timestamp", new Date().toString());
			}

		} catch (Exception ex) {
			System.out.println("Unknown exception while processing a reply");
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
		consecutiveCancelCount = 0;
		consecutiveExecCount = 0;
		long timestamp = jsonObject.getLong("timestamp");
		
		timestampsOfRecentRejectMessages[currentIdxTimestampsOfRecentRejectMessages] = timestamp;
		currentIdxTimestampsOfRecentRejectMessages++;
		if (currentIdxTimestampsOfRecentRejectMessages >= this.config.getRejectionsPerSecondThreshold()) {
			currentIdxTimestampsOfRecentRejectMessages = 0;
		}
		
		if (timestamp - timestampsOfRecentRejectMessages[currentIdxTimestampsOfRecentRejectMessages] < 1000*1000) //if difference is less than 1 second
		{
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
				return reply;
			} catch (Exception ex) {
				System.out.println("Unknown exception while processing a reply in handleReject()");
				return null;
			}
		}
		else {
			return null;
		}
	}

	private JSONObject handleCancel(JSONObject jsonObject) {
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
