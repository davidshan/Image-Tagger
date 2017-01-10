package photo_renamer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This Class consists of static methods that handles the saving and loading of
 * the revisions and the master tagList.
 *
 * The methods <code>saveData</code> and throws the IOException if the file does
 * not exist or the file stream is interrupted.
 * 
 * The method <code>reloadData</code> throws the IOException if the file does
 * not exist or the file stream is interrupted.
 */

public class DataService {

	/**
	 * Saves data in a HashMap of general type.
	 *
	 * @param data
	 *            a Map of data.
	 * @param filePath
	 *            the string filePath of the file being saved.
	 * @throws IOException
	 *             if the parent file does not exist.
	 */

	public static void saveData(Map<?, ?> data, String filePath) 
			throws IOException{
		OutputStream os = new FileOutputStream(filePath);
		OutputStream bos = new BufferedOutputStream(os);
		ObjectOutput output = new ObjectOutputStream(bos);
		
		output.writeObject(data);
		output.close();
	}

	/**
	 * Reloads and returns a HashMap serialized at String filePath.
	 *
	 * @param filePath
	 *            the string filePath of the file being reloaded.
	 * @return HashMap of generic type.
	 * @throws IOException
	 *             if the parent file does not exist.
	 */

	public static Map<?, ?> reloadData(String filePath) 
			throws ClassNotFoundException, IOException {
		Map<?,?> data = new HashMap<>();
		File dataRecord = new File(filePath);
		if (dataRecord.exists()) {
			InputStream file = new FileInputStream(filePath);
		    InputStream buffer = new BufferedInputStream(file);
		    ObjectInput input = new ObjectInputStream(buffer);
		        
		    data = (HashMap<?, ?>) input.readObject();
		    input.close();
		}
		else {
			FileOutputStream newFile = new FileOutputStream(filePath);
			saveData(data, filePath);
			newFile.close();
		}
		return data;
	}

}
