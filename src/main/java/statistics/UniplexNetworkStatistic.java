/**
 * 
 */
package statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphs.relationalStreams.IndexedRelationalEventStream;
import graphs.relationalStreams.IndexedRelationalEventStreams;

/**
 * @author duyv
 * 
 */
public class UniplexNetworkStatistic extends NetworkStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(UniplexNetworkStatistic.class.getName());

	protected String relationalEventClassName;
	protected IndexedRelationalEventStream indexedRelationalEventStream;

	public UniplexNetworkStatistic() {
	}

	public String getRelationalEventClassName() {
		return relationalEventClassName;
	}

	public void setRelationalEventClassName(String relationalEventClassName) {
		this.relationalEventClassName = relationalEventClassName;
		indexedRelationalEventStream = IndexedRelationalEventStreams
				.getInstance().getIndexedRelationalEventStream(
						this.relationalEventClassName);
		LOG.info(
				"Mapping the statistic {} to the relational event stream {} which is an instance of {}",
				this.getClass().getSimpleName(), this.relationalEventClassName,
				indexedRelationalEventStream.getClass().getSimpleName());

	}

}
