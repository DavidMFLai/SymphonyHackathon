package hackathon;

public class Configuration {
	
	public Configuration(int latencyThresholdMilliseconds, int rejectionsPerSecondThreshold) {
		this.latencyThresholdMilliseconds = latencyThresholdMilliseconds;
		this.rejectionsPerSecondThreshold = rejectionsPerSecondThreshold;
	}
	
	public int getLatencyThresholdMilliseconds() {
		return latencyThresholdMilliseconds;
	}

	public int getRejectionsPerSecondThreshold() {
		return rejectionsPerSecondThreshold;
	}

	private final int latencyThresholdMilliseconds;
	private final int rejectionsPerSecondThreshold;
}