/**
 * 
 */
package statistics.followers;

import java.util.ArrayList;

import events.Event;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class ReciprocityFollowSenderReceiverStatistic extends
		FollowSenderReceiverStatistic {

	public ReciprocityFollowSenderReceiverStatistic() {
		super();
	}

	@Override
	public double getValue(int senderID, int receiverID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty).get(receiverID);
		if (myEvents != null)
			return UniplexEventQuery.filterEventsByProperty(myEvents,
					receiverNodeProperty, senderID).size();
		else
			return 0;
	}

}
