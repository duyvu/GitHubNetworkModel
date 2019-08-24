/**
 * 
 */
package graphs.atRiskStreams;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author duyv
 * 
 */
public class AtRiskStatuses {

	private static final Logger LOG = LoggerFactory
			.getLogger(AtRiskStatuses.class.getName());

	private static volatile AtRiskStatuses instance = null;

	public static AtRiskStatuses getInstance() {
		if (instance == null) {
			synchronized (AtRiskStatuses.class) {
				// Double check
				if (instance == null) {
					instance = new AtRiskStatuses();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<String, AtRiskStatus> atRiskStatuses = null;

	// private constructor
	private AtRiskStatuses() {

		LOG.trace("** AtRiskStatuses()");

		atRiskStatuses = new ConcurrentHashMap<>();
	}

	/**
	 * The file should be many lines
	 * 
	 * The left-hand side of each line is the name of at risk set such as
	 * "User", "Commit", "PullRequest" and the right-hand side is the
	 * corresponding AtRiskStatus class such as "UserAtRiskStatus",
	 * "CommitAtRiskStatus", "PullRequestAtRiskStatus"
	 * 
	 * Names on left-hand sides will be used to map samplers to at risk sets
	 */
	public int loadAtRiskStatuses(String mappedAtRiskStatusesFile)
			throws IOException {

		try (FileReader reader = new FileReader(new File(
				mappedAtRiskStatusesFile))) {

			Properties props = new Properties();

			props.load(reader);

			for (Object _eventClass : props.keySet()) {

				String eventClassName = (String) _eventClass;

				String atRiskStatusName = props.getProperty(eventClassName);

				try {
					Class<?> atRiskStatusClass = Class
							.forName(atRiskStatusName);
					Method getInstanceMethod = atRiskStatusClass
							.getMethod("getInstance");
					AtRiskStatus atRiskStatus = (AtRiskStatus) getInstanceMethod
							.invoke(null);

					registerAtRiskStatus(eventClassName, atRiskStatus);
				} catch (ClassNotFoundException | NoSuchMethodException
						| SecurityException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					LOG.error(
							"Failed to invoke the getInstance() method on {} for the event class {}",
							atRiskStatusName, eventClassName);
					e.printStackTrace();
				}

			}

		}

		return atRiskStatuses.size();
	}

	public void registerAtRiskStatus(String atRiskStatusName,
			AtRiskStatus atRiskStatus) {

		LOG.info("Registering {} for the event stream {}", atRiskStatus
				.getClass().getName(), atRiskStatusName);

		atRiskStatuses.put(atRiskStatusName, atRiskStatus);
	}

	public AtRiskStatus getAtRiskStatus(String atRiskStatusName) {
		return atRiskStatuses.get(atRiskStatusName);
	}

}
