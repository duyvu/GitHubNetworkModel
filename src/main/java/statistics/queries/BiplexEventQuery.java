package statistics.queries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;

public class BiplexEventQuery {

	private static final Logger LOG = LoggerFactory
			.getLogger(BiplexEventQuery.class.getName());

	public static double getBiplexTwoPaths(
			int userID,
			int repoID,
			ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedReceiverEvents,
			String primarySenderNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> secondaryIndexedSenderEvents,
			String secondaryReceiverNodeProperty) {

		ArrayList<Event> primaryReceiverEvents = primaryIndexedReceiverEvents
				.get(repoID);
		if (primaryReceiverEvents == null)
			return 0;

		ArrayList<Event> secondarySenderEvents = secondaryIndexedSenderEvents
				.get(userID);
		if (secondarySenderEvents == null)
			return 0;

		TreeSet<Integer> nodeSet1 = new TreeSet<Integer>();
		for (Event primaryReceiverEvent : primaryReceiverEvents) {
			try {
				int primarySenderID = (Integer) PropertyUtils
						.getSimpleProperty(primaryReceiverEvent,
								primarySenderNodeProperty);
				if (userID != primarySenderID)
					nodeSet1.add(primarySenderID);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						primarySenderNodeProperty, primaryReceiverEvent);
				e.printStackTrace();
				System.exit(-1);
			}

		}

