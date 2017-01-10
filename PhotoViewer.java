package photo_renamer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

// The week 11 lab, good for GUi reference on how to build it

/**
 * The listener for the button to choose a directory. This is where most of the
 * work is done.
 *
 * 
 * Additional reading/documentation:
 * https://docs.oracle.com/javase/tutorial/uiswing/layout/border.html
 * https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
 * https://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html
 *
 * For additional features:
 * http://docs.oracle.com/javase/tutorial/uiswing/events/documentlistener.html
 * http://www.java2s.com/Code/Java/2D-Graphics-GUI/ImageViewer.htm
 * https://docs.oracle.com/javase/tutorial/uiswing/components/list.html
 */
public class PhotoViewer {
	/** The window the button is in. */
	private final JFrame jFrame;
	/** The panel the button is in. */
	private final JPanel buttonContainer;
	/** The button used to open the file. */
	private final JButton openFileButton;
	/** The button option to rename the file. */
	private final JButton renameFileButton;
	/** The area to use to display the nested directory contents. */
	private final JTextArea textArea;
	/** The directory being worked with. */
	private File directoryChosen;
	// TODO define text area

	/** An action listener for the window jFrame.  */
	private PhotoViewer() {
		this.jFrame = new JFrame();
		openFileButton = new JButton("Choose directory");
		openFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFileChooser();
			}
		});
		renameFileButton = new JButton("Rename files");
		renameFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String prefix = textArea.getText();
				renameFiles(directoryChosen, prefix);
			}
		});
		// TODO add a listener to renameFileButton that does the renaming logic
		renameFileButton.setEnabled(true);

		textArea = new JTextArea();
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			/**
			 * Handles the updated change of an event.
			 *
			 * @param e
			 *            the event object
			 */
			public void changedUpdate(DocumentEvent e) {

			}
			/**
			 * Handles the insertion of the update of event e.
			 *
			 * @param e
			 *            the event object
			 */
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}
			/**
			 * Handles the removal of an update of event e.
			 *
			 * @param e
			 *            the event object
			 */
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}
		});
		// TODO create a JTextArea field here for the text to add

		Container content = this.jFrame.getContentPane();

		// We create a new panel inside of our panel so that we can have
		// our buttons side by side, while also at the bottom of the main
		// layout.
		buttonContainer = new JPanel();
		buttonContainer.add(openFileButton, BorderLayout.LINE_START);
		buttonContainer.add(renameFileButton, BorderLayout.LINE_END);
		content.add(buttonContainer, BorderLayout.PAGE_END);
		content.add(textArea, BorderLayout.CENTER);
		// TODO content.add([your JTextArea], BorderLayout.[see diagram in
		// hand out])
	}
	
	private void showFileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(jFrame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			directoryChosen = chooser.getSelectedFile();
			// TODO keep track of the selected directory so we can rename the
			// files in it.
		}
	}

	private void createAndShowGui() {
		// The following three lines will be included in basically every GUI
		// you see. If you don't include EXIT_ON_CLOSE, your application won't
		// close properly!
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jFrame.pack();
		jFrame.setVisible(true);
	}

	private void renameFiles(File directory, String prefix) {
		for (File f : directory.listFiles()) {
			f.renameTo(new File(directory, prefix + f.getName()));
			// notice that File has a constructor which takes a directory and a
			// name,
			// so the destination for your rename can be: new File(directory,
			// "prefix_DSCN0218.jpg")
		}
	}

	// public static void main(String[] args) {
	// PhotoViewer v = new PhotoViewer();
	// v.createAndShowGui();
	// }
}