package photo_renamer;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RevisionsManager {
	private String saveFilePath;
	private Map<String, Photo> revisions;
	private static RevisionsManager revManagerInstance; // Singleton Design Patterns
	
	private RevisionsManager(String filePath) {
		//revisions = new HashMap<>();
		// Load in the revisions
		try{
			revisions = (Map<String, Photo>) DataService.reloadData(filePath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		saveFilePath = filePath;
	}
	
	// Singleton Design Pattern
	public static RevisionsManager getInstance(String filePath) {
		revManagerInstance = new RevisionsManager(filePath);
		return revManagerInstance;
	}
	
	// load photo: create a new photo if a record of it doesn't exist
	// otherwise return the photo stored in the record that is associated with that file
	public Photo loadPhoto(File image) {
		String imagePath = image.getAbsolutePath();
		if (revisions.containsKey(imagePath)) {
			return revisions.get(imagePath);
		}
		// runs in the event that there is no such photo currently existing
		Photo newPhoto = new Photo(image);
		revisions.put(imagePath, newPhoto);
		saveRevisionsRecord();
		return newPhoto;
	}
	
	// unfortunately, this does not account for updating the master tag list,
	// WHICH is handled in filemanager
	public boolean revertToPreviousName(Photo photo, String name) {
		File imageFile = photo.getPhoto();
		File parentFile = imageFile.getParentFile();
		File revertedFile = new File(parentFile, name);
		List<String> prevTags = new ArrayList<>(photo.getTags());
		String oldName = imageFile.getName();
		
		boolean success = imageFile.renameTo(revertedFile);
		if (success) {
			photo.getPastVersions().put(oldName, prevTags);
			photo.setTagList(new ArrayList<String>(photo.getPastVersions().get(name)));
			photo.setImageFile(revertedFile);
		}
		saveRevisionsRecord();
		
		return success;
	}
	
	// purpose: when you modify the name of the file, 
	// we want to save it to the same place inthe hash map
	// we need to remove the old key, and insert the new key
	public void modifyKeyEntry(String key, String newKey) {
		Photo photoAtKey = revisions.get(key);
		revisions.remove(key);
		revisions.put(newKey, photoAtKey);
		
		saveRevisionsRecord();
	}
	
	private void saveRevisionsRecord() {
		try {
			DataService.saveData(revisions, saveFilePath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
