package utils.exp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.commits.CommitEvent;
import events.pullRequests.PullRequestCommentEvent;
import events.pullRequests.PullRequestOpenedEvent;

public class BeanUtilsExperiment {

	private static final Logger LOG = LoggerFactory
			.getLogger(BeanUtilsExperiment.class.getName());

	public static void main(String[] args) {

		test1();
		test2();

		ArrayList<PullRequestCommentEvent> list = new ArrayList<>();
		testPassingSubclassList(list);

	}

	protected static void testPassingSubclassList(
			ArrayList<? extends Event> list) {
		LOG.info("Test passing a list of subclass objects");
	}

	protected static void test1() {
		LOG.info("Testing BeanUtils 1 -- START");

		Event event = new Event();
		String propertyName = "time";
		try {

			// Set the event time
			PropertyUtils.setSimpleProperty(event, propertyName, 123.000);
			// Get the event time
			double time = (Double) PropertyUtils.getSimpleProperty(event,
					propertyName);
			LOG.info("Got event time {}", time);

		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			LOG.error("Failed to set property {} !!!", propertyName);
			e.printStackTrace();
		}

		LOG.info("Testing BeanUtils 1 -- FINISH");
	}

	protected static void test2() {
		LOG.info("Testing BeanUtils 2 -- START");

		double time = 0;
		int commitID = 1;
		int authorID = 2;
		int committerID = authorID;
		int repoID = 4;
		int pullRequestID = 5;

		Event commitEvent = new CommitEvent(time, commitID, authorID,
				committerID, repoID);

		LOG.info(commitEvent.toString());

		Event pullRequestEvent = new PullRequestOpenedEvent(time,
				pullRequestID, authorID, repoID);
		LOG.info(pullRequestEvent.toString());

		try {

			LOG.info("authorID = {}", authorID);

			int myAuthor = (Integer) PropertyUtils.getSimpleProperty(
					commitEvent, "authorID");
			LOG.info("Got authorID = {} from commitEvent", myAuthor);
			assert (myAuthor == authorID);

			int myUser = (Integer) PropertyUtils.getSimpleProperty(
					pullRequestEvent, "userID");
			LOG.info("Got userID = {} from pullRequestEvent", myUser);
			assert (myUser == authorID);

		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			e.printStackTrace();
		}

		LOG.info("Testing BeanUtils 2 -- FINISH");
	}
}
