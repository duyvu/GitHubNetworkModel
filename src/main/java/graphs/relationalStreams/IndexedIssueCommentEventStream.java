package graphs.relationalStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexedIssueCommentEventStream extends IndexedRelationalEventStream {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedIssueCommentEventStream.class.getName());

	private static volatile IndexedIssueCommentEventStream instance = null;

	public static IndexedIssueCommentEventStream getInstance() {
		if (instance == null) {
			synchronized (IndexedIssueCommentEventStream.class) {
				// Double check
				if (instance == null) {
					instance = new IndexedIssueCommentEventStream();
				}
			}
		}
		return instance;
	}

	protected final String[] DEFAULT_INDEXED_PROPERTY_NAMES = { "userID",
			"issueID", "repoID" };

	// private constructor
	private IndexedIssueCommentEventStream() {

		LOG.trace("** IssueCommentEvents()");

		setIndexedPropertyNames(DEFAULT_INDEXED_PROPERTY_NAMES);

	}

}
