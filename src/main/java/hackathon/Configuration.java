package hackathon;

public class Configuration {
	
	public Configuration(int latencyThresholdMilliseconds, int rejectionsPerSecondThreshold, int cancelsPerSecondThreshold) {
		this.latencyThresholdMilliseconds = latencyThresholdMilliseconds;
		this.rejectionsPerSecondThreshold = rejectionsPerSecondThreshold;
		this.cancelsPerSecondThreshold = cancelsPerSecondThreshold;
	}
	
	public int getLatencyThresholdMilliseconds() {
		return latencyThresholdMilliseconds;
	}

	public int getRejectionsPerSecondThreshold() {
		return rejectionsPerSecondThreshold;
	}

	public int getCancelsPerSecondThreshold() {
		return cancelsPerSecondThreshold;
	}

	private final int latencyThresholdMilliseconds;
	private final int rejectionsPerSecondThreshold;
	private final int cancelsPerSecondThreshold;
}