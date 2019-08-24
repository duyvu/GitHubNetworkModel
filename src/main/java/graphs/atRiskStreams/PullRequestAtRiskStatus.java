package graphs.atRiskStreams;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.pullRequests.PullRequestClosedEvent;
import events.pullRequests.PullRequestOpenedEvent;

public class PullRequestAtRiskStatus extends NodeAtRisk {

	private static final Logger LOG = LoggerFactory
			.getLogger(PullRequestAtRiskStatus.class.getName());

	private static volatile PullRequestAtRiskStatus instance = null;

	public static PullRequestAtRiskStatus getInstance() {
		if (instance == null) {
			synchronized (PullRequestAtRiskStatus.class) {
				// Double check
				if (instance == null) {
					instance = new PullRequestAtRiskStatus();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<Integer, PullRequestOpenedEvent> pullRequests = null;

	// private constructor
	private PullRequestAtRiskStatus() {
		pullRequests = new ConcurrentHashMap<>();
	}

	/**
	 * Add a new pull request to the database this implies the pull request
	 * starts to be at risk In case the pullRequest is already in the database,
	 * the pull request event is updated
	 */
	public void addAtRiskPullRequestEvent(
			PullRequestOpenedEvent pullRequestOpenedEvent) {

		LOG.trace("Add a pull request: {}" + pullRequestOpenedEvent);

		pullRequests.put(pullRequestOpenedEvent.getPullRequestID(),
				pullRequestOpenedEvent);
		atRiskSet.add(pullRequestOpenedEvent.getPullRequestID());
	}

	/**
	 * Remove a pull request from the risk set
	 * 
	 * @return true is returned if the pull request is removed from the set;
	 *         otherwise, false is returned if the pull request is not in the
	 *         set
	 */
	public boolean removeAtRiskPullRequest(int pullRequestID) {

		LOG.trace("Remove a pull request from the at risk set: {}"
				+ pullRequests.get(pullRequestID));

		return atRiskSet.remove(pullRequestID);
	}

	@Override
	public void processAtRiskEvent(Event event) {
		if (event instanceof PullRequestOpenedEvent) {
			PullRequestOpenedEvent pullRequestOpenedEvent = (PullRequestOpenedEvent) event;
			addAtRiskPullRequestEvent(pullRequestOpenedEvent);
		} else if (event instanceof PullRequestClosedEvent) {
			PullRequestClosedEvent pullRequestClosedEvent = (PullRequestClosedEvent) event;
			removeAtRiskPullRequest(pullRequestClosedEvent.getPullRequestID());
		} else {
			super.processAtRiskEvent(event);
		}
	}

}
