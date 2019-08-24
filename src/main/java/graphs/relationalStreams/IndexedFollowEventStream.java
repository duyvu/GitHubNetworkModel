package graphs.relationalStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexedFollowEventStream extends IndexedRelationalEventStream {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedFollowEventStream.class.getName());

	private static volatile IndexedFollowEventStream instance = null;

	public static IndexedFollowEventStream getInstance() {
		if (instance == null) {
			synchronized (IndexedFollowEventStream.class) {
				// Double check
				if (instance == null) {
					instance = new IndexedFollowEventStream();
				}
			}
		}
		return instance;
	}

	protected final String[] DEFAULT_INDEXED_PROPERTY_NAMES = { "senderID",
			"receiverID" };

	// private constructor
	private IndexedFollowEventStream() {

		LOG.trace("** FollowEvents()");

		setIndexedPropertyNames(DEFAULT_INDEXED_PROPERTY_NAMES);

	}

}
