package events;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventStreams {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventStreams.class.getName());

	public static String separateString = ",;\t\"";

	public static double ONE_DAY_IN_SECONDS = 24 * 60 * 60;

	public static double ONE_DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

	public static DateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static volatile EventStreams instance = null;

	public static EventStreams getInstance() {
		if (instance == null) {
			synchronized (EventStreams.class) {
				// Double check
				if (instance == null) {
					instance = new EventStreams();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<String, ArrayList<Event>> eventStreams = null;

	// private constructor
	private EventStreams() {

		LOG.trace("** EventStreamReaders()");

		eventStreams = new ConcurrentHashMap<>();
		
	}

	public int readEventStreams(String mappedEventStreamsFile)
			throws IOException {

		try (FileReader reader = new FileReader(
				new File(mappedEventStreamsFile))) {

			Properties props = new Properties();

			props.load(reader);

			for (Object _eventClass : props.keySet()) {

				String eventClassName = (String) _eventClass;

				String eventFile = props.getProperty(eventClassName);

				ArrayList<Event> eventStream = new ArrayList<>();

				try {
					Class<?> eventClass = Class.forName(eventClassName);
					Method dataMethod = eventClass.getMethod("readEvents",
							String.class, ArrayList.class);
					dataMethod.invoke(null, eventFile, eventStream);

					LOG.info("Finished reading the {} stream of {} events",
							eventClass.getSimpleName(), eventStream.size());
					eventStreams.put(eventClass.getSimpleName(), eventStream);
				} catch (ClassNotFoundException | NoSuchMethodException
						| SecurityException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					LOG.error(
							"Failed to invoke the event reading method on the event class {}",
							eventClassName);
					e.printStackTrace();
				}

			}

		}

		return eventStreams.size();
	}

	public ArrayList<Event> getEventStream(String eventStreamName) {
		return eventStreams.get(eventStreamName);
	}

	public PriorityQueue<Event> getEventQueue() {

		LOG.info("Start creating a priority queue of events...");

		PriorityQueue<Event> eventQueue = new PriorityQueue<Event>();

		for (String eventStreamName : eventStreams.keySet()) {

			ArrayList<Event> eventStream = eventStreams.get(eventStreamName);

			LOG.info("Stream {} has {} events", eventStreamName,
					eventStream.size());

			eventQueue.addAll(eventStream);
		}

		LOG.info("Finish creating a priority queue of events...");

		return eventQueue;

	}
}
