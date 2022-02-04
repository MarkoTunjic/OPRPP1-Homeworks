package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A class that represents a opened document
 *
 * @author Marko Tunjić
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
    /**
     * An private attribute that contains the path to the opened file or null if new
     * file
     */
    private Path filePath;

    /**
     * An private attribute that represents the GUI component that contains the text
     */
    private JTextArea textArea;

    /**
     * An private attribute that contains the refernece to all listeners interested
     * in changes to on this object
     */
    private List<SingleDocumentListener> listeners;

    /** An private attribute that says if this document has been modified or not */
    private boolean modified;

    /**
     * A constructor that creates the file from the given params. Throws
     * {@link NullPointerException} if textContent was null
     *
     * @param filePath    the path to the file or null i fnew file
     * @param textContent the text that this file contains
     *
     * @throws NullPointerException if textContetnt is null
     */
    public DefaultSingleDocumentModel(Path filePath, String textContent) {
        if (textContent == null)
            throw new NullPointerException("Text conent can not be empty in SingleDocumentModel");
        this.filePath = filePath;
        modified = false;
        textArea = new JTextArea();

        // unmap the action mneonivs for cut copy paste select all
        InputMap imap = textArea.getInputMap();
        imap.put(KeyStroke.getKeyStroke("control A"), "none");
        imap.put(KeyStroke.getKeyStroke("control C"), "none");
        imap.put(KeyStroke.getKeyStroke("control V"), "none");
        imap.put(KeyStroke.getKeyStroke("control X"), "none");

        listeners = new ArrayList<>();
        textArea.setText(textContent);

        // create a leistener that will listen for changes and change the modified
        // status and inform all listeners
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
                fireModifiyStatusUpdated();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
                fireModifiyStatusUpdated();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
                fireModifiyStatusUpdated();
            }
        });
    }

    /**
     * A method that returns the GUI component that contains the text
     *
     * @return the GUI component that contains the text
     */
    @Override
    public JTextArea getTextComponent() {
        return textArea;
    }

    /**
     * A method that return the path to the file or null if new file
     *
     * @return the path to the file or null if new file
     */
    @Override
    public Path getFilePath() {
        return filePath;
    }

    /**
     * A method that changes the files path and informs all interested listeners.
     * Throws {@link NullPointerException} if null was given
     *
     * @param path the path to which the current path should be changed
     *
     * @throws NullPointerException if null was given
     */
    @Override
    public void setFilePath(Path path) {
        // check if null
        if (path == null)
            throw new NullPointerException("File path can not be null");

        // change
        this.filePath = path;

        // inform all listeners
        fireFilePathUpdated();
    }

    /**
     * A method that return true if ths file has been modified and false otherwise.
     *
     * @return true if modified and false otherwise
     */
    @Override
    public boolean isModified() {
        return modified;
    }

    /**
     * A method that sets the modified status to the given value.
     *
     * @param modified the status to which the modifed status should be changed
     */
    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        fireModifiyStatusUpdated();
    }

    /**
     * A method that adds a {@link SingleDocumentListener} to the collection of
     * interested listeners.
     * Throws null pointer exception if null was given
     *
     * @param l the interested listener.
     *
     * @throws NullPointerException if null was given.
     */
    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        if (l == null)
            throw new NullPointerException("Listener can not be null");
        listeners.add(l);
    }

    /**
     * A method that removes the given {@link SingleDocumentListener} from the
     * collection of interested
     *
     * @param l the interested listener.
     */
    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    /**
     * A method that infomrms all interested listeners that the path has been
     * updated
     */
    private void fireFilePathUpdated() {
        for (var listener : listeners)
            listener.documentFilePathUpdated(this);
    }

    /**
     * A method that infomrms all interested listeners that the modified status has
     * been updated
     */
    private void fireModifiyStatusUpdated() {
        for (var listener : listeners)
            listener.documentModifyStatusUpdated(this);
    }

}
