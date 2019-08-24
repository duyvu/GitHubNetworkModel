package graphs.relationalStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexedProjectMemberEventStream extends
		IndexedRelationalEventStream {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedProjectMemberEventStream.class.getName());

	private static volatile IndexedProjectMemberEventStream instance = null;

	public static IndexedProjectMemberEventStream getInstance() {
		if (instance == null) {
			synchronized (IndexedProjectMemberEventStream.class) {
				// Double check
				if (instance == null) {
					instance = new IndexedProjectMemberEventStream();
				}
			}
		}
		return instance;
	}

	protected final String[] DEFAULT_INDEXED_PROPERTY_NAMES = { "userID",
			"repoID" };

	// private constructor
	private IndexedProjectMemberEventStream() {

		LOG.trace("** ProjectMemberEvents()");

		setIndexedPropertyNames(DEFAULT_INDEXED_PROPERTY_NAMES);

	}

}
