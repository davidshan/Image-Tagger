package photo_renamer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class consists exclusively of void methods dealing with the addition and
 * removal of tags on a Photo file and the renaming of the Photo file.
 *
 */

public class Photo implements Serializable {

	private static final long serialVersionUID = 1L;
	private File image;
	private List<String> tagList;
	private Map<String, List<String>> pastVersions;
	private String originalName;

	/**
	 * Class constructor specifying the Photo file and all tags in the tag list
	 * of the Photo.
	 *
	 * @param image
	 *            the photo file being examined.
	 * @param tagList
	 *            the list of tags attached to the photo, in ArrayList format.
	 */

	// might not need this constructor
	public Photo(File image, List<String> tagList) {
		this(image);
		this.tagList = tagList;
	}

	/**
	 * Class constructor specifying the Photo file.
	 *
	 * @param image
	 *            the photo file being examined.
	 */
	public Photo(File image) {
		this.image = image;
		pastVersions = new HashMap<>();
		tagList = new ArrayList<>();
		// revisions.add(this);
		originalName = image.getName();
	}

	/**
	 * Adds a tag to the tag list of the Photo.
	 *
	 * @param tag
	 *            the tag being added to the Photo.
	 * @return Returns true if the tagging was successful, false if not
	 * @throws IOException
	 *             if the parent file does not exist.
	 */
	public boolean addTag(String tag) {
		// tagManager.makeTag(tag);
		return updateName(tag, "addTag");
	}

	/**
	 * Removes a tag to the tag list of the Photo.
	 *
	 * @param tag
	 *            the tag being removed from the Photo.
	 */
	public boolean removeTag(String tag) {
		/*
		 * if (tagManager.tagExists(fileManager.getImages(fileManager.
		 * getDirectoryOpen()), tag)){ tagManager.removeTag(tag); }
		 */
		return updateName(tag, "removeTag"); // Needs tagExist in the button
												// alongside
	}

	/**
	 * Renames the photo to incorporate the changes in tags, added or removed,
	 * from the Photo.
	 *
	 * @param image
	 *            the photo file being renamed.
	 * @param tag
	 *            the tag being adjusted in the Photo name.
	 * @param operation
	 *            the operation that determines what happens to the tag.
	 *            "addTag" modifies the name to add tag, "removeTag" does the
	 *            opposite
	 * @return Returns true if the photo renaming was successful, and false if
	 *         it was not
	 */
	public boolean updateName(String tag, String operation) {
		String oldName = image.getName();
		String orig = new String(image.getName());
		String newName = new String();
		newName = orig.substring(0, orig.indexOf("."));
		String extension = orig.substring(orig.indexOf('.'), orig.length());
		boolean success = false;

		if (operation == "addTag") {

			if (!newName.toLowerCase().contains(tag.toLowerCase())) {

				newName = newName + "@" + tag;
				orig = newName + extension;
				File parent = image.getParentFile();
				File newFile = new File(parent, orig);
				success = image.renameTo(newFile);
				if (success) {
					// check that we aren't already in there
					if (!pastVersions.keySet().contains(oldName)) {
						pastVersions.put(oldName, new ArrayList<>(tagList));
					}
					tagList.add(tag);
					image = newFile;
				}
				// we add to revisions only if it doesn't already exist
			}

		} else if (operation == "removeTag") {

			if (newName.toLowerCase().contains(tag.toLowerCase())) {

				newName = newName.replace(("@" + tag), "");
				orig = newName + extension;
				File parent = image.getParentFile();
				File newFile = new File(parent, orig);
				success = image.renameTo(newFile);

				if (success) {
					// check that we aren't already in there
					if (!pastVersions.keySet().contains(oldName)) {
						pastVersions.put(oldName, new ArrayList<>(tagList));
					}
					tagList.remove(tag);
					image = newFile;
				}
			}
		}
		return success;
		// System.out.println(revisions.size());
		// fileManager.updateRevision(String.valueOf(image.hashCode()),
		// revisions);

	}

	/**
	 * Gets the list of tags associated with this photo
	 * 
	 * @return A List<String> of tags
	 * 
	 */
	public List<String> getTags() {
		return tagList;
	}

	/**
	 * Gets the image file associated with the photo.
	 * 
	 * @return The image file associated with this photo
	 */
	public File getPhoto() {
		return image;
	}

	/**
	 * Sets the tagList on a photo.
	 * 
	 * @param tagList
	 *            tagList being set on the photo.
	 * 
	 */
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	/**
	 * Gets all past versions of a photo.
	 * 
	 * @return a HashMap of all past versions of a photo.
	 */
	public Map<String, List<String>> getPastVersions() {
		return pastVersions;
	}

	/**
	 * Sets File file as the photo.
	 * 
	 * @param file
	 *            file being set.
	 */
	public void setImageFile(File file) {
		image = file;
	}
	/*
	 * public void generateTags() throws IOException { String name =
	 * image.getName(); if (name.contains("@")) { name =
	 * name.substring(name.indexOf("@")+1, name.indexOf(".")); for (String tag:
	 * Arrays.asList(name.split("@"))) { addTag(tag); } } }
	 * 
	 * 
	 * public boolean containsRevision(String name) { for (Photo p : revisions)
	 * { if (p.getPhoto().getName() == name) { return true; } } return false; }
	 */
}
