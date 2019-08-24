/**
 * 
 */
package statistics.users.commits.paths;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import statistics.queries.UniplexEventQuery;
import statistics.users.commits.RelationalCommitUserStatistic;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class TwoPathsCommitUserStatistic extends RelationalCommitUserStatistic {
	/**
	 * 
	 */
	public TwoPathsCommitUserStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, Event event) {
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty);
		return UniplexEventQuery.getTwoPaths(userID, senderNodeProperty,
				indexedSenderEvents, receiverNodeProperty,
				indexedReceiverEvents);
	}
}
