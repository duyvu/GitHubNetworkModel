package graphs.atRiskStreams;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.commits.CommitEvent;

public class CommitAtRiskStatus extends NodeAtRisk {

	private static final Logger LOG = LoggerFactory
			.getLogger(CommitAtRiskStatus.class.getName());

	private static volatile CommitAtRiskStatus instance = null;

	public static CommitAtRiskStatus getInstance() {
		if (instance == null) {
			synchronized (CommitAtRiskStatus.class) {
				// Double check
				if (instance == null) {
					instance = new CommitAtRiskStatus();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<Integer, CommitEvent> commits = null;

	// private constructor
	private CommitAtRiskStatus() {
		commits = new ConcurrentHashMap<>();
	}

	/**
	 * Add a new commit to the database this implies the commit starts to be at
	 * risk In case the commit is already in the database, the commit event is
	 * updated
	 */
	public void addAtRiskCommitEvent(CommitEvent commitEvent) {

		LOG.trace("Add a commit: {}" + commitEvent);

		commits.put(commitEvent.getCommitID(), commitEvent);
		atRiskSet.add(commitEvent.getCommitID());
	}

	/**
	 * Remove a commit from the risk set
	 * 
	 * @return true is returned if the commit is removed from the set;
	 *         otherwise, false is returned if the commit is not in the set
	 */
	public boolean removeAtRiskCommit(int commitID) {

		LOG.trace("Remove a commit from the at risk set: {}"
				+ commits.get(commitID));

		return atRiskSet.remove(commitID);
	}

	@Override
	public void processAtRiskEvent(Event event) {
		if (event instanceof CommitEvent) {
			CommitEvent commitEvent = (CommitEvent) event;
			addAtRiskCommitEvent(commitEvent);
		} else {
			super.processAtRiskEvent(event);
		}
	}

}
