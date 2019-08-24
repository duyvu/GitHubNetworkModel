package graphs.relationalStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexedCommitCommentEventStream extends
		IndexedRelationalEventStream {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedCommitCommentEventStream.class.getName());

	private static volatile IndexedCommitCommentEventStream instance = null;

	public static IndexedCommitCommentEventStream getInstance() {
		if (instance == null) {
			synchronized (IndexedCommitCommentEventStream.class) {
				// Double check
				if (instance == null) {
					instance = new IndexedCommitCommentEventStream();
				}
			}
		}
		return instance;
	}

	protected final String[] DEFAULT_INDEXED_PROPERTY_NAMES = { "userID",
			"commitID" };

	// private constructor
	private IndexedCommitCommentEventStream() {

		LOG.trace("** CommitCommentEvents()");

		setIndexedPropertyNames(DEFAULT_INDEXED_PROPERTY_NAMES);

	}

}
