/**
 * 
 */
package statistics.members;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.RelationalUniplexNetworkStatistic;
import statistics.commits.UserRepoStatistic;

/**
 * @author duyv
 * 
 */
public abstract class MembershipUserRepoStatistic extends
		RelationalUniplexNetworkStatistic implements UserRepoStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(MembershipUserRepoStatistic.class.getName());

	public MembershipUserRepoStatistic() {
		senderNodeProperty = "userID";
		receiverNodeProperty = "repoID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped senderNodeProperty =  {}", senderNodeProperty);
		LOG.info(">> Mapped receiverNodeProperty =  {}", receiverNodeProperty);
	}

	@Override
	public double[] getValues(int userID, int repoID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(userID, repoID, event);
		return results;
	}

}
