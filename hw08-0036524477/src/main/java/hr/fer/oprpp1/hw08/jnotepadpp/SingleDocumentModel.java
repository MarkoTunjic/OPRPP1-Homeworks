package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * An interface that contains all the methods that a class has to implement if
 * it wants to become a document representation
 *
 * @author Marko Tunjić
 */
public interface SingleDocumentModel {

    /**
     * A method that returns the GUI component that contains the text
     *
     * @return the GUI component that contains the text
     */
    JTextArea getTextComponent();

    /**
     * A method that return the path to the file or null if new file
     *
     * @return the path to the file or null if new file
     */
    Path getFilePath();

    /**
     * A method that changes the files path and informs all interested listeners.
     * Throws {@link NullPointerException} if null was given
     *
     * @param path the path to which the current path should be changed
     *
     * @throws NullPointerException if null was given
     */
    void setFilePath(Path path);

    /**
     * A method that return true if ths file has been modified and false otherwise.
     *
     * @return true if modified and false otherwise
     */
    boolean isModified();

    /**
     * A method that sets the modified status to the given value.
     *
     * @param modified the status to which the modifed status should be changed
     */
    void setModified(boolean modified);

    /**
     * A method that adds a {@link SingleDocumentListener} to the collection of
     * interested listeners.
     * Throws null pointer exception if null was given
     *
     * @param l the interested listener.
     *
     * @throws NullPointerException if null was given.
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * A method that removes the given {@link SingleDocumentListener} from the
     * collection of interested
     *
     * @param l the interested listener.
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}
