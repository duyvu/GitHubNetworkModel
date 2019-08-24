/**
 * 
 */
package statistics.users.pullRequests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.RelationalUniplexNetworkStatistic;
import statistics.users.UserStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class RelationalPullRequestUserStatistic extends
		RelationalUniplexNetworkStatistic implements UserStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(RelationalPullRequestUserStatistic.class.getName());

	/**
	 * 
	 */
	public RelationalPullRequestUserStatistic() {
		senderNodeProperty = "userID";
		receiverNodeProperty = "repoID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped senderNodeProperty =  {}", senderNodeProperty);
		LOG.info(">> Mapped receiverNodeProperty =  {}", receiverNodeProperty);
	}

	@Override
	public double[] getValues(int userID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(userID, event);
		return results;
	}

}
