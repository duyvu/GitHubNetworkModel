package samplers.followers.unstratified;

import events.Event;
import events.users.FollowEvent;
import graphs.atRiskStreams.ProjectAtRiskStatus;
import graphs.atRiskStreams.UserAtRiskStatus;
import graphs.elements.WeightedEdge;

import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RCREFollowEventSampler extends UnstratifiedFollowEventSampler {

	private static final Logger LOG = LoggerFactory
			.getLogger(RCREFollowEventSampler.class.getName());

	public RCREFollowEventSampler() {
	}

	@Override
	protected void getCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {

		FollowEvent followEvent = (		FollowEvent) event;

		if (UserAtRiskStatus.getInstance().getRiskSet().size() > 0
				&& ProjectAtRiskStatus.getInstance().getRiskSet().size() > 0) {
			if (controlSamplingSize < UserAtRiskStatus.getInstance()
					.getRiskSet().size()
					* ProjectAtRiskStatus.getInstance().getRiskSet().size())
				getUnstratifiedRandomControls(followEvent, selectedEdges);
			else
				getUnstratifiedFullControls(followEvent, selectedEdges);
		} else {
			LOG.warn("Failed to sample an empty risk set at the event {}",
					followEvent);
		}

	}

}
