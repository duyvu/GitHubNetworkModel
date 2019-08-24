/**
 * 
 */
package statistics.issues.comments.paths;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import statistics.issues.comments.RelationalIssueCommentIssueStatistic;
import statistics.queries.UniplexEventQuery;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class TwoPathsIssueCommentIssueStatistic extends
		RelationalIssueCommentIssueStatistic {
	/**
	 * 
	 */
	public TwoPathsIssueCommentIssueStatistic() {
		super();
	}

	@Override
	public double getValue(int issueID, Event event) {
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty);
		return UniplexEventQuery.getTwoPaths(issueID, receiverNodeProperty,
				indexedReceiverEvents, senderNodeProperty, indexedSenderEvents);
	}
}
