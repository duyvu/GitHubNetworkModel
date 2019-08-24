package graphs.atRiskStreams;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.issues.IssueClosedEvent;
import events.issues.IssueOpenedEvent;

public class IssueAtRiskStatus extends NodeAtRisk {

	private static final Logger LOG = LoggerFactory
			.getLogger(IssueAtRiskStatus.class.getName());

	private static volatile IssueAtRiskStatus instance = null;

	public static IssueAtRiskStatus getInstance() {
		if (instance == null) {
			synchronized (IssueAtRiskStatus.class) {
				// Double check
				if (instance == null) {
					instance = new IssueAtRiskStatus();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<Integer, IssueOpenedEvent> issues = null;

	// private constructor
	private IssueAtRiskStatus() {
		issues = new ConcurrentHashMap<>();
	}

	/**
	 * Add a new issue to the database this implies the issue starts to be at
	 * risk In case the issue is already in the database, the issue event is
	 * updated
	 */
	public void addAtRiskIssueEvent(IssueOpenedEvent issueOpenedEvent) {

		LOG.trace("Add an issue: {}" + issueOpenedEvent);

		issues.put(issueOpenedEvent.getIssueID(), issueOpenedEvent);
		atRiskSet.add(issueOpenedEvent.getIssueID());
	}

	/**
	 * Remove a issue from the risk set
	 * 
	 * @return true is returned if the issue is removed from the set; otherwise,
	 *         false is returned if the issue is not in the set
	 */
	public boolean removeAtRiskIssue(int issueID) {

		LOG.trace("Remove an issue from the at risk set: {}"
				+ issues.get(issueID));

		return atRiskSet.remove(issueID);
	}

	@Override
	public void processAtRiskEvent(Event event) {
		if (event instanceof IssueOpenedEvent) {
			IssueOpenedEvent issueOpenedEvent = (IssueOpenedEvent) event;
			addAtRiskIssueEvent(issueOpenedEvent);
		} else if (event instanceof IssueClosedEvent) {
			IssueClosedEvent issueClosedEvent = (IssueClosedEvent) event;
			removeAtRiskIssue(issueClosedEvent.getIssueID());
		} else {
			super.processAtRiskEvent(event);
		}
	}

}
