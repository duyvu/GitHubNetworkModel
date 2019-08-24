package samplers.issues.unstratified;

import events.Event;
import events.issues.IssueCommentEvent;
import graphs.atRiskStreams.UserAtRiskStatus;
import graphs.atRiskStreams.IssueAtRiskStatus;
import graphs.elements.WeightedEdge;

import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RCREIssueCommentEventSampler extends
		UnstratifiedIssueCommentEventSampler {

	private static final Logger LOG = LoggerFactory
			.getLogger(RCREIssueCommentEventSampler.class.getName());

	public RCREIssueCommentEventSampler() {
	}

	@Override
	protected void getCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {

		IssueCommentEvent issueCommentEvent = (IssueCommentEvent) event;

		if (UserAtRiskStatus.getInstance().getRiskSet().size() > 0
				&& IssueAtRiskStatus.getInstance().getRiskSet().size() > 0) {
			if (controlSamplingSize < UserAtRiskStatus.getInstance()
					.getRiskSet().size()
					* IssueAtRiskStatus.getInstance().getRiskSet().size())
				getUnstratifiedRandomControls(issueCommentEvent, selectedEdges);
			else
				getUnstratifiedFullControls(issueCommentEvent, selectedEdges);
		} else {
			LOG.warn("Failed to sample an empty risk set at the event {}",
					issueCommentEvent);
		}

	}

}
