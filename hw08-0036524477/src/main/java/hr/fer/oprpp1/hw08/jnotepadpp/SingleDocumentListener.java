package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * An interface that contains all the methods in which object interested in
 * {@link SingleDocumentModel} can perform changes
 *
 * @author Marko Tunjić
 */
public interface SingleDocumentListener {
    /**
     * A method that will be called whenever a document has been
     * modifed. Throws {@link NullPointerException} if null was given
     *
     * @param model the {@link SingleDocumentModel} that has been changed
     *
     * @throws NullPointerException if null was given
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * A method that will be called whenever a path to document has been
     * modifed. Throws {@link NullPointerException} if null was given
     *
     * @param model the {@link SingleDocumentModel} that which path been changed
     *
     * @throws NullPointerException if null was given
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}
