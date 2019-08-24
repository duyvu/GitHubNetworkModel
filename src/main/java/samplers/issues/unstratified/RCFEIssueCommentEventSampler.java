package samplers.issues.unstratified;

public class RCFEIssueCommentEventSampler extends RCREIssueCommentEventSampler {

	public RCFEIssueCommentEventSampler() {
		eventSamplingRatio = Double.MAX_VALUE;
	}

}
