/**
 * 
 */
package statistics.users.commits;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyv
 * 
 */
public class TimeFromLastCommitUserStatistic extends
		EgocentricCommitUserStatistic {

	public TimeFromLastCommitUserStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, Event event) {

		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(nodeProperty).get(userID);

		double lastEventTime;
		if (myEvents != null)
			lastEventTime = myEvents.get(myEvents.size() - 1).time;
		else
			lastEventTime = 0;

		return (event.time - lastEventTime);

	}

}
