/**
 * 
 */
package statistics.comments.issues.paths;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import events.Event;
import statistics.comments.issues.IssueCommentUserIssueStatistic;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class ThreePathsIssueCommentUserIssueStatistic extends
		IssueCommentUserIssueStatistic {

	public ThreePathsIssueCommentUserIssueStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int repoID, Event event) {
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty);
		return UniplexEventQuery.getThreePaths(userID, repoID,
				senderNodeProperty, indexedSenderEvents, receiverNodeProperty,
				indexedReceiverEvents);
	}
}
