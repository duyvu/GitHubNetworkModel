/**
 * 
 */
package statistics.repos.commits;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyv
 * 
 */
public class TimeFromLastCommitRepoStatistic extends
		EgocentricCommitRepoStatistic {

	public TimeFromLastCommitRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int repoID, Event event) {

		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(nodeProperty).get(repoID);

		double lastEventTime;
		if (myEvents != null)
			lastEventTime = myEvents.get(myEvents.size() - 1).time;
		else
			lastEventTime = 0;

		return (event.time - lastEventTime);

	}

}
