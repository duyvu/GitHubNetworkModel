/**
 * 
 */
package statistics.followers.paths;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import events.Event;
import statistics.followers.IssueCommentSenderReceiverStatistic;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class HomophilyIssueCommentSenderReceiverStatistic extends
		IssueCommentSenderReceiverStatistic {

	public HomophilyIssueCommentSenderReceiverStatistic() {
		super();
	}

	@Override
	public double getValue(int senderID, int receiverID, Event event) {
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty);
		return UniplexEventQuery.getSharedPaths(senderID, receiverID,
				indexedSenderEvents, receiverNodeProperty);
	}
}
