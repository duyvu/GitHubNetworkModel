/**
 * 
 */
package statistics.repos.commits;

import java.util.ArrayList;

import statistics.queries.UniplexEventQuery;
import events.Event;

/**
 * @author duyvu
 * 
 */
public class DegreeCommitRepoStatistic extends RelationalCommitRepoStatistic {

	/**
	 * 
	 */
	public DegreeCommitRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int repoID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty).get(repoID);
		if (myEvents != null)
			return UniplexEventQuery.getUniquePropertyValues(myEvents,
					senderNodeProperty).size();
		else
			return 0;
	}

}
