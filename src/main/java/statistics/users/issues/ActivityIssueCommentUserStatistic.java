/**
 * 
 */
package statistics.users.issues;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class ActivityIssueCommentUserStatistic extends
		EgocentricIssueCommentUserStatistic {

	/**
	 * 
	 */
	public ActivityIssueCommentUserStatistic() {
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
