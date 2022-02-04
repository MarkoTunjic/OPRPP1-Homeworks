package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

/**
 * A class that represents a tab component design in the {@link JTabbedPane}
 *
 * @author Marko Tunjić
 */
public class TabComponent extends JPanel {
    /** An private attribute that contains the green icon */
    private ImageIcon greenDiskette;

    /** An private attribute that contains the red green icon */
    private ImageIcon redDiskette;

    /**
     * An private attribute that contains the reference to the
     * {@link MultipleDocumentModel} that contains this tab
     */
    private MultipleDocumentModel pane;

    /** An private attribute that indicates if the current file has been changed */
    private boolean modified;

    /** An private attribute that represents the current active icon of the tab */
    private JLabel icon;

    /** An private attribute that represents the current active title of the tab */
    private JLabel title;

    /** An private attribute that represents the x button on the tab */
    private JButton button;

    /**
     * An constructot that creates a {@link TabComponent} from the given params.
     * Throws {@link NullPointerException} if null was given
     *
     * @param greenDiskette the green icon
     * @param redDiskette   the red icon
     * @param modified      indicates if the file has modified or not
     * @param title         the title of the tab
     * @param pane          the pane to which this tab belongs
     *
     * @throws NullPointerException if null was give
     */
    public TabComponent(ImageIcon greenDiskette, ImageIcon redDiskette, boolean modified, String title,
            MultipleDocumentModel pane) {
        // check if null
        if (greenDiskette == null || redDiskette == null || title == null || pane == null)
            throw new NullPointerException("Cannot create TabComponent from null values");

        // intitialize attributes
        this.greenDiskette = greenDiskette;
        this.redDiskette = redDiskette;
        this.modified = modified;
        this.pane = pane;

        // add a listener that will repaint the tab component when the file has been
        // changed
        pane.getCurrentDocument().addSingleDocumentListener(new Repaint());

        // intiial values
        icon = new JLabel(modified ? redDiskette : greenDiskette);
        this.title = new JLabel(title);
        button = new TabButton();

        // add the elements to the pane
        add(icon);
        add(this.title);
        add(button);
    }

    /**
     * A private class that represents the "x" button on the tab. And is a listener
     * for itself
     *
     * @author Marko Tunjić
     */
    private class TabButton extends JButton implements ActionListener {

        /**
         * A constructor that creates a {@link TabButton} by setting the preffered size
         * and adding a action listener that changes the apperance
         */
        public TabButton() {
            setPreferredSize(new Dimension(17, 17));
            addActionListener(this);
        }

        /**
         * A method that paints the icon, title and button on the tab
         *
         * @param g the graphics object that is used for drawing
         */
        @Override
        public void paintComponent(Graphics g) {
            // call super method
            super.paintComponent(g);

            // swing gurantees that the g object will be a g2d object so we can safely cast
            Graphics2D g2 = (Graphics2D) g.create();

            // add some pretty stuff
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }

        /**
         * A method that is calles whenever the button "x" is pressed.
         *
         * @param e the event that cuased this action to execute
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // get the index of the file that this tab represents
            int index = ((JTabbedPane) pane.getVisualComponent()).indexOfTabComponent(TabComponent.this);
            SingleDocumentModel currentDocument = pane.getDocument(index);

            // check if file was modified
            if (currentDocument.isModified()) {

                // get the name of the file
                String name = currentDocument.getFilePath() == null ? DefaultMultipleDocumentModel.UNAMED
                        : currentDocument.getFilePath().getFileName().toString();

                // ask the user if he wants to save the file
                int selected = JOptionPane.showOptionDialog(TabComponent.this,
                        LocalizationProvider.getInstance().getString(Keys.saveDocument) + " " + name,
                        LocalizationProvider.getInstance().getString(Keys.warning),
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        new String[] { LocalizationProvider.getInstance().getString(Keys.yes),
                                LocalizationProvider.getInstance().getString(Keys.no),
                                LocalizationProvider.getInstance().getString(Keys.cancel) },
                        2);

                // check if user said yes
                if (selected == 0) {
                    // check if new file if yes then save as
                    if (currentDocument.getFilePath() == null) {

                        // get path to save as
                        Path openedFilePath = getPathForSAving();

                        // check if user provided a path if not inform him taht nothing has been saved
                        if (openedFilePath == null) {
                            JOptionPane.showOptionDialog(TabComponent.this,
                                    LocalizationProvider.getInstance().getString(Keys.nothingSaved),
                                    LocalizationProvider.getInstance().getString(Keys.warning),
                                    JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE, null,
                                    new String[] { LocalizationProvider.getInstance().getString(Keys.ok) },
                                    0);
                            return;
                        }

                        // save the document on the selected path
                        pane.saveDocument(currentDocument, openedFilePath);
                        return;
                    }

                    // save the document
                    pane.saveDocument(currentDocument, null);
                }
                // if cancel was called do nothing
                else if (selected == 2)
                    return;
            }

            // if no was selcted exit without saving
            ((JTabbedPane) pane.getVisualComponent()).remove(index);
            pane.closeDocument(currentDocument);
        }

        /**
         * A private method that asks the user to select the path on which to save the
         * file through the file chooser
         */
        private Path getPathForSAving() {
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle(LocalizationProvider.getInstance().getString(Keys.saveDocument));
            if (jfc.showSaveDialog(TabComponent.this) != JFileChooser.APPROVE_OPTION)
                return null;
            return jfc.getSelectedFile().toPath();
        }
    }

    /**
     * A class that represents a {@link SingleDocumentListener} that will update
     * this {@link TabComponent} appereance on each change.
     *
     * @author Marko Tunjić
     */
    private class Repaint implements SingleDocumentListener {

        /**
         * A method that updates the icon if the modifiy status has been updated
         *
         * @param model the model that has been changed
         */
        @Override
        public void documentModifyStatusUpdated(SingleDocumentModel model) {
            // remove old stuff
            TabComponent.this.remove(icon);
            TabComponent.this.remove(title);
            TabComponent.this.remove(button);

            // update icon
            modified = model.isModified();
            icon = new JLabel(modified ? redDiskette : greenDiskette);

            // add new stuff
            TabComponent.this.add(icon);
            TabComponent.this.add(title);
            TabComponent.this.add(button);
        }

        /**
         * A method that updates the title if the path has been updated
         *
         * @param model the model that has been changed
         */
        @Override
        public void documentFilePathUpdated(SingleDocumentModel model) {
            // remove old stuff
            TabComponent.this.remove(icon);
            TabComponent.this.remove(title);
            TabComponent.this.remove(button);

            // update title
            title = new JLabel(model.getFilePath() == null ? DefaultMultipleDocumentModel.UNAMED
                    : model.getFilePath().getFileName().toString());

            // add new stuff
            TabComponent.this.add(icon);
            TabComponent.this.add(title);
            TabComponent.this.add(button);
        }

    }

}
