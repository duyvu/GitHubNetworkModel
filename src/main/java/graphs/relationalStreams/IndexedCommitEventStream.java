package graphs.relationalStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexedCommitEventStream extends IndexedRelationalEventStream {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedCommitEventStream.class.getName());

	private static volatile IndexedCommitEventStream instance = null;

	public static IndexedCommitEventStream getInstance() {
		if (instance == null) {
			synchronized (IndexedCommitEventStream.class) {
				// Double check
				if (instance == null) {
					instance = new IndexedCommitEventStream();
				}
			}
		}
		return instance;
	}

	protected final String[] DEFAULT_INDEXED_PROPERTY_NAMES = { "authorID",
			"committerID", "repoID" };

	// private constructor
	private IndexedCommitEventStream() {

		LOG.trace("** CommitEvents()");

		setIndexedPropertyNames(DEFAULT_INDEXED_PROPERTY_NAMES);

	}

}
