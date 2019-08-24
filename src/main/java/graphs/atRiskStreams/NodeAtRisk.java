package graphs.atRiskStreams;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import graphs.processors.AtRiskEventStreamProcessor;

public abstract class NodeAtRisk implements AtRiskStatus,
		AtRiskEventStreamProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(NodeAtRisk.class
			.getName());

	protected Set<Integer> atRiskSet = null;

	protected NodeAtRisk() {
		atRiskSet = Collections
				.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
	}

	@Override
	public void processAtRiskEvent(Event event) {
		LOG.warn(">> processAtRiskEvent(Event event))");
		LOG.warn("		Can not handle an event {}: [{}]", event.getClass()
				.getSimpleName(), event);
	}
	
	@Override
	public Set<Integer> getRiskSet() {
		return atRiskSet;
	}

	@Override
	/**
	 * Check if a commit is in the risk set
	 * 
	 * @return true is returned if the commit is in the set; otherwise, false is
	 *         returned if the commit is not in the set
	 */
	public boolean isAtRisk(int nodeID) {
		return atRiskSet.contains(nodeID);
	}
	
}
