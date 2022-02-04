package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * An interface that contains all the methods in which object interested in
 * {@link MultipleDocumentModel} can perform changes
 *
 * @author Marko Tunjić
 */
public interface MultipleDocumentListener {

    /**
     * A method that will be called whenever the currently active document has been
     * changed. Throws {@link NullPointerException} if both parameters are null
     *
     * @param previousModel the model from which the selection has changed
     * @param currentModel  the model to which the selection has changed
     *
     * @throws NullPointerException if both parameters are null
     */
    void currentDocumentChanged(SingleDocumentModel previousModel,
            SingleDocumentModel currentModel);

    /**
     * A method that will be called whenever a document has been
     * added. Throws {@link NullPointerException} if null was given
     *
     * @param model the {@link SingleDocumentModel} that has been added
     *
     * @throws NullPointerException if null was given
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * A method that will be called whenever a document has been
     * removed. Throws {@link NullPointerException} if null was given
     *
     * @param model the {@link SingleDocumentModel} that has been removed
     *
     * @throws NullPointerException if null was given
     */
    void documentRemoved(SingleDocumentModel model);
}
