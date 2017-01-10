package photo_renamer;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Handler;
import java.util.logging.FileHandler;

/**
 * This Class consists of static and void methods dealing with the managing the
 * tag log and revisions log.
 */

public class LoggerService {
	private static LoggerService logServiceInstance;
	private final Logger LOGGER = Logger.getLogger("EventsLog");
	private final SimpleFormatter FORMATTER = new SimpleFormatter();
	private Handler handler;

	private final String OLD_PREFIX = "Old: ";
	private final String NEW_PREFIX = "New: ";
	private final String REV_PREFIX = "Reverted: ";

	/**
	 * Class constructor specifying the formatter of the log.
	 *
	 * @param logFilePath
	 *            the filePath being set.
	 */
	public LoggerService(String logFilePath) {
		try {
			handler = new FileHandler(logFilePath);
			handler.setFormatter(FORMATTER);
			LOGGER.addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the log entry of a tag being added to a photo.
	 *
	 * @param name
	 *            the name of the photo that is written into the log.
	 * @param tag
	 *            the tag added to the photo that is written into the log.
	 */
	public void logTagAdded(String name, String tag) {
		String[] nameAndExtension = name.split(".");
		String newName = nameAndExtension[0] + "@" + tag + nameAndExtension[1];

		LOGGER.config(OLD_PREFIX + name + " " + NEW_PREFIX + newName + ", added tag \"" + tag + "\"");
	}

	/**
	 * Adds the log entry of a tag being removed to a photo.
	 *
	 * @param name
	 *            the name of the photo that is written into the log.
	 * @param tag
	 *            the tag removed from the photo that is written into the log.
	 */
	public void logTagRemoved(String name, String tag) {
		String newName = name.replace(("@" + tag), "");

		LOGGER.config(OLD_PREFIX + name + " " + NEW_PREFIX + newName + ", removed tag \"" + tag + "\"");
	}

	/**
	 * Adds the log entry of a photo name being reverted back to a historical
	 * name.
	 *
	 * @param nameNow
	 *            the most current version of the photo name.
	 * @param namePrev
	 *            the previous version of the photo name that the photo has been
	 *            reverted back to.
	 */
	public void logRevertingBack(String nameNow, String prevName) {
		LOGGER.config(REV_PREFIX + nameNow + " to " + prevName);
	}

	public static LoggerService getInstance(String logFilePath) {
		logServiceInstance = new LoggerService(logFilePath);
		return logServiceInstance;
	}
	// created new tag for master list???? nahh
}
