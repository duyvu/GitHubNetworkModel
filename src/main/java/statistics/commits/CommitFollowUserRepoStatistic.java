/**
 * 
 */
package statistics.commits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.RelationalBiplexNetworkStatistic;

/**
 * @author duyv
 * 
 */
public abstract class CommitFollowUserRepoStatistic extends
		RelationalBiplexNetworkStatistic implements UserRepoStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(CommitFollowUserRepoStatistic.class.getName());

	public CommitFollowUserRepoStatistic() {
		primarySenderNodeProperty = "authorID";
		primaryReceiverNodeProperty = "repoID";

		secondarySenderNodeProperty = "senderID";
		secondaryReceiverNodeProperty = "receiverID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped primarySenderNodeProperty =  {}",
				primarySenderNodeProperty);
		LOG.info(">> Mapped primaryReceiverNodeProperty =  {}",
				primaryReceiverNodeProperty);
		LOG.info(">> Mapped secondarySenderNodeProperty =  {}",
				secondarySenderNodeProperty);
		LOG.info(">> Mapped secondaryReceiverNodeProperty =  {}",
				secondaryReceiverNodeProperty);
	}

	@Override
	public double[] getValues(int userID, int repoID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(userID, repoID, event);
		return results;
	}

}
