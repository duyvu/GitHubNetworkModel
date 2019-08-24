/**
 * 
 */
package statistics.repos.commits;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class ActivityCommitRepoStatistic extends EgocentricCommitRepoStatistic {

	/**
	 * 
	 */
	public ActivityCommitRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int repoID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(nodeProperty).get(repoID);
		if (myEvents != null)
			return myEvents.size();
		else
			return 0;
	}

}
