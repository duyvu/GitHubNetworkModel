/**
 * 
 */
package statistics.followers.members;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;

import events.Event;
import statistics.followers.ProjectMemberSenderReceiverStatistic;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class DisjointProjectMemberSetSenderReceiverStatistic extends
		ProjectMemberSenderReceiverStatistic {

	public DisjointProjectMemberSetSenderReceiverStatistic() {
		super();
	}

	@Override
	public double getValue(int senderID, int receiverID, Event event) {
		
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty);

		TreeSet<Integer> senderProjects = UniplexEventQuery
				.getUniquePropertyValues(
						indexedSenderEvents.get(senderID),
						receiverNodeProperty);
		TreeSet<Integer> receiverProjects = UniplexEventQuery
				.getUniquePropertyValues(
						indexedSenderEvents.get(receiverID),
						receiverNodeProperty);

		return (CollectionUtils.disjunction(senderProjects, receiverProjects)
				.size() > 0) ? 1.0 : 0.0;
	
	}
	
}
