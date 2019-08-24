package graphs.relationalStreams;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import graphs.processors.RelationalEventStreamProcessor;

public abstract class IndexedRelationalEventStream implements
		RelationalEventStreamProcessor {

	private static final Logger LOG = LoggerFactory
			.getLogger(IndexedRelationalEventStream.class.getName());

	/**
	 * Names of properties to be indexed
	 * 
	 * TO-DO: This list of property names is better provided through the
	 * configuration file
	 */
	protected String[] indexedPropertyNames = null;

	/**
	 * The map allows to retrieve events by values of a property propertyName ->
	 * propertyValue -> events Example: "repoID" -> 1234 -> commit comment
	 * events to the repository o				if (event instanceof CommitEvent)
					LOG.debug(
							"\t The after size of mapped list {}",
							propertyMap.containsKey(propertyValue) ? propertyMap
									.get(propertyValue).size() : 0);
f ID = 1234;
	 */
	protected ConcurrentHashMap<String, ConcurrentHashMap<Integer, ArrayList<Event>>> indexedEventStream = null;

	protected IndexedRelationalEventStream() {
	}

	public void setIndexedPropertyNames(String[] indexedPropertyNames) {

		LOG.info("Building the map for indexed streams");

		indexedEventStream = new ConcurrentHashMap<>();
		if (indexedPropertyNames != null) {
			this.indexedPropertyNames = indexedPropertyNames.clone();
			for (String propertyName : indexedPropertyNames) {
				LOG.info("Indexed by {}", propertyName);
				ConcurrentHashMap<Integer, ArrayList<Event>> propertyMap = new ConcurrentHashMap<>();
				indexedEventStream.put(propertyName, propertyMap);
			}
		}
	}

	/**
	 * Allow to retrieve the inde				if (event instanceof CommitEvent)
					LOG.debug(
							"\t The after size of mapped list {}",
							propertyMap.containsKey(propertyValue) ? propertyMap
									.get(propertyValue).size() : 0);
xed map of a property
	 * 
	 * @param propertyName
	 *            is the name of the property to retrieve
	 * @return the indexed map of the property
	 */
	public ConcurrentHashMap<Integer, ArrayList<Event>> getPropertyMap(
			String propertyName) {
		return indexedEventStream.get(propertyName);
	}

	@Override
	/**
	 * Allow to add a relational event to the indexed maps
	 * 
	 * @param event
	 *            is the event to be added
	 */
	public void processRelationalEvent(Event event) {

		for (String propertyName : indexedPropertyNames) {

			try {
				ConcurrentHashMap<Integer, ArrayList<Event>> propertyMap = indexedEventStream
						.get(propertyName);

				int propertyValue = (Integer) PropertyUtils.getSimpleProperty(
						event, propertyName);

				if (propertyMap.containsKey(propertyValue)) {
					propertyMap.get(propertyValue).add(event);
				} else {
					ArrayList<Event> indexedEvents = new ArrayList<>();
					indexedEvents.add(event);
					propertyMap.put(propertyValue, indexedEvents);
				}

			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error(">> addEvent()");
				LOG.error("Could not find the property = {} ", propertyName);
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}
}
