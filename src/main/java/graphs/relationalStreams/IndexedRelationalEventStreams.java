package graphs.relationalStreams;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexedRelationalEventStreams {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedRelationalEventStreams.class.getName());

	private static volatile IndexedRelationalEventStreams instance = null;

	public static IndexedRelationalEventStreams getInstance() {
		if (instance == null) {
			synchronized (IndexedRelationalEventStreams.class) {
				// Double check
				if (instance == null) {
					instance = new IndexedRelationalEventStreams();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<String, IndexedRelationalEventStream> indexedRelationalEventStreams = null;

	// private constructor
	private IndexedRelationalEventStreams() {

		LOG.trace("** IndexedRelationalEventStreams()");

		indexedRelationalEventStreams = new ConcurrentHashMap<>();
	}

	/**
	 * The file should be many lines
	 * 
	 * The left-hand side of each line is the name of relational event stream
	 * such as "FollowEvent", "CommitEvent", "PullRequestCommentEvent" and the
	 * right-hand side is the corresponding IndexRelationalEventStream class
	 * such as "IndexedFollowEventStream", "IndexedCommitEventStream",
	 * "IndexedPullRequestCommentEventStream"
	 * 
	 * Names on left-hand sides will be used to map network statistics to
	 * relational event streams
	 */
	public int loadIndexedRelationalEventStreams(
			String mappedIndexedRelationalEventStreamsFile) throws IOException {

		try (FileReader reader = new FileReader(new File(
				mappedIndexedRelationalEventStreamsFile))) {

			Properties props = new Properties();

			props.load(reader);

			for (Object _eventClass : props.keySet()) {

				String eventClassName = (String) _eventClass;

				String indexedEventStreamName = props
						.getProperty(eventClassName);

				try {
					Class<?> indexedEventStreamClass = Class
							.forName(indexedEventStreamName);
					Method getInstanceMethod = indexedEventStreamClass
							.getMethod("getInstance");
					IndexedRelationalEventStream indexedRelationalEventStream = (IndexedRelationalEventStream) getInstanceMethod
							.invoke(null);

					registerIndexedRelationalEventStream(eventClassName,
							indexedRelationalEventStream);
				} catch (ClassNotFoundException | NoSuchMethodException
						| SecurityException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					LOG.error(
							"Failed to invoke the getInstance() method on {} for the event class {}",
							indexedEventStreamName, eventClassName);
					e.printStackTrace();
				}

			}

		}

		return indexedRelationalEventStreams.size();
	}

	public void registerIndexedRelationalEventStream(String eventStreamName,
			IndexedRelationalEventStream indexedRelationalEventStream) {

		LOG.info("Registering {} for the event stream {}",
				indexedRelationalEventStream.getClass().getName(),
				eventStreamName);

		indexedRelationalEventStreams.put(eventStreamName,
				indexedRelationalEventStream);
	}

	public IndexedRelationalEventStream getIndexedRelationalEventStream(
			String eventStreamName) {
		return indexedRelationalEventStreams.get(eventStreamName);
	}

}
