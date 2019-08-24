package graphs.relationalStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexedPullRequestOpenedEventStream extends IndexedRelationalEventStream {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedPullRequestOpenedEventStream.class.getName());

	private static volatile IndexedPullRequestOpenedEventStream instance = null;

	public static IndexedPullRequestOpenedEventStream getInstance() {
		if (instance == null) {
			synchronized (IndexedPullRequestOpenedEventStream.class) {
				// Double check
				if (instance == null) {
					instance = new IndexedPullRequestOpenedEventStream();
				}
			}
		}
		return instance;
	}

	protected final String[] DEFAULT_INDEXED_PROPERTY_NAMES = { "userID",
			"repoID" };

	// private constructor
	private IndexedPullRequestOpenedEventStream() {

		LOG.trace("** PullRequestOpenedEvents()");

		setIndexedPropertyNames(DEFAULT_INDEXED_PROPERTY_NAMES);

	}

}