		TreeSet<Integer> nodeSet2 = new TreeSet<Integer>();
		for (Event secondarySenderEvent : secondarySenderEvents) {
			try {
				int secondaryReceiverID = (Integer) PropertyUtils
						.getSimpleProperty(secondarySenderEvent,
								secondaryReceiverNodeProperty);
				if (userID != secondaryReceiverID)
					nodeSet2.add(secondaryReceiverID);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						secondaryReceiverNodeProperty, secondarySenderEvent);
				e.printStackTrace();
				System.exit(-1);
			}
		}

		nodeSet1.retainAll(nodeSet2);
		return nodeSet1.size();
	}

	public static double getDisjointNodeSetThreePaths(
			int senderID,
			int receiverID,
			ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedSenderEvents,
			String primarySenderNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedReceiverEvents,
			String primaryReceiverNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> secondaryIndexedSenderEvents,
			String secondaryReceiverNodeProperty) {

		ArrayList<Event> fromSenderEvents = primaryIndexedSenderEvents
				.get(senderID);
		if (fromSenderEvents == null)
			return 0;

		ArrayList<Event> fromReceiverEvents = primaryIndexedReceiverEvents
				.get(receiverID);
		if (fromReceiverEvents == null)
			return 0;

		TreeSet<Integer> receiverSet1 = new TreeSet<Integer>();
		for (Event fromSenderEvent : fromSenderEvents)
			try {
				int onePathReceiverID = (Integer) PropertyUtils
						.getSimpleProperty(fromSenderEvent,
								primaryReceiverNodeProperty);
				receiverSet1.add(onePathReceiverID);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						primaryReceiverNodeProperty, fromSenderEvent);
				e.printStackTrace();
				System.exit(-1);
			}

		TreeSet<Integer> senderSet = new TreeSet<Integer>();
		for (Event fromReceiverEvent : fromReceiverEvents)
			try {
				int onePathSenderID = (Integer) PropertyUtils
						.getSimpleProperty(fromReceiverEvent,
								primarySenderNodeProperty);
				senderSet.add(onePathSenderID);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						primarySenderNodeProperty, fromReceiverEvent);
				e.printStackTrace();
				System.exit(-1);
			}

		double membershipThreePaths = 0.0;

		for (int onePathSenderID : senderSet) {

			ArrayList<Event> fromSenderEvents2 = primaryIndexedSenderEvents
					.get(onePathSenderID);

			TreeSet<Integer> receiverSet2 = new TreeSet<Integer>();
			for (Event fromSenderEvent2 : fromSenderEvents2)
				try {
					int twoPathReceiverID = (Integer) PropertyUtils
							.getSimpleProperty(fromSenderEvent2,
									primaryReceiverNodeProperty);
					if (receiverID != twoPathReceiverID)
						receiverSet2.add(twoPathReceiverID);

				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					LOG.error(
							"Failed to get the property {} from the event {}",
							primaryReceiverNodeProperty, fromSenderEvent2);
					e.printStackTrace();
					System.exit(-1);
				}

			receiverSet2.retainAll(receiverSet1);

			if (receiverSet2.size() > 0) {

				TreeSet<Integer> senderProjects = UniplexEventQuery
						.getUniquePropertyValues(
								secondaryIndexedSenderEvents.get(senderID),
								secondaryReceiverNodeProperty);
				TreeSet<Integer> onePathSenderProjects = UniplexEventQuery
						.getUniquePropertyValues(secondaryIndexedSenderEvents
								.get(onePathSenderID),
								secondaryReceiverNodeProperty);
				if (CollectionUtils.disjunction(senderProjects,
						onePathSenderProjects).size() > 0)
					membershipThreePaths += receiverSet2.size();

			}
		}

		return membershipThreePaths;
	}

	public static double getStrictDisjointNodeSetThreePaths(
			int senderID,
			int receiverID,
			ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedSenderEvents,
			String primarySenderNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> primaryIndexedReceiverEvents,
			String primaryReceiverNodeProperty,
			ConcurrentHashMap<Integer, ArrayList<Event>> secondaryIndexedSenderEvents,
			String secondaryReceiverNodeProperty) {

		ArrayList<Event> fromSenderEvents = primaryIndexedSenderEvents
				.get(senderID);
		if (fromSenderEvents == null)
			return 0;

		ArrayList<Event> fromReceiverEvents = primaryIndexedReceiverEvents
				.get(receiverID);
		if (fromReceiverEvents == null)
			return 0;

		TreeSet<Integer> receiverSet1 = new TreeSet<Integer>();
		for (Event fromSenderEvent : fromSenderEvents)
			try {
				int onePathReceiverID = (Integer) PropertyUtils
						.getSimpleProperty(fromSenderEvent,
								primaryReceiverNodeProperty);
				receiverSet1.add(onePathReceiverID);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						primaryReceiverNodeProperty, fromSenderEvent);
				e.printStackTrace();
				System.exit(-1);
			}

		TreeSet<Integer> senderSet = new TreeSet<Integer>();
		for (Event fromReceiverEvent : fromReceiverEvents)
			try {
				int onePathSenderID = (Integer) PropertyUtils
						.getSimpleProperty(fromReceiverEvent,
								primarySenderNodeProperty);
				senderSet.add(onePathSenderID);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.error("Failed to get the property {} from the event {}",
						primarySenderNodeProperty, fromReceiverEvent);
				e.printStackTrace();
				System.exit(-1);
			}

		double membershipThreePaths = 0.0;

		for (int onePathSenderID : senderSet) {

			ArrayList<Event> fromSenderEvents2 = primaryIndexedSenderEvents
					.get(onePathSenderID);

			TreeSet<Integer> receiverSet2 = new TreeSet<Integer>();
			for (Event fromSenderEvent2 : fromSenderEvents2)
				try {
					int twoPathReceiverID = (Integer) PropertyUtils
							.getSimpleProperty(fromSenderEvent2,
									primaryReceiverNodeProperty);
					if (receiverID != twoPathReceiverID)
						receiverSet2.add(twoPathReceiverID);

				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					LOG.error(
							"Failed to get the property {} from the event {}",
							primaryReceiverNodeProperty, fromSenderEvent2);
					e.printStackTrace();
					System.exit(-1);
				}

			receiverSet2.retainAll(receiverSet1);

			if (receiverSet2.size() > 0) {

				TreeSet<Integer> senderProjects = UniplexEventQuery
						.getUniquePropertyValues(
								secondaryIndexedSenderEvents.get(senderID),
								secondaryReceiverNodeProperty);
				
				TreeSet<Integer> onePathSenderProjects = UniplexEventQuery
						.getUniquePropertyValues(secondaryIndexedSenderEvents
								.get(onePathSenderID),
								secondaryReceiverNodeProperty);

				for (int onePathReceiverID : receiverSet2) {
					if (exclusivelySetContain(senderProjects, receiverID,
							onePathSenderProjects, onePathReceiverID)
							|| exclusivelySetContain(senderProjects,
									onePathReceiverID, onePathSenderProjects,
									receiverID))
						membershipThreePaths += 1.0;
				}

			}
		}

		return membershipThreePaths;
	}

	protected static boolean exclusivelySetContain(TreeSet<Integer> aProjects,
			int aID, TreeSet<Integer> bProjects, int bID) {
		return aProjects.contains(aID) && !aProjects.contains(bID)
				&& !bProjects.contains(aID) && bProjects.contains(bID);
	}

}