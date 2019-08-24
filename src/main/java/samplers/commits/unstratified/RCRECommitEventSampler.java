package samplers.commits.unstratified;

import events.Event;
import events.commits.CommitEvent;
import graphs.atRiskStreams.ProjectAtRiskStatus;
import graphs.atRiskStreams.UserAtRiskStatus;
import graphs.elements.WeightedEdge;

import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RCRECommitEventSampler extends UnstratifiedCommitEventSampler {

	private static final Logger LOG = LoggerFactory
			.getLogger(RCRECommitEventSampler.class.getName());

	public RCRECommitEventSampler() {
	}

	@Override
	protected void getCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {

		CommitEvent commitEvent = (CommitEvent) event;

		if (UserAtRiskStatus.getInstance().getRiskSet().size() > 0
				&& ProjectAtRiskStatus.getInstance().getRiskSet().size() > 0) {
			if (controlSamplingSize < UserAtRiskStatus.getInstance()
					.getRiskSet().size()
					* ProjectAtRiskStatus.getInstance().getRiskSet().size())
				getUnstratifiedRandomControls(commitEvent, selectedEdges);
			else
				getUnstratifiedFullControls(commitEvent, selectedEdges);
		} else {
			LOG.warn("Failed to sample an empty risk set at the event {}",
					commitEvent);
		}

	}

}
