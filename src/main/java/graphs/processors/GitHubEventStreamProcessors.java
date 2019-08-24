/**
 * 
 */
package graphs.processors;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;

/**
 * @author duyv
 * 
 */
public class GitHubEventStreamProcessors {

	private static final Logger LOG = LoggerFactory
			.getLogger(GitHubEventStreamProcessors.class.getName());

	private static volatile GitHubEventStreamProcessors instance = null;

	public static GitHubEventStreamProcessors getInstance() {
		if (instance == null) {
			synchronized (GitHubEventStreamProcessors.class) {
				// Double check
				if (instance == null) {
					instance = new GitHubEventStreamProcessors();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<String, ArrayList<AtRiskEventStreamProcessor>> atRiskEventStreamProcessors = null;
	protected ConcurrentHashMap<String, ArrayList<RelationalEventStreamProcessor>> relationalEventStreamProcessors = null;

	// private constructor
	private GitHubEventStreamProcessors() {

		LOG.trace("** GitHubEventStreamProcessors()");

		atRiskEventStreamProcessors = new ConcurrentHashMap<>();
		relationalEventStreamProcessors = new ConcurrentHashMap<>();
	}

	/**
	 * A at risk event can contain at risk information of more than one node
	 * types; therefore, we allow to have multiple AtRiskEventStreamProcessor
	 * for an event class. Each AtRiskEventStreamProcessor will contain at risk
	 * information of one node set which can be looked up through a name such as
	 * "User", "Issue", "Commit" by accessing AtRiskStatuses A similar design is
	 * applied to relational event streams
	 */
	public int loadAtRiskEventStreamProcessors(
			String mappedAtRiskEventStreamProcessorsFile) throws IOException {

		try (FileReader reader = new FileReader(new File(
				mappedAtRiskEventStreamProcessorsFile))) {

			Properties props = new Properties();

			props.load(reader);

			for (Object _eventClass : props.keySet()) {

				String eventClassName = (String) _eventClass;

				String atRiskEventStreamProcessorNames = props
						.getProperty(eventClassName);
				try {
					StringTokenizer tokenizer = new StringTokenizer(
							atRiskEventStreamProcessorNames, ";");
					while (tokenizer.hasMoreTokens()) {
						String atRiskEventStreamProcessorName = tokenizer
								.nextToken().trim();
						Class<?> atRiskEventStreamProcessorClass = Class
								.forName(atRiskEventStreamProcessorName);
						Method getInstanceMethod = atRiskEventStreamProcessorClass
								.getMethod("getInstance");
						AtRiskEventStreamProcessor atRiskEventStreamProcessor = (AtRiskEventStreamProcessor) getInstanceMethod
								.invoke(null);

						addAtRiskEventStreamProcessor(eventClassName,
								atRiskEventStreamProcessor);
					}
				} catch (ClassNotFoundException | NoSuchMethodException
						| SecurityException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					LOG.error(
							"Failed to invoke the getInstance() method on {} for the event class {}",
							atRiskEventStreamProcessorNames, eventClassName);
					e.printStackTrace();
				}

			}

		}

		return atRiskEventStreamProcessors.size();
	}

	protected void addAtRiskEventStreamProcessor(String eventClassName,
			AtRiskEventStreamProcessor atRiskEventStreamProcessor) {

		LOG.info("Registering {} for the event stream {}",
				atRiskEventStreamProcessor.getClass().getName(), eventClassName);

		if (atRiskEventStreamProcessors.contains(eventClassName))
			atRiskEventStreamProcessors.get(eventClassName).add(
					atRiskEventStreamProcessor);
		else {
			ArrayList<AtRiskEventStreamProcessor> myAtRiskEventStreamProcessors = new ArrayList<>();
			myAtRiskEventStreamProcessors.add(atRiskEventStreamProcessor);
			atRiskEventStreamProcessors.put(eventClassName,
					myAtRiskEventStreamProcessors);
		}
	}

	public int loadRelationalEventStreamProcessors(
			String mappedRelationalEventStreamProcessorsFile)
			throws IOException {

		try (FileReader reader = new FileReader(new File(
				mappedRelationalEventStreamProcessorsFile))) {

			Properties props = new Properties();

			props.load(reader);

			for (Object _eventClass : props.keySet()) {

				String eventClassName = (String) _eventClass;

				String relationalEventStreamProcessorNames = props
						.getProperty(eventClassName);

				try {
					StringTokenizer tokenizer = new StringTokenizer(
							relationalEventStreamProcessorNames, ";");
					while (tokenizer.hasMoreTokens()) {
						String relationalEventStreamProcessorName = tokenizer
								.nextToken().trim();
						Class<?> relationalEventStreamProcessorClass = Class
								.forName(relationalEventStreamProcessorName);
						Method getInstanceMethod = relationalEventStreamProcessorClass
								.getMethod("getInstance");
						RelationalEventStreamProcessor relationalEventStreamProcessor = (RelationalEventStreamProcessor) getInstanceMethod
								.invoke(null);

						addRelationalEventStreamProcessor(eventClassName,
								relationalEventStreamProcessor);
					}

				} catch (ClassNotFoundException | NoSuchMethodException
						| SecurityException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					LOG.error(
							"Failed to invoke the getInstance() method on {} for the event class {}",
							relationalEventStreamProcessorNames, eventClassName);
					e.printStackTrace();
				}

			}

		}

		return relationalEventStreamProcessors.size();
	}

	protected void addRelationalEventStreamProcessor(String eventClassName,
			RelationalEventStreamProcessor relationalEventStreamProcessor) {

		LOG.info("Registering {} for the event stream {}",
				relationalEventStreamProcessor.getClass().getName(),
				eventClassName);

		if (relationalEventStreamProcessors.contains(eventClassName))
			relationalEventStreamProcessors.get(eventClassName).add(
					relationalEventStreamProcessor);
		else {
			ArrayList<RelationalEventStreamProcessor> myRelationalEventStreamProcessors = new ArrayList<>();
			myRelationalEventStreamProcessors
					.add(relationalEventStreamProcessor);
			relationalEventStreamProcessors.put(eventClassName,
					myRelationalEventStreamProcessors);
		}
	}

	public void processEvent(Event event) {

		String eventClassName = event.getClass().getName();

		if (atRiskEventStreamProcessors.get(eventClassName) == null
				&& relationalEventStreamProcessors.get(eventClassName) == null)
			LOG.warn("There is an UNKNOWN event type {}", eventClassName);
		else {

			/*
			 * Note that an event can be consumed by both at risk and relational
			 * processors
			 */

			if (atRiskEventStreamProcessors.get(eventClassName) != null)
				for (AtRiskEventStreamProcessor atRiskEventStreamProcessor : atRiskEventStreamProcessors
						.get(eventClassName))
					atRiskEventStreamProcessor.processAtRiskEvent(event);
			if (relationalEventStreamProcessors.get(eventClassName) != null)
				for (RelationalEventStreamProcessor relationalEventStreamProcessor : relationalEventStreamProcessors
						.get(eventClassName))
					relationalEventStreamProcessor
							.processRelationalEvent(event);
		}

	}

}
