package samplers.followers.unstratified;

import events.Event;
import events.users.FollowEvent;
import graphs.elements.WeightedEdge;

import java.util.TreeSet;

public class FCREFollowEventSampler extends UnstratifiedFollowEventSampler {

	public FCREFollowEventSampler() {
	}

	@Override
	protected void getCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {
		getUnstratifiedFullControls((FollowEvent) event, selectedEdges);
	}

}
