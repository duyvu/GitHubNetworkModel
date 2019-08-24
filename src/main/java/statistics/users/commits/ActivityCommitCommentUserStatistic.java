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
public class ActivityCommitCommentUserStatistic extends
		EgocentricCommitCommentUserStatistic {

	/**
	 * 
	 */
	public ActivityCommitCommentUserStatistic() {
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
