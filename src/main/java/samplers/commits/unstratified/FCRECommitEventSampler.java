package samplers.commits.unstratified;

import events.Event;
import events.commits.CommitEvent;
import graphs.elements.WeightedEdge;

import java.util.TreeSet;

public class FCRECommitEventSampler extends UnstratifiedCommitEventSampler {

	public FCRECommitEventSampler() {
	}

	@Override
	protected void getCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {
		getUnstratifiedFullControls((CommitEvent) event, selectedEdges);
	}

}
