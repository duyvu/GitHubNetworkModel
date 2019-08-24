/**
 * 
 */
package statistics;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphs.relationalStreams.IndexedRelationalEventStream;
import graphs.relationalStreams.IndexedRelationalEventStreams;

/**
 * @author duyv
 * 
 */
public class BiplexNetworkStatistic extends NetworkStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(BiplexNetworkStatistic.class.getName());

	protected String relationalEventClassName;

	/*
	 * The first event stream is the main one, i.e. its events are of modelling
	 * interest using this statistic. For example, this will be the commit event
	 * stream when we are interested in modelling how the follow network affects
	 * the commit engagement.
	 * 
	 * The second event stream is the secondary one, i.e. the follow events in
	 * the above example.
	 */

	protected IndexedRelationalEventStream primaryIndexedRelationalEventStream;
	protected IndexedRelationalEventStream secondaryIndexedRelationalEventStream;

	public BiplexNetworkStatistic() {
	}

	public String getRelationalEventClassName() {
		return relationalEventClassName;
	}

	public void setRelationalEventClassName(String relationalEventClassName) {

		this.relationalEventClassName = relationalEventClassName;
		StringTokenizer tokenizer = new StringTokenizer(
				this.relationalEventClassName.trim(), ";");

		if (tokenizer.countTokens() != 2) {

			LOG.error("A statistic {} requires two relational event streams",
					this.getClass().getName());
			System.exit(-1);

		} else {

			String firstIndexedRelationalEventStreamName = tokenizer
					.nextToken().trim();
			primaryIndexedRelationalEventStream = IndexedRelationalEventStreams
					.getInstance().getIndexedRelationalEventStream(
							firstIndexedRelationalEventStreamName);

			String secondIndexedRelationalEventStreamName = tokenizer
					.nextToken().trim();
			secondaryIndexedRelationalEventStream = IndexedRelationalEventStreams
					.getInstance().getIndexedRelationalEventStream(
							secondIndexedRelationalEventStreamName);

			LOG.info(
					"Mapping the statistic {} to the relational event streams {} and {} which are instances of {} and {}",
					this.getClass().getSimpleName(),
					firstIndexedRelationalEventStreamName,
					secondIndexedRelationalEventStreamName,
					primaryIndexedRelationalEventStream.getClass().getName(),
					secondaryIndexedRelationalEventStream.getClass().getName());
		}
	}

}
