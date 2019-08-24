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
public class TimeWeightedActivityIssueCommentUserIssueStatistic extends
		IssueCommentUserIssueStatistic {

	protected double exponentialWeight = -1.0;

	public void setExponentialWeight(Double exponentialWeight) {
		this.exponentialWeight = exponentialWeight;
	}

	public void setExponentialWeight(double exponentialWeight) {
		this.exponentialWeight = exponentialWeight;
	}

	public TimeWeightedActivityIssueCommentUserIssueStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int issueID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty).get(userID);
		if (myEvents != null)
			return UniplexEventQuery.getTimeWeightedCount(UniplexEventQuery
					.filterEventsByProperty(myEvents, receiverNodeProperty,
							issueID), event.getTime(), exponentialWeight);
		else
			return 0;
	}

}
