/**
 * 
 */
package statistics.users.commits;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class ActivityCommitUserStatistic extends EgocentricCommitUserStatistic {
	
	/**
	 * 
	 */
	public ActivityCommitUserStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, Event event) {

		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(nodeProperty).get(userID);
		if (myEvents != null)
			return myEvents.size();
		else
			return 0;

	}
}
