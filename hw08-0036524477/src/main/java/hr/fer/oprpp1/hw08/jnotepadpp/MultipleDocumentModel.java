package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JComponent;

/**
 * An interface that contains all the methods that a class has to implement if
 * it wants to become a multiple document holder
 *
 * @author Marko Tunjić
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    /**
     * A method that returns the JTabbedPane which visually contains the documents
     *
     * @return the JTabbedPane which visually contains the documents
     */
    JComponent getVisualComponent();

    /**
     * A method that creates and returns a new {@link SingleDocumentModel} and adds
     * it to the {@link JTabbedPane}
     *
     * @return the new {@link SingleDocumentModel}
     */
    SingleDocumentModel createNewDocument();

    /**
     * A method that return the currently selected document or null if not selcted
     *
     * @return the currently selected document or null if not selcted
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * A method that adds and returns a new {@link SingleDocumentModel} that is
     * loaded form the disc and adds it to the {@link JTabbedPane}. Throws
     * {@link NullPointerException} if given path is null
     *
     * @param path the file from which the file should be loaded
     *
     * @return the new {@link SingleDocumentModel}
     *
     * @throws NullPointerException if null was given
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * A method that saves the given model onto the given path or to the old path if
     * given path is null. Throws {@link NullPointerException} if model is null
     *
     * @param model   the model to be saved
     * @param newPath the path on which the file will be saved or to the old path if
     *                given path is null
     *
     * @throws NullPointerException if model is null
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * A method that closes the given document.
     *
     * @param model the document to be closed
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * A method that adds a listener to the interested listeners. Throws
     * {@link NullPointerException} if null was given
     *
     * @param l the interested listener
     *
     * @throws NullPointerException if nullwas given
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * A method that adds removes listener from the interested listeners.
     *
     * @param l the listener that was interested
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * A method that returns the number of opened documents
     *
     * @return the number of opened documents
     */
    int getNumberOfDocuments();

    /**
     * A method that returns the document on the given index. Throws
     * {@link IndexOutOfBoundsException} if invalid index was given
     *
     * @param index the index of the searched document
     * @return the {@link SingleDocumentModel} on the given index
     * @throws IndexOutOfBoundsException if invalid index was given
     */
    SingleDocumentModel getDocument(int index);

    /**
     * A method that returns the document that has the given path or null if not
     * exists. Throws
     * {@link NullPointerException} if null was given
     *
     * @param path the path of the searched document
     * @return the {@link SingleDocumentModel} that contains the given path or null
     *         if not exists
     * @throws NullPointerException if null was given
     */
    SingleDocumentModel findForPath(Path path); // null, if no such model exists

    /**
     * A method that returns the index of the given document or -1 if not found
     *
     * @param doc the document for which the index is needed
     *
     * @return the idex of the document or -1 if not found
     */
    int getIndexOfDocument(SingleDocumentModel doc); // -1 if not present
}
