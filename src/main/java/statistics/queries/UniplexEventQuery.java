package statistics.queries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;

public class UniplexEventQuery {

	private static final Logger LOG = LoggerFactory
			.getLogger(UniplexEventQuery.class.getName());

	public static double getTimeWeightedCount(ArrayList<Event> events,
			double currentTime, double exponentialWeight) {
		double timeWeightedCount = 0.0;

		if (events != null)
			for (Event event : events) {
				double gapTime = Math.max(1.0, currentTime - event.getTime());
				timeWeightedCount += Math.pow(gapTime, exponentialWeight);
			}

		return timeWeightedCount;
	}

	public static ArrayList<Event> filterEventsByProperty(
			ArrayList<Event> events, String propertyName,
			int selectedPropertyValue) {

		ArrayList<Event> filteredEvents = new ArrayList<>();

		for (Event event : events)
			try {

				int propertyValue = (Integer) PropertyUtils.getSimpleProperty(
						event, propertyName);

				if (propertyValue == selectedPropertyValue)
					filteredEvents.add(event);

			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						propertyName, event);
				e.printStackTrace();
				System.exit(-1);
			}

		return filteredEvents;

	}

	public static Event getRecentEventByProperty(ArrayList<Event> events,
			String propertyName, int selectedPropertyValue) {

		for (int i = events.size() - 1; i >= 0; i--) {

			Event event = events.get(i);

			try {

				int propertyValue = (Integer) PropertyUtils.getSimpleProperty(
						event, propertyName);

				if (propertyValue == selectedPropertyValue)
					return event;

			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						propertyName, event);
				e.printStackTrace();
				System.exit(-1);
			}

		}

		return null;

	}

	public static TreeSet<Integer> getUniquePropertyValues(
			ArrayList<Event> events, String propertyName) {

		TreeSet<Integer> uniquePropertyValues = new TreeSet<>();

		if (events != null)
			for (Event event : events)
				try {

					int propertyValue = (Integer) PropertyUtils
							.getSimpleProperty(event, propertyName);

					uniquePropertyValues.add(propertyValue);

				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					LOG.error(
							"Failed to get the property {} from the event {}",
							propertyName, event);
					e.printStackTrace();
					System.exit(-1);
				}

		return uniquePropertyValues;

	}

	public static double getSharedPaths(int senderID, int receiverID,
			ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents,
			String sharedNodeProperty) {

		ArrayList<Event> fromSenderEvents = indexedSenderEvents.get(senderID);
		if (fromSenderEvents == null)
			return 0;

		ArrayList<Event> fromReceiverEvents = indexedSenderEvents
				.get(receiverID);
		if (fromReceiverEvents == null)
			return 0;

		TreeSet<Integer> nodeSet1 = getUniquePropertyValues(fromSenderEvents,
				sharedNodeProperty);

		TreeSet<Integer> nodeSet2 = getUniquePropertyValues(fromReceiverEvents,
				sharedNodeProperty);

		nodeSet1.retainAll(nodeSet2);

		return nodeSet1.size();

	}

	public static double getTwoPaths(int senderID, String senderNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents,
			String receiverNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents) {

		ArrayList<Event> fromSenderEvents = indexedSenderEvents.get(senderID);

		if (fromSenderEvents == null) {
			return 0.0;
		} else {
			// Reset the count
			double twoPaths = 0.0;

			// One step from the sender to its unique receivers
			TreeSet<Integer> receiverSet = new TreeSet<Integer>();
			for (Event fromSenderEvent : fromSenderEvents) {
				try {
					int onePathReceiverID = (Integer) PropertyUtils
							.getSimpleProperty(fromSenderEvent,
									receiverNodeProperty);
					receiverSet.add(onePathReceiverID);
				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					LOG.error(
							"Failed to get the property {} from the event {}",
							receiverNodeProperty, fromSenderEvent);
					e.printStackTrace();
					System.exit(-1);
				}
			}

			// From each unique receiver, move one step to unique senders but
			// exclude the pivoted sender
			for (int receiverID : receiverSet) {

				ArrayList<Event> fromReceiverEvents = indexedReceiverEvents
						.get(receiverID);

				TreeSet<Integer> senderSet = new TreeSet<Integer>();
				for (Event fromReceiverEvent : fromReceiverEvents) {
					try {
						int twoPathSenderID = (Integer) PropertyUtils
								.getSimpleProperty(fromReceiverEvent,
										senderNodeProperty);
						// exclude the pivoted sender
						if (senderID != twoPathSenderID)
							senderSet.add(twoPathSenderID);
					} catch (IllegalAccessException | InvocationTargetException
							| NoSuchMethodException e) {
						LOG.error(
								"Failed to get the property {} from the event {}",
								senderNodeProperty, fromReceiverEvent);
						e.printStackTrace();
						System.exit(-1);
					}
				}

				twoPaths += senderSet.size();
			}

			return twoPaths;
		}

	}

	public static double getTwoPaths(int senderID, int receiverID,
			String senderNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents,
			String receiverNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents) {

		ArrayList<Event> fromSenderEvents = indexedSenderEvents.get(senderID);
		if (fromSenderEvents == null)
			return 0;

		ArrayList<Event> fromReceiverEvents = indexedReceiverEvents
				.get(receiverID);
		if (fromReceiverEvents == null)
			return 0;

		TreeSet<Integer> nodeSet1 = getUniquePropertyValues(fromSenderEvents,
				receiverNodeProperty);

		TreeSet<Integer> nodeSet2 = getUniquePropertyValues(fromReceiverEvents,
				senderNodeProperty);

		nodeSet1.retainAll(nodeSet2);

		return nodeSet1.size();
	}

	public static double getThreePaths(int senderID, int receiverID,
			String senderNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents,
			String receiverNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents) {

		ArrayList<Event> fromSenderEvents = indexedSenderEvents.get(senderID);
		if (fromSenderEvents == null)
			return 0;

		ArrayList<Event> fromReceiverEvents = indexedReceiverEvents
				.get(receiverID);
		if (fromReceiverEvents == null)
			return 0;

		TreeSet<Integer> receiverSet1 = new TreeSet<Integer>();
		for (Event fromSenderEvent : fromSenderEvents)
			try {
				int onePathReceiverID = (Integer) PropertyUtils
						.getSimpleProperty(fromSenderEvent,
								receiverNodeProperty);
				receiverSet1.add(onePathReceiverID);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						receiverNodeProperty, fromSenderEvent);
				e.printStackTrace();
				System.exit(-1);
			}

		TreeSet<Integer> senderSet = new TreeSet<Integer>();
		for (Event fromReceiverEvent : fromReceiverEvents)
			try {
				int onePathSenderID = (Integer) PropertyUtils
						.getSimpleProperty(fromReceiverEvent,
								senderNodeProperty);
				senderSet.add(onePathSenderID);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						senderNodeProperty, fromReceiverEvent);
				e.printStackTrace();
				System.exit(-1);
			}

		double threePaths = 0.0;

		for (int onePathSenderID : senderSet) {

			ArrayList<Event> fromSenderEvents2 = indexedSenderEvents
					.get(onePathSenderID);

			TreeSet<Integer> receiverSet2 = new TreeSet<Integer>();
			for (Event fromSenderEvent2 : fromSenderEvents2)
				try {
					int twoPathReceiverID = (Integer) PropertyUtils
							.getSimpleProperty(fromSenderEvent2,
									receiverNodeProperty);
					if (receiverID != twoPathReceiverID)
						receiverSet2.add(twoPathReceiverID);

				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					LOG.error(
							"Failed to get the property {} from the event {}",
							receiverNodeProperty, fromSenderEvent2);
					e.printStackTrace();
					System.exit(-1);
				}

			receiverSet2.retainAll(receiverSet1);

			threePaths += receiverSet2.size();
		}

		return threePaths;

	}
}
