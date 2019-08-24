package graphs.relationalStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexedPullRequestCommentEventStream extends IndexedRelationalEventStream {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedPullRequestCommentEventStream.class.getName());

	private static volatile IndexedPullRequestCommentEventStream instance = null;

	public static IndexedPullRequestCommentEventStream getInstance() {
		if (instance == null) {
			synchronized (IndexedPullRequestCommentEventStream.class) {
				// Double check
				if (instance == null) {
					instance = new IndexedPullRequestCommentEventStream();
				}
			}
		}
		return instance;
	}

	protected final String[] DEFAULT_INDEXED_PROPERTY_NAMES = { "userID",
			"pullRequestID" };

	// private constructor
	private IndexedPullRequestCommentEventStream() {

		LOG.trace("** PullRequestCommentEvents()");

		setIndexedPropertyNames(DEFAULT_INDEXED_PROPERTY_NAMES);

	}

}
