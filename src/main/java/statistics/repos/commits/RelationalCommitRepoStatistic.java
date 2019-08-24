/**
 * 
 */
package statistics.repos.commits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.RelationalUniplexNetworkStatistic;
import statistics.repos.RepoStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class RelationalCommitRepoStatistic extends
		RelationalUniplexNetworkStatistic implements RepoStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(RelationalCommitRepoStatistic.class.getName());

	/**
	 * 
	 */
	public RelationalCommitRepoStatistic() {
		senderNodeProperty = "authorID";
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
