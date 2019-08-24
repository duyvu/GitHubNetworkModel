/**
 * 
 */
package statistics.issues.comments;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyv
 * 
 */
public class TimeFromLastIssueCommentIssueStatistic extends
		EgocentricIssueCommentIssueStatistic {

	public TimeFromLastIssueCommentIssueStatistic() {
		super();
	}

	@Override
	public double getValue(int issueID, Event event) {

		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(nodeProperty).get(issueID);

		double lastEventTime;
		if (myEvents != null)
			lastEventTime = myEvents.get(myEvents.size() - 1).time;
		else
			lastEventTime = 0;

		return (event.time - lastEventTime);

	}

}
