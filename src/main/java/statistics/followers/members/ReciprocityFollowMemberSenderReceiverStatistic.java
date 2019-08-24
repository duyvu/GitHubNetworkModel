/**
 * 
 */
package statistics.followers.members;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;

import events.Event;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class ReciprocityFollowMemberSenderReceiverStatistic extends
		FollowMemberSenderReceiverStatistic {

	public ReciprocityFollowMemberSenderReceiverStatistic() {
		super();
	}

	@Override
	public double getValue(int senderID, int receiverID, Event event) {

		ArrayList<Event> myEvents = primaryIndexedRelationalEventStream
				.getPropertyMap(primarySenderNodeProperty).get(receiverID);

		ConcurrentHashMap<Integer, ArrayList<Event>> secondaryIndexedSenderEvents = secondaryIndexedRelationalEventStream
				.getPropertyMap(secondarySenderNodeProperty);
		TreeSet<Integer> senderProjects = UniplexEventQuery
				.getUniquePropertyValues(
						secondaryIndexedSenderEvents.get(senderID),
						secondaryReceiverNodeProperty);
		TreeSet<Integer> receiverProjects = UniplexEventQuery
				.getUniquePropertyValues(
						secondaryIndexedSenderEvents.get(receiverID),
						secondaryReceiverNodeProperty);

		if (myEvents != null) {
			double reciprocatedEdge = UniplexEventQuery.filterEventsByProperty(
					myEvents, primaryReceiverNodeProperty, senderID).size();
			if (reciprocatedEdge > 0.0)
				return (CollectionUtils.disjunction(senderProjects,
						receiverProjects).size() > 0) ? 1.0 : 0.0;
			else
				return 0.0;
		} else
			return 0.0;
		
	}
}
