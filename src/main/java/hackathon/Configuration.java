package hackathon;

public class Configuration {
	
	public Configuration(int latencyThresholdMicroseconds, int rejectionsPerSecondThreshold) {
		this.latencyThresholdMicroseconds = latencyThresholdMicroseconds;
		this.rejectionsPerSecondThreshold = rejectionsPerSecondThreshold;
	}
	
	public int getLatencyThresholdMicroseconds() {
		return latencyThresholdMicroseconds;
	}

	public int getRejectionsPerSecondThreshold() {
		return rejectionsPerSecondThreshold;
	}

	private final int latencyThresholdMicroseconds;
	private final int rejectionsPerSecondThreshold;
}