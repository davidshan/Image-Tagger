package photo_renamer;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

//import javax.activation.MimetypesFileTypeMap;


// UTILISING the Facade Design Pattern
// We designed the program so that FileManagerImp acts as the FACADE for the client
// (in this case, the GUI). So FileManagerImp on top of having it's own unique methods,
// brings everything together nicely, so that the GUI can simply call any required methods
// from here (after initializing an instance of it of course, through a singleton design pattern).

public class FileManagerImp{
	
	private static FileManagerImp fileManagerInstance;
	private final List<String> EXTENSIONS = imageExtensions();
	private final String REVISIONS_FILEPATH = "revision_history.ser";
	private final String TAGS_FILEPATH = "tags_list.ser";
	private final String LOG_FILEPATH = "log.txt";
	
	private RevisionsManager revManager;
	private TagManager tagManager;
	private LoggerService logger;
	
	private Photo currentPhoto;
	
	private FileManagerImp() {
		// SINGLETON DESIGN
		// Access of one instance of TagManager and RevisionsManager
		
		revManager = RevisionsManager.getInstance(REVISIONS_FILEPATH);
		tagManager = TagManager.getInstance(TAGS_FILEPATH);
		logger = LoggerService.getInstance(LOG_FILEPATH);
	}
	
	//recursion to get all image files under directory 'file'
	// Precondition: file must be a directory
	public List<File> getImages(File file) {
		ArrayList<File> images = new ArrayList<File>();
		if (isImage(file)) {
			images.add(file);
		} else if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				images.addAll(getImages(f));
			}
		}
		return images;
	}
	
	public boolean tag(String tag) {
		String oldName = currentPhoto.getPhoto().getName();
		String filePath = currentPhoto.getPhoto().getAbsolutePath();
		boolean success = tagManager.tagPhoto(currentPhoto, tag);
		
		if (success) {
			revManager.modifyKeyEntry(filePath, currentPhoto.getPhoto().getAbsolutePath());
			logger.logTagAdded(oldName, tag);
		}
		
		return success;
		//add to revisions
		//log info
	}
	
	public boolean untag(String tag) {
		String oldName = currentPhoto.getPhoto().getName();
		String filePath = currentPhoto.getPhoto().getAbsolutePath();
		boolean success = tagManager.untagPhoto(currentPhoto, tag);
		
		if (success) {
			revManager.modifyKeyEntry(filePath, currentPhoto.getPhoto().getAbsolutePath());
			logger.logTagRemoved(oldName, tag);
		}
		
		return success;
		//add to revisions
		//log info
	}
	
	public void revertToSelection(String selection) {
		// hang on logic is screwed up, look at this again tommorow.
		// nvm fixed?
		
		String oldName = currentPhoto.getPhoto().getName();
		String filePath = currentPhoto.getPhoto().getAbsolutePath();
		List<String> oldTagsCopy = new ArrayList<String>(currentPhoto.getTags());
		boolean success = revManager.revertToPreviousName(currentPhoto, selection);
		
		if (success) {
			for (String oldTag: oldTagsCopy) {
				tagManager.removeTag(oldTag);
			}
			for (String newTag: currentPhoto.getTags()) {
				tagManager.makeTag(newTag);
			}
			revManager.modifyKeyEntry(filePath, currentPhoto.getPhoto().getAbsolutePath());
			logger.logRevertingBack(oldName, selection);
		}
	}
	
	public List<String> listPastVersions() {
		return new ArrayList<String>(currentPhoto.getPastVersions().keySet());
	}
	
	public void setCurrentPhoto(File image) {
		currentPhoto = revManager.loadPhoto(image);
	}
	
	public Photo getCurrentPhoto() {
		return currentPhoto;
	}
	
	public List<String> getPastVersions() {
		System.out.println(currentPhoto.getPhoto().getName());
		return new ArrayList<>(currentPhoto.getPastVersions().keySet());
	}
	
	public List<String> getAllTags() {
		return tagManager.getExistingTags();
	}
	
	// SINGLETON DESIGN
	public static FileManagerImp getInstance() {
		fileManagerInstance = new FileManagerImp();
		return fileManagerInstance;
	}
	
	private boolean isImage(File file) {
		String name = file.getName();
		if (name.contains(".")) {
			return EXTENSIONS.contains(name.substring(name.indexOf(".")));
		}
		return false;
		//return (new MimetypesFileTypeMap().getContentType(file).split("/")[0].equals("image"));
	}
	
	private List<String> imageExtensions() {
		// Some hard-coded nonsense
		String[] exts = {".png",".jpg",".bmp",".tiff"};
		return Arrays.asList(exts);
	}
}