/**
 * 
 */
package statistics.comments.issues;

import java.util.ArrayList;

import events.Event;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class ActivityIssueCommentUserIssueStatistic extends IssueCommentUserIssueStatistic {

	public ActivityIssueCommentUserIssueStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int issueID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty).get(userID);
		if (myEvents != null)
			return UniplexEventQuery.filterEventsByProperty(myEvents,
					receiverNodeProperty, issueID).size();
		else
			return 0;
	}

}
