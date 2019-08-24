/**
 * 
 */
package statistics.users.issues;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyv
 * 
 */
public class TimeFromLastIssueCommentUserStatistic extends
		EgocentricIssueCommentUserStatistic {

	public TimeFromLastIssueCommentUserStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, Event event) {

		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(nodeProperty).get(userID);

		double lastEventTime;
		if (myEvents != null)
			lastEventTime = myEvents.get(myEvents.size() - 1).time;
		else
			lastEventTime = 0;

		return (event.time - lastEventTime);

	}

}
