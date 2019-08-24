package samplers.issues.unstratified;

import events.Event;
import events.issues.IssueCommentEvent;
import graphs.elements.WeightedEdge;

import java.util.TreeSet;

public class FCREIssueCommentEventSampler extends
		UnstratifiedIssueCommentEventSampler {

	public FCREIssueCommentEventSampler() {
	}

	@Override
	protected void getCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {
		getUnstratifiedFullControls((IssueCommentEvent) event, selectedEdges);
	}

}
