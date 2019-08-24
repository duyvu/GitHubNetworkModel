/**
 * 
 */
package statistics.commits.paths.biplex;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import events.Event;
import statistics.commits.CommitMembershipUserRepoStatistic;
import statistics.queries.BiplexEventQuery;

/**
 * @author duyv
 * 
 */
public class ThreePathsMembershipCommitUserRepoStatistic extends
		CommitMembershipUserRepoStatistic {

	public ThreePathsMembershipCommitUserRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int repoID, Event event) {

		ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedSenderEvents = primaryIndexedRelationalEventStream
				.getPropertyMap(primarySenderNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedReceiverEvents = primaryIndexedRelationalEventStream
				.getPropertyMap(primaryReceiverNodeProperty);

		ConcurrentHashMap<Integer, ArrayList<Event>> secondaryIndexedSenderEvents = secondaryIndexedRelationalEventStream
				.getPropertyMap(secondarySenderNodeProperty);

		return BiplexEventQuery.getDisjointNodeSetThreePaths(userID, repoID,
				primaryIndexedSenderEvents, primarySenderNodeProperty,
				primaryIndexedReceiverEvents, primaryReceiverNodeProperty,
				secondaryIndexedSenderEvents, secondaryReceiverNodeProperty);

	}

}
