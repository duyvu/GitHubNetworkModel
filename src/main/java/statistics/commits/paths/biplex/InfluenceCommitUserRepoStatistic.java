/**
 * 
 */
package statistics.commits.paths.biplex;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import events.Event;
import statistics.commits.CommitFollowUserRepoStatistic;
import statistics.queries.BiplexEventQuery;

/**
 * @author duyv
 * 
 */
public class InfluenceCommitUserRepoStatistic extends
		CommitFollowUserRepoStatistic {

	public InfluenceCommitUserRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int repoID, Event event) {
		
		ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedReceiverEvents = primaryIndexedRelationalEventStream
				.getPropertyMap(primaryReceiverNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> secondaryIndexedSenderEvents = secondaryIndexedRelationalEventStream
				.getPropertyMap(secondarySenderNodeProperty);

		return BiplexEventQuery.getBiplexTwoPaths(userID, repoID,
				primaryIndexedReceiverEvents, primarySenderNodeProperty,
				secondaryIndexedSenderEvents, secondaryReceiverNodeProperty);
		
	}

}
