/**
 * 
 */
package statistics.followers.paths;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import events.Event;
import statistics.followers.FollowSenderReceiverStatistic;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class TransitivityFollowSenderReceiverStatistic extends
		FollowSenderReceiverStatistic {

	public TransitivityFollowSenderReceiverStatistic() {
		super();
	}

	@Override
	public double getValue(int senderID, int receiverID, Event event) {
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty);
		return UniplexEventQuery.getTwoPaths(senderID, receiverID,
				senderNodeProperty, indexedSenderEvents, receiverNodeProperty,
				indexedReceiverEvents);
	}
}
