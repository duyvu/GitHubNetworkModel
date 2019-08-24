package graphs.atRiskStreams;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.users.UserOpenedEvent;

public class UserAtRiskStatus extends NodeAtRisk {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserAtRiskStatus.class.getName());

	private static volatile UserAtRiskStatus instance = null;

	public static UserAtRiskStatus getInstance() {
		if (instance == null) {
			synchronized (UserAtRiskStatus.class) {
				// Double check
				if (instance == null) {
					instance = new UserAtRiskStatus();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<Integer, UserOpenedEvent> users = null;

	// private constructor
	private UserAtRiskStatus() {
		users = new ConcurrentHashMap<>();		
	}

	/**
	 * Add a new user to the database this implies he or she starts to be at
	 * risk In case the user is already in the database, his or her event is
	 * updated
	 */
	public void addAtRiskUserEvent(UserOpenedEvent userOpenedEvent) {

		LOG.trace("Add a user: {}" + userOpenedEvent);

		users.put(userOpenedEvent.getUserID(), userOpenedEvent);
		atRiskSet.add(userOpenedEvent.getUserID());
	}

	/**
	 * Remove a user from the risk set
	 * 
	 * @return true is returned if the user is removed from the set; otherwise,
	 *         false is returned if the user is not in the set
	 */
	public boolean removeAtRiskUser(int userID) {

		LOG.trace("Remove a user from the at risk set: {}" + users.get(userID));

		return atRiskSet.remove(userID);
	}

	@Override
	public void processAtRiskEvent(Event event) {
		if (event instanceof UserOpenedEvent) {
			UserOpenedEvent userOpenedEvent = (UserOpenedEvent) event;
			addAtRiskUserEvent(userOpenedEvent);
		} else {
			super.processAtRiskEvent(event);
		}
	}

}
