package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
 * Creates and shows the jFrame, which displays the contents of a
 * directory.
 */
public class PhotoRenamer {
	/** The window the button is in. */
	private final JFrame jframe;
	/** The button used to open the file. */
	private final JPanel buttonContainer;
	/** The button used to choose the directory. */
	private final JButton directoryButton;
	/** The button used to choose the file. */
	private final JButton openFileButton;
	/** The menu for tag adding. */
	private JComboBox addTagMenu;
	/** The menu for tag removing.*/
	private JComboBox removeTagMenu;
	/** The implementation of the fileManager. */
	private JComboBox revisionsMenu;
	private JComboBox imageChooserMenu;
	private JLabel imageOfFile;
	private final FileManagerImp fileManager;
	/** The chosen directory. */
	private File directoryChosen;
	private File fileChosen;

	private PhotoRenamer() {
		fileManager = FileManagerImp.getInstance();
		this.jframe = new JFrame();
		directoryButton = new JButton("Choose directory");
		directoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileChooser();
			}
		});
		
		openFileButton = new JButton("Open Selected File");
		openFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		// Dropdown Menus
		// initially an empty dropdown menu of files in a directory
		imageChooserMenu = new JComboBox();
		imageChooserMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox thisMenu = (JComboBox)e.getSource();
				fileChosen = (File)thisMenu.getSelectedItem();
				fileManager.setCurrentPhoto(fileChosen);
				imageOfFile.setIcon(new ImageIcon(fileChosen.getAbsolutePath()));
				updateTagMenus();
				thisMenu.setSelectedItem(fileChosen);
			}
		});
		
		addTagMenu = new JComboBox(fileManager.getAllTags().toArray(new String[0]));
		addTagMenu.setEditable(true);
		addTagMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox thisMenu = (JComboBox)e.getSource();
				String tagToBeAdded = (String)thisMenu.getSelectedItem();
				if (fileManager.tag(tagToBeAdded)) {
					updateTagMenus();
					thisMenu.setSelectedItem(tagToBeAdded);
				}
			}
		});
		addTagMenu.setEnabled(false);
		
		removeTagMenu = new JComboBox(fileManager.getAllTags().toArray(new String[0]));
		removeTagMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox thisMenu = (JComboBox)e.getSource();
				String tagToBeRemoved = (String)thisMenu.getSelectedItem();
				if (fileManager.untag(tagToBeRemoved)) {
					updateTagMenus();
					//thisMenu.setSelectedItem(tagToBeRemoved);
				}
			}
		});
		removeTagMenu.setEnabled(false);
		
		revisionsMenu = new JComboBox();
		revisionsMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox thisMenu = (JComboBox)e.getSource();
				String selectedRev = (String) thisMenu.getSelectedItem();
				fileManager.revertToSelection(selectedRev);
				updateTagMenus();
				thisMenu.setSelectedItem(selectedRev);
			}
		});
		revisionsMenu.setEnabled(false);
		
		imageOfFile = new JLabel();
		
		Container content = this.jframe.getContentPane();

		// We create a new panel inside of our panel so that we can have
		// our buttons side by side, while also at the bottom of the main
		// layout.
		buttonContainer = new JPanel();
		buttonContainer.add(directoryButton, BorderLayout.LINE_START);
		buttonContainer.add(addTagMenu, BorderLayout.EAST);
		buttonContainer.add(removeTagMenu, BorderLayout.WEST);
		buttonContainer.add(revisionsMenu, BorderLayout.SOUTH);
		buttonContainer.add(imageOfFile, BorderLayout.CENTER);
		content.add(buttonContainer, BorderLayout.PAGE_END);
		content.add(imageChooserMenu, BorderLayout.PAGE_START);
	}
/**
 * Choose directory button
 * dropdown menu for list of files of that directory
 * choose file button
 * center image
 * add tag button
 * dropdown button of all available tags
 * vice versa remove tag
 * revert back to button
 * and drop down button of all possible dropdowns
 * 
 */
	private void showFileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(jframe);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			directoryChosen = chooser.getSelectedFile();
			List<File> images = fileManager.getImages(directoryChosen);
			imageChooserMenu.setModel(new DefaultComboBoxModel(images.toArray()));
			enableAllMenus();
		}
		
/*		for (File image: images) {
			System.out.println(image.getAbsolutePath());
		}*/
/*		fileManager.setCurrentPhoto(images.get(0));
		
		for (String s: fileManager.getPastVersions()) {
			System.out.println("Revision: " + s);
		}
		
		System.out.println(fileManager.getCurrentPhoto().getPastVersions().values());
		fileManager.tag("First");
		fileManager.tag("Second");
		fileManager.untag("First");
		fileManager.untag("Second");
		
		fileManager.revertToSelection(fileManager.getPastVersions().get(3));
		*/
		
	}
	
	private void updateTagMenus() {
		addTagMenu.setModel(new DefaultComboBoxModel(fileManager.getAllTags().toArray()));
		removeTagMenu.setModel(new DefaultComboBoxModel
				(fileManager.getCurrentPhoto().getTags().toArray()));
		revisionsMenu.setModel(new DefaultComboBoxModel
				(fileManager.getPastVersions().toArray()));
		imageChooserMenu.setModel(new DefaultComboBoxModel
				(fileManager.getImages(directoryChosen).toArray()));
		jframe.pack();
	}
	
	private void enableAllMenus() {
		addTagMenu.setEnabled(true);
		removeTagMenu.setEnabled(true);
		revisionsMenu.setEnabled(true);
		imageChooserMenu.setEnabled(true);
	}
	
	private void createAndShowGui() {
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setVisible(true);
	}
	
	public static void main(String[] args) {
		PhotoRenamer g = new PhotoRenamer();
		g.createAndShowGui();
	}
}
