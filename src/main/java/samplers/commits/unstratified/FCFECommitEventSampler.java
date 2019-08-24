package samplers.commits.unstratified;

public class FCFECommitEventSampler extends FCRECommitEventSampler {

	public FCFECommitEventSampler() {
		eventSamplingRatio = Double.MAX_VALUE;
	}

}
