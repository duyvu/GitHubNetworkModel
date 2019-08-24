/**
 * 
 */
package statistics.repos.commits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.EgocentricUniplexNetworkStatistic;
import statistics.repos.RepoStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class EgocentricCommitRepoStatistic extends
		EgocentricUniplexNetworkStatistic implements RepoStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(EgocentricCommitRepoStatistic.class.getName());

	/**
	 * 
	 */
	public EgocentricCommitRepoStatistic() {
		nodeProperty = "repoID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped nodeProperty =  {}", nodeProperty);
	}

	@Override
	public double[] getValues(int repoID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(repoID, event);
		return results;
	}

}
