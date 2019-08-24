package samplers.commits.unstratified;

public class RCFECommitEventSampler extends RCRECommitEventSampler {

	public RCFECommitEventSampler() {
		eventSamplingRatio = Double.MAX_VALUE;
	}

}
