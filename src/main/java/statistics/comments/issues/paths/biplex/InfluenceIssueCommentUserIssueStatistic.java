/**
 * 
 */
package statistics.comments.issues.paths.biplex;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import events.Event;
import statistics.comments.issues.IssueCommentFollowUserIssueStatistic;
import statistics.queries.BiplexEventQuery;

/**
 * @author duyv
 * 
 */
public class InfluenceIssueCommentUserIssueStatistic extends
		IssueCommentFollowUserIssueStatistic {

	public InfluenceIssueCommentUserIssueStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int issueID, Event event) {

		ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedReceiverEvents = primaryIndexedRelationalEventStream
				.getPropertyMap(primaryReceiverNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> secondaryIndexedSenderEvents = secondaryIndexedRelationalEventStream
				.getPropertyMap(secondarySenderNodeProperty);

		return BiplexEventQuery.getBiplexTwoPaths(userID, issueID,
				primaryIndexedReceiverEvents, primarySenderNodeProperty,
				secondaryIndexedSenderEvents, secondaryReceiverNodeProperty);

	}

}
