/**
 * 
 */
package statistics.users.pullRequests;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class ActivityPullRequestCommentUserStatistic extends
		EgocentricPullRequestCommentUserStatistic {

	/**
	 * 
	 */
	public ActivityPullRequestCommentUserStatistic() {
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
