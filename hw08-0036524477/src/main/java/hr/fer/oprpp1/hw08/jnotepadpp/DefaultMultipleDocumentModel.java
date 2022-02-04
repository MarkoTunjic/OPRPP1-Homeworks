package hr.fer.oprpp1.hw08.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

/**
 * A class that defines all the functionality for working with multiple
 * documents.
 *
 * @author Marko Tunjić
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
    /**
     * A constant that defines the name of the image that will be displayed when the
     * currently selected document is up to date with the document on the disk
     */
    private static final String SAVED_IMAGE_NAME = "green_diskette.png";

    /**
     * A constant that defines the name of the image that will be displayed when the
     * currently selected document is not up to date with the document on the disk
     */
    private static final String NOT_SAVED_IMAGE_NAME = "red_diskette.png";

    /**
     * A constant that defines the stirng that will be displayed as the title of a
     * new file
     */
    public static final String UNAMED = "(unnamed)";

    /** A constant that deifens the size of the diskette icons */
    private static final int ICON_SIZE = 12;

    /** An private attribute that contains all of the displayed documents */
    private List<SingleDocumentModel> documents;

    /**
     * An private attribute that contains all of the interested listeners od this
     * {@link JTabbedPane}
     */
    private List<MultipleDocumentListener> listeners;

    /**
     * An private attribute that contains the image that will be displayed when the
     * currently selected document is up to date with the document on the disk
     */
    private ImageIcon greenDiskette;

    /**
     * An private attribute that contains the image that will be displayed when the
     * currently selected document is not up to date with the document on the disk
     */
    private ImageIcon redDiskette;

    /**
     * An privateattribute that contains the index of the curerntly opened document
     */
    private int current;

    /**
     * A consturctor that create an instance of the
     * {@link DefaultMultipleDocumentModel} by initializing all attributes and
     * adding a change listener to the {@link JTabbedPane}
     */
    public DefaultMultipleDocumentModel() {
        // initialize all attributes
        current = -1;
        documents = new ArrayList<>();
        listeners = new ArrayList<>();
        greenDiskette = new ImageIcon(
                getIcon(SAVED_IMAGE_NAME).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
        redDiskette = new ImageIcon(
                getIcon(NOT_SAVED_IMAGE_NAME).getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));

        // register the listener to change the document when the tab changes
        this.addChangeListener(e -> {
            SingleDocumentModel previous = this.getCurrentDocument();
            this.current = this.getSelectedIndex();
            this.fireCurrentDocumentChanged(previous);
        });
    }

    /**
     * A method that returns an iteretor over the opened documents
     *
     * @return an iteretor over the opened documents
     */
    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

    /**
     * A method that returns the JTabbedPane aka this
     *
     * @return the JTabbedPane aka this
     */
    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    /**
     * A method that creates and returns a new {@link SingleDocumentModel} and adds
     * it to the {@link JTabbedPane}
     *
     * @return the new {@link SingleDocumentModel}
     */
    @Override
    public SingleDocumentModel createNewDocument() {
        // create new model
        SingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, "");
        newDocument.setModified(true);

        // register a listener to change the tab appreance
        newDocument.addSingleDocumentListener(new ChangeTab());

        // add the docuemnt to the collection
        documents.add(newDocument);

        // add the docuemnt to the JTabbedPane and inform all interested listeners
        this.addTab(UNAMED, redDiskette, new JScrollPane(newDocument.getTextComponent()), UNAMED);
        fireDocumentAdded(newDocument);
        this.setSelectedIndex(documents.size() - 1);
        setTabComponentAt(getSelectedIndex(), new TabComponent(greenDiskette, redDiskette, true, UNAMED, this));

        return newDocument;
    }

    /**
     * A method that return the currently selected document or null if not selcted
     *
     * @return the currently selected document or null if not selcted
     */
    @Override
    public SingleDocumentModel getCurrentDocument() {
        if (current < 0)
            return null;
        return documents.get(this.current);
    }

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
    @Override
    public SingleDocumentModel loadDocument(Path path) {
        // check if null
        if (path == null)
            throw new NullPointerException("Path can not be null");

        // check if already exists and return the existing
        if (documents.stream().anyMatch(document -> path.equals(document.getFilePath())))
            throw new IllegalArgumentException("Document with specified path already opened");

        // the octets of the file
        byte[] octets;
        // try reading file
        try {
            octets = Files.readAllBytes(path);
        }
        // if error occured inform the user
        catch (Exception ex) {
            JOptionPane.showOptionDialog(this, LocalizationProvider.getInstance().getString(Keys.readingError) + " "
                    + path.toFile().getAbsolutePath() + ".", LocalizationProvider.getInstance().getString(Keys.error),
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null,
                    new String[] { LocalizationProvider.getInstance().getString(Keys.ok) }, 0);
            return null;
        }
        // create text from bytes with UTF-8
        String tekst = new String(octets, StandardCharsets.UTF_8);

        // create new model
        SingleDocumentModel openedDocument = new DefaultSingleDocumentModel(path, tekst);

        // register a listener to change the tab appreance
        openedDocument.addSingleDocumentListener(new ChangeTab());

        // add the docuemnt to the collection
        documents.add(openedDocument);

        // add the docuemnt to the JTabbedPane and inform all interested listeners
        this.addTab(path.getFileName().toString(), greenDiskette, new JScrollPane(openedDocument.getTextComponent()),
                path.toAbsolutePath().toString());
        fireDocumentAdded(openedDocument);
        this.setSelectedIndex(documents.size() - 1);
        setTabComponentAt(getSelectedIndex(),
                new TabComponent(greenDiskette, redDiskette, false, path.getFileName().toString(), this));

        return openedDocument;
    }

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
    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        if (model == null)
            throw new NullPointerException("Model can not be null");

        // get real path on which to save the file
        boolean oldPath = false;
        if (newPath == null) {
            newPath = model.getFilePath();
            oldPath = true;
        }

        // get the byte data of the file
        byte[] data = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);

        // try writing
        try {
            Files.write(newPath, data);
        }
        // if something went wrong inform the user
        catch (IOException e1) {
            JOptionPane.showOptionDialog(this, LocalizationProvider.getInstance().getString(Keys.writingError) + " "
                    + newPath.toFile().getAbsolutePath()
                    + ".\n" + LocalizationProvider.getInstance().getString(Keys.caution),
                    LocalizationProvider.getInstance().getString(Keys.error),
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null,
                    new String[] { LocalizationProvider.getInstance().getString(Keys.ok) }, 0);
            return;
        }
        // if file path has been changed update the path
        if (!oldPath)
            model.setFilePath(newPath);

        // set up to date
        model.setModified(false);
    }

    /**
     * A method that closes the given document.
     *
     * @param model the document to be closed
     */
    @Override
    public void closeDocument(SingleDocumentModel model) {
        documents.remove(model);
        fireDocumentRemoved(model);
        fireCurrentDocumentChanged(model);
    }

    /**
     * A method that adds a listener to the interested listeners. Throws
     * {@link NullPointerException} if null was given
     *
     * @param l the interested listener
     *
     * @throws NullPointerException if nullwas given
     */
    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        if (l == null)
            throw new NullPointerException("Listener can not be null");
        listeners.add(l);
    }

    /**
     * A method that adds removes listener from the interested listeners.
     *
     * @param l the listener that was interested
     */
    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    /**
     * A method that returns the number of opened documents
     *
     * @return the number of opened documents
     */
    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    /**
     * A method that returns the document on the given index. Throws
     * {@link IndexOutOfBoundsException} if invalid index was given
     *
     * @param index the index of the searched document
     * @return the {@link SingleDocumentModel} on the given index
     * @throws IndexOutOfBoundsException if invalid index was given
     */
    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

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
    @Override
    public SingleDocumentModel findForPath(Path path) {
        // check if null
        if (path == null)
            throw new NullPointerException("Path can not be null");

        // search all documents
        for (var document : documents) {
            if (document.getFilePath().equals(path))
                return document;
        }
        return null;
    }

    /**
     * A method that returns the index of the given document or -1 if not found
     *
     * @param doc the document for which the index is needed
     *
     * @return the idex of the document or -1 if not found
     */
    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        return documents.indexOf(doc);
    }

    /**
     * A private method that fires all listeners that are interested on document
     * changed action.
     *
     * @param previous the document from which the state was changed to the current
     *                 document
     */
    private void fireCurrentDocumentChanged(SingleDocumentModel previous) {
        for (var listener : listeners)
            listener.currentDocumentChanged(previous, getCurrentDocument());
    }

    /**
     * A private method that fires all listeners that are interested on document
     * added action.
     *
     * @param added the document which was added
     */
    private void fireDocumentAdded(SingleDocumentModel added) {
        for (var listener : listeners)
            listener.documentAdded(added);
    }

    /**
     * A private method that fires all listeners that are interested on document
     * removed action.
     *
     * @param added the document which was removed
     */
    private void fireDocumentRemoved(SingleDocumentModel removed) {
        for (var listener : listeners)
            listener.documentRemoved(removed);
    }

    /**
     * A private method that reads a image with the given name from the resources
     * and return an {@link ImageIcon} object. Throws {@link NullPointerException}
     * if null was given
     *
     * @param name the name of the imae on disc
     *
     * @return the {@link ImageIcon} of the given image name
     */
    private ImageIcon getIcon(String name) {

        // ask the JVM to find and read the image with the given name
        byte[] bytes;
        try (InputStream is = this.getClass().getResourceAsStream("icons/" + name)) {
            if (is == null)
                throw new IllegalArgumentException("Could not read image");
            bytes = is.readAllBytes();
        }
        // if something went wrong
        catch (IOException e) {
            System.out.println("Could not read image");
            return null;
        }

        return new ImageIcon(bytes);
    }

    /**
     * A class that implements the {@link SingleDocumentListener} interface and
     * represents a listener that changes the tab appereance whenever the document
     * updates
     *
     * @author Marko Tunjić
     */
    private class ChangeTab implements SingleDocumentListener {

        /**
         * A method that does nothing becouse it is not its job but the TabComponents
         */
        @Override
        public void documentModifyStatusUpdated(SingleDocumentModel model) {
        }

        /**
         * A method that changes the tooltip text and fires the document changed
         * listeners
         *
         * @param model the model on which the path was updated
         */
        @Override
        public void documentFilePathUpdated(SingleDocumentModel model) {
            int index = DefaultMultipleDocumentModel.this.getIndexOfDocument(model);
            String toolTip = model.getFilePath() == null ? UNAMED : model.getFilePath().toAbsolutePath().toString();
            DefaultMultipleDocumentModel.this.setToolTipTextAt(index, toolTip);
            fireCurrentDocumentChanged(model);
        }

    }

}
