package photo_renamer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.*;

/**
 * A class used to create and delete tags, as well as manage the master-list of
 * tags, provided a collection of photos representing every photo that has been
 * modified.
 */
public class TagManager {

	private Map<String, Integer> tagsList;
	private static TagManager tagManagerInstance; // Singleton Design purposes
	private String saveFilePath;

	private TagManager(String filePath) {
		try{
			tagsList = (Map<String, Integer>) DataService.reloadData(filePath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		saveFilePath = filePath;
	}

	/**
	 * A boolean that returns success if Photo photo has a String tag.
	 * 
	 * @param photo
	 *            the photo being updated.
	 * @param tag
	 *            the tag being searched for.
	 * 
	 * @return success or null.
	 */
	public boolean tagPhoto(Photo photo, String tag) {
		boolean success = photo.addTag(tag);
		if (success) {
			makeTag(tag);
		}
		return success;
	}
	

	/**
	 * Returns success if a String tag is removed from Photo photo.
	 * 
	 * @param photo
	 *            The photo being updated.
	 * 
	 * @param tag
	 *            The tag being removed.
	 * 
	 * @return success or null.
	 */
	public boolean untagPhoto(Photo photo, String tag) {
		boolean success = photo.removeTag(tag);
		if (success) {
			removeTag(tag);
		}
		return success;
	}
	

	/**
	 * Creates a tag and adds it to the master-tag list, provided that it does
	 * not already exist.
	 * 
	 * @param newTag
	 *            The tag to be added into the master list
	 */
	public void makeTag(String newTag) {
		if (!tagsList.containsKey(newTag)) {
			tagsList.put(newTag, new Integer(1));
		}
		else {
			tagsList.put(newTag, tagsList.get(newTag) + 1);
		}
		saveTagsList();
	}

	/**
	 * Remove a tag from the master-tag list. Will do nothing if the tag doesn't
	 * exist. Precondition: tag must exist.
	 * 
	 * @param tag
	 *            the tag to be removed
	 */
	public void removeTag(String tag) {
		// CHECK LOGIC FOR INTEGER REFERNCING
		Integer numTags = tagsList.get(tag);
		tagsList.put(tag, numTags-1);
		if (numTags-1 < 1) {
			tagsList.remove(tag);
		}
		saveTagsList();
	}

	/**
	 * Returns an ArrayList of all existing tags of a photo.
	 * 
	 * @return String ArrayList of existing tags.
	 */
	public List<String> getExistingTags() {
		return (ArrayList<String>) tagsList.keySet();
	}

	/**
	 * Returns an instance object of the TagManager class.
	 * 
	 * @return an instance object of the TagManager class.
	 */
	// SINGLETON DESIGN PATTERN
	public static TagManager getInstance(String filePath) {
		tagManagerInstance = new TagManager(filePath);
		return tagManagerInstance;
	}

	public void saveTagsList() {
		try {
			DataService.saveData(tagsList, saveFilePath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
