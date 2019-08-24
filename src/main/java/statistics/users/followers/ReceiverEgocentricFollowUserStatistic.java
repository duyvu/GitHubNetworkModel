/**
 * 
 */
package statistics.users.followers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.EgocentricUniplexNetworkStatistic;
import statistics.users.UserStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class ReceiverEgocentricFollowUserStatistic extends
		EgocentricUniplexNetworkStatistic implements UserStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(ReceiverEgocentricFollowUserStatistic.class.getName());

	/**
	 * 
	 */
	public ReceiverEgocentricFollowUserStatistic() {
		nodeProperty = "receiverID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped nodeProperty =  {}", nodeProperty);
	}

	@Override
	public double[] getValues(int userID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(userID, event);
		return results;
	}

}
