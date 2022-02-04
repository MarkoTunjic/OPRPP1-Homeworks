package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;

import java.text.Collator;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.swing.Timer;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;

/**
 * A class that represents a text editor with multiple tabs
 *
 * @author Marko Tunjić
 */
public class JNotepadPP extends JFrame {
    /**
     * An private attribute that contains the reference to the
     * {@link MultipleDocumentModel} that handles multiple documents
     */
    private MultipleDocumentModel tabbedPane;

    /**
     * An private attribute that is used to get localized strings and for
     * subscribing listners on localization changes
     */
    private ILocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

    /**
     * A constructor that initializes the tabbed pane and adds listeners for
     * changes, and adds a window listener
     */
    public JNotepadPP() {
        // initialize tabbed pane
        tabbedPane = new DefaultMultipleDocumentModel();

        // add listener for title
        tabbedPane.addMultipleDocumentListener(new ChangeTitle());

        // set initial values
        setSize(800, 500);
        setTitle("JNotepad++");

        // add close listener
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            /**
             * A method that is called whenever X is pressed and it calls the windowClosed
             * method
             */
            @Override
            public void windowClosing(WindowEvent e) {
                windowClosed(e);
            }

            /**
             * A method that is called whenever dispose is called and calls the custom exit
             * method
             */
            @Override
            public void windowClosed(WindowEvent e) {
                JNotepadPP.this.exit(new ActionEvent(JNotepadPP.this, ActionEvent.CTRL_MASK, "close"));
            }
        });

        // initi gui
        initGui();
    }

    /** A method that initializes the GUI elements */
    private void initGui() {
        // the content pane of this app
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // new panel for center text element and status bat
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tabbedPane.getVisualComponent(), BorderLayout.CENTER);
        cp.add(centerPanel, BorderLayout.CENTER);

        initializeActions();
        createMenus();
        createToolbars();
        createStatusBar(centerPanel);
    }

    /**
     * A method that initializes the status bar and adds it to the given panel
     *
     * @param the panel to which this status bar should be added
     */
    private void createStatusBar(JPanel centerPanel) {
        StatusBar bottomPanel = new StatusBar();
        tabbedPane.addMultipleDocumentListener(bottomPanel.new DocumentChanged());
        centerPanel.add(bottomPanel, BorderLayout.PAGE_END);
    }

    /**
     * An private attribute that represents the action that will open a document
     */
    private final Action openDocumentAction = new LocalizableAction(Keys.openFile, Keys.openDescription, flp) {

        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // choose file to open
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(flp.getString(Keys.openFile));

            // if nothing selected
            if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            // get path from file chooser
            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();

            // if not readeable
            if (!Files.isReadable(filePath)) {
                JOptionPane.showOptionDialog(JNotepadPP.this,
                        flp.getString(Keys.file) + " " + fileName.getAbsolutePath() + " "
                                + flp.getString(Keys.notExist),
                        flp.getString(Keys.error),
                        JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE, null,
                        new String[] { flp.getString(Keys.ok) },
                        0);
                return;
            }

            // load the documents in the tabbed pane
            tabbedPane.loadDocument(filePath);
        }
    };

    /**
     * An private attribute that represents the action that will create a new
     * document
     */
    private final Action newDocumentAction = new LocalizableAction(Keys.newFile, Keys.newDocumentDescription, flp) {

        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // just call the method to create the document on the tabbed pane
            tabbedPane.createNewDocument();
        }
    };

    /**
     * An private attribute that represents the action that will save a
     * document
     */
    private final Action saveDocumentAction = new LocalizableAction(Keys.saveFile, Keys.saveDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // get the current document
            SingleDocumentModel currentDocument = tabbedPane.getCurrentDocument();

            // save it
            saveDocument(currentDocument, e);
        }
    };

    /**
     * A private method that saves the current document by checking if it exists on
     * the disc and calls save as or just calls save on the
     * {@link MultipleDocumentModel}
     *
     * @param currentDocument the document to be saved
     * @param e               the action event that called this method
     */
    private void saveDocument(SingleDocumentModel currentDocument, ActionEvent e) {
        // check if null and do nothing
        if (currentDocument == null)
            return;

        // if new file get the path for saving
        if (currentDocument.getFilePath() == null) {
            saveDocumentAsAction.actionPerformed(e);
            return;
        }

        // sae the document
        tabbedPane.saveDocument(currentDocument, null);
    }

    /**
     * An private attribute that represents the action that saves the current
     * document as by getting the path
     * through {@link javax.swing.plaf.FileChooserUI} and
     * calling the save method on the {@link MultipleDocumentModel}
     */
    private final Action saveDocumentAsAction = new LocalizableAction(Keys.saveFileAs, Keys.saveAsDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // check if null and do nothing
            if (tabbedPane.getCurrentDocument() == null)
                return;

            // get the path for saving
            Path openedFilePath = getPathForSAving();

            // if nothing selected
            if (openedFilePath == null) {
                JOptionPane.showOptionDialog(JNotepadPP.this,
                        flp.getString(Keys.nothingSaved),
                        flp.getString(Keys.warning),
                        JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        new String[] { flp.getString(Keys.ok) },
                        0);
                return;
            }

            // if file already exists ask to overwrite
            if (openedFilePath.toFile().exists()) {
                int result = JOptionPane.showOptionDialog(JNotepadPP.this,
                        flp.getString(Keys.overwrite),
                        flp.getString(Keys.warning),
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        new String[] { flp.getString(Keys.yes),
                                flp.getString(Keys.no) },
                        1);
                if (result == 1)
                    return;
            }

            // call the save document on the multiple codument model
            tabbedPane.saveDocument(tabbedPane.getCurrentDocument(), openedFilePath);
        }
    };

    /**
     * An private attribute that represents the action that closes the current
     * document by
     * calling the close method on the {@link MultipleDocumentModel}
     */
    private final Action closeCurrentDocumentAction = new LocalizableAction(Keys.closeFile, Keys.closeDescription,
            flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentDocument = tabbedPane.getCurrentDocument();
            if (closeDocument(currentDocument, e)) {
                // remove and close docuemnt
                ((JTabbedPane) tabbedPane.getVisualComponent())
                        .removeTabAt(tabbedPane.getIndexOfDocument(currentDocument));
                tabbedPane.closeDocument(currentDocument);
            }
        }
    };

    /**
     * An private method that handles close events by checking if the document was
     * modified and asking for saving or just closes it
     *
     * @param currentDocument the document to be closed
     * @param e               the action event that called this method
     */
    private boolean closeDocument(SingleDocumentModel currentDocument, ActionEvent e) {
        // check if null and do nothing
        if (currentDocument == null)
            return false;

        // check if modifed
        if (currentDocument.isModified()) {
            // ask for saving
            String name = currentDocument.getFilePath() == null ? DefaultMultipleDocumentModel.UNAMED
                    : currentDocument.getFilePath().toAbsolutePath().toString();
            int selected = JOptionPane.showOptionDialog(JNotepadPP.this,
                    flp.getString(Keys.saveDocument) + " " + name,
                    flp.getString(Keys.warning),
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                    new String[] { flp.getString(Keys.yes),
                            flp.getString(Keys.no),
                            flp.getString(Keys.cancel) },
                    2);

            // yes
            if (selected == 0)
                saveDocumentAction.actionPerformed(e);

            // cancel
            else if (selected == 2)
                return false;
        }
        return true;
    }

    /**
     * An private attribute that represents the action that copies the current
     * selection by
     * calling the copy method on the {@link JTextArea}
     */
    private final Action copyCurrentSelectionAction = new LocalizableAction(Keys.copy, Keys.copyDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // check if null and do nothing
            if (tabbedPane.getCurrentDocument() == null)
                return;
            tabbedPane.getCurrentDocument().getTextComponent().copy();
        }
    };

    /**
     * An private attribute that represents the action that pastes the current
     * content from the clipboard by
     * calling the paste method on the {@link JTextArea}
     */
    private final Action pasteFromClipboardAction = new LocalizableAction(Keys.paste, Keys.pasteDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabbedPane.getCurrentDocument() == null)
                return;
            tabbedPane.getCurrentDocument().getTextComponent().paste();
        }
    };

    /**
     * An private attribute that represents the action that copies the current
     * selection by
     * calling the cut method on the {@link JTextArea}
     */
    private final Action cutCurrentSelectionAction = new LocalizableAction(Keys.cut, Keys.cutDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabbedPane.getCurrentDocument() == null)
                return;
            tabbedPane.getCurrentDocument().getTextComponent().cut();
        }
    };

    /**
     * An private attribute that represents the action that shows the user the
     * statistical info about the current docuemnt
     */
    private final Action getStatisticalInfoAction = new LocalizableAction(Keys.statistics, Keys.statisticsDescription,
            flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // check if null and do nothing
            if (tabbedPane.getCurrentDocument() == null)
                return;

            // get the current text area
            JTextArea textArea = tabbedPane.getCurrentDocument().getTextComponent();

            // get statistical info
            int numOfcharacters = textArea.getText().length();
            int numOfNonWhitespaceCharacters = getNonWhitespaceCount(textArea.getText());
            int numOfLines = textArea.getLineCount();

            // show statistical info
            JOptionPane.showOptionDialog(JNotepadPP.this,
                    String.format("%s %d %s, %d %s %s %d %s.",
                            flp.getString(Keys.yourDocumentHas), numOfcharacters, flp.getString(Keys.letters),
                            numOfNonWhitespaceCharacters, flp.getString(Keys.nonBlank), flp.getString(Keys.and),
                            numOfLines, flp.getString(Keys.lines)),
                    flp.getString(Keys.warning),
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new String[] { flp.getString(Keys.ok) },
                    0);
        }

        /**
         * An private method that returns the number of non whitespaces characters for
         * the given text
         *
         * @param text the text for which the count is needed
         *
         * @return the number of non whitespaces characters for
         *         the given text
         */
        private int getNonWhitespaceCount(String text) {
            int counter = 0;
            for (char c : text.toCharArray()) {
                if (!Character.isWhitespace(c))
                    counter++;
            }
            return counter;
        }
    };

    /**
     * An private attribute that represents the action that exits from the app
     */
    private final Action exitAction = new LocalizableAction(Keys.exit, Keys.exitDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            exit(e);
        }
    };

    /**
     * An private method that exits from the app by checking if all documents are
     * saved and if not the user is asked if he wants to save
     *
     * @param e the action that called this method
     */
    private void exit(ActionEvent e) {
        // get the docuemnt iterator to loop through all documents
        Iterator<SingleDocumentModel> iterator = tabbedPane.iterator();
        while (iterator.hasNext()) {
            SingleDocumentModel document = iterator.next();
            if (!closeDocument(document, e))
                return;
        }
        System.exit(0);
    }

    /**
     * An private attribute that represents the action that sets the current
     * selection to uppercase
     */
    private final Action upperCaseAction = new LocalizableAction(Keys.upperCase, Keys.upperCaseDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            replaceSelected(String::toUpperCase, JNotepadPP.this.tabbedPane.getCurrentDocument().getTextComponent());
        }
    };

    /**
     * An private attribute that represents the action that sets the current
     * selection to lower case
     */
    private final Action lowerCaseAction = new LocalizableAction(Keys.lowerCase, Keys.lowerCaseDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            replaceSelected(String::toLowerCase, JNotepadPP.this.tabbedPane.getCurrentDocument().getTextComponent());
        }
    };

    /**
     * An private attribute that represents the action that switches lowercase and
     * uppercase letters in the current selection
     */
    private final Action switchCaseAction = new LocalizableAction(Keys.switchCase, Keys.switchCaseDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            replaceSelected(JNotepadPP::switchCase, JNotepadPP.this.tabbedPane.getCurrentDocument().getTextComponent());
        }
    };

    /**
     * An private attribute that represents the action that sorts the currently
     * selected rows in ascending order if not a full line is selcted the action
     * behaves as if it is
     */
    private final Action sortAscendingAction = new LocalizableAction(Keys.ascending, Keys.ascendingDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            sort(true, JNotepadPP.this.tabbedPane.getCurrentDocument().getTextComponent());
        }
    };

    /**
     * An private attribute that represents the action that sorts the currently
     * selected rows in descending order if not a full line is selcted the action
     * behaves as if it is
     */
    private final Action sortDescendingAction = new LocalizableAction(Keys.descending, Keys.descendingDescription,
            flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            sort(false, JNotepadPP.this.tabbedPane.getCurrentDocument().getTextComponent());
        }
    };

    /**
     * An private attribute that represents the action that sets the current
     * localizatio to english
     */
    private final Action changeLanguageToEnglishAction = new LocalizableAction(Keys.en, Keys.enDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider.getInstance().setLanguage("en");
        }
    };

    /**
     * An private attribute that represents the action that sets the current
     * localizatio to croatian
     */
    private final Action changeLanguageToCroatianAction = new LocalizableAction(Keys.hr, Keys.hrDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider.getInstance().setLanguage("hr");
        }
    };

    /**
     * An private attribute that represents the action that sets the current
     * localizatio to german
     */
    private final Action changeLanguageToGermanAction = new LocalizableAction(Keys.de, Keys.deDescription, flp) {
        private static final long serialVersionUID = 1L;

        /**
         * A method that is called whenever the event for this action has happened
         *
         * @param e the action event that caused this action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalizationProvider.getInstance().setLanguage("de");
        }
    };

    /**
     * A private method that sorts the currently selected in the current
     * {@link JTextArea} if not a full line is selcted the method
     * behaves as if it is
     *
     * @param ascending indicates if ascending or descending order
     */
    private void sort(boolean ascending, JTextComponent textArea) {
        // get the curernt locale collator
        Locale locale = new Locale(flp.getLanguage());
        Collator collator = Collator.getInstance(locale);

        // get the text docuemnt
        Document doc = textArea.getDocument();
        try {
            // get the begining and endging line
            Element root = doc.getDefaultRootElement();
            int caretPosition = textArea.getCaretPosition();
            int startLine = root.getElementIndex(textArea.getSelectionStart());
            int endLine = root.getElementIndex(textArea.getSelectionEnd());
            textArea.select(0, 0);
            textArea.setCaretPosition(caretPosition);

            // get the first and last index
            int first = root.getElement(startLine).getStartOffset();
            int last = root.getElement(endLine).getEndOffset();

            // calculate the lenght
            int len = last - first;

            // get the text
            String text = doc.getText(first, len);

            // get the rows
            List<String> lines = Arrays.asList(text.split("\n"));

            // sort the rows
            if (ascending)
                lines.sort((a, b) -> collator.compare(a, b));
            else
                lines.sort((a, b) -> collator.compare(b, a));

            // rebuild the text by concating lines
            StringBuilder builder = new StringBuilder();
            lines.forEach(line -> builder.append(line).append("\n"));
            builder.deleteCharAt(builder.length() - 1);
            text = builder.toString();

            // remove old and put new
            doc.remove(first, len - 1);
            doc.insertString(first, text, null);
        }
        // carret exception
        catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * An private method that switches the the lowercase letters and uppercase
     * leeters in the given text
     *
     * @param the text hat needs changing
     *
     * @return the changed text
     */
    private final static String switchCase(String text) {
        char[] znakovi = text.toCharArray();
        for (int i = 0; i < znakovi.length; i++) {
            char c = znakovi[i];
            if (Character.isLowerCase(c)) {
                znakovi[i] = Character.toUpperCase(c);
            } else if (Character.isUpperCase(c)) {
                znakovi[i] = Character.toLowerCase(c);
            }
        }
        return new String(znakovi);
    }

    /**
     * An private method that replaces the currently selected text in the
     * {@link JTextArea} with the text
     * that is a reuslt of applyng the given function on it
     *
     * @param function the function that will calculate the new text
     */
    private void replaceSelected(Function<String, String> function, JTextComponent textArea) {
        // get the document
        Document doc = textArea.getDocument();

        // get the start of selection and lenght of selection
        int offset = textArea.getSelectionStart();
        int len = textArea.getSelectionEnd() - textArea.getSelectionStart();
        try {
            // get the old text
            String text = doc.getText(offset, len);

            // get the new text
            text = function.apply(text);

            // replace old with new
            doc.remove(offset, len);
            doc.insertString(offset, text, null);
        } catch (BadLocationException e1) {
            System.out.println("Error while working with text");
        }
    }

    /**
     * A method that displays a {@link JFileChooser} and requests the user to select
     * a path to a file and returns the path.
     *
     * @return the selected path from {@link JFileChooser}
     */
    public Path getPathForSAving() {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(flp.getString(Keys.saveDocument));
        if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
            return null;
        return jfc.getSelectedFile().toPath();
    }

    /**
     * A private method that initializes all actions by setting their mnemonics and
     * keys
     */
    private void initializeActions() {
        // get current default actin maps and input maps
        ActionMap amap = tabbedPane.getVisualComponent().getActionMap();
        InputMap imap = tabbedPane.getVisualComponent().getInputMap();

        // initialize some normal actions
        initializeAction(openDocumentAction, "control O", KeyEvent.VK_O);
        initializeAction(newDocumentAction, "control N", KeyEvent.VK_N);
        initializeAction(saveDocumentAction, "control S", KeyEvent.VK_S);

        // remove the deafult control a actions and set our
        imap.put(KeyStroke.getKeyStroke("control A"), "none");
        initializeAction(saveDocumentAsAction, "control A", KeyEvent.VK_A);

        // normal
        initializeAction(closeCurrentDocumentAction, "control E", KeyEvent.VK_E);

        // remove the deafult copy actions and set our
        imap.put(KeyStroke.getKeyStroke("control C"), "none");
        initializeAction(copyCurrentSelectionAction, "control C", KeyEvent.VK_C);
        amap.put(DefaultEditorKit.copyAction, copyCurrentSelectionAction);

        // remove the deafult paste actions and set our
        Object actionKey = imap.get(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        imap.put(KeyStroke.getKeyStroke("control V"), "none");
        imap.put(KeyStroke.getKeyStroke("P"), actionKey);
        initializeAction(pasteFromClipboardAction, "control P", KeyEvent.VK_P);
        amap.put(DefaultEditorKit.pasteAction, pasteFromClipboardAction);

        // remove the deafult cut actions and set our
        actionKey = imap.get(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        imap.put(KeyStroke.getKeyStroke("control X"), "none");
        imap.put(KeyStroke.getKeyStroke("U"), actionKey);
        initializeAction(cutCurrentSelectionAction, "control U", KeyEvent.VK_U);
        amap.put(DefaultEditorKit.cutAction, cutCurrentSelectionAction);

        // initialize some normal actions
        initializeAction(getStatisticalInfoAction, "control I", KeyEvent.VK_I);
        initializeAction(exitAction, "control X", KeyEvent.VK_X);
        initializeAction(upperCaseAction, "control U", KeyEvent.VK_U);
        initializeAction(lowerCaseAction, "control L", KeyEvent.VK_L);
        initializeAction(switchCaseAction, "control T", KeyEvent.VK_T);
        initializeAction(sortAscendingAction, "control G", KeyEvent.VK_G);
        initializeAction(sortDescendingAction, "control D", KeyEvent.VK_D);

        // initially disable the case options
        upperCaseAction.setEnabled(false);
        lowerCaseAction.setEnabled(false);
        switchCaseAction.setEnabled(false);
    }

    /** A private method that create the menus bar */
    private void createMenus() {
        // create menu bar
        JMenuBar menuBar = new JMenuBar();

        // add files menu
        JMenu fileMenu = new LJMenu(Keys.file, flp);
        menuBar.add(fileMenu);

        // add file items
        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.add(new JMenuItem(newDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAsAction));
        fileMenu.add(new JMenuItem(closeCurrentDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));

        // add edit menu
        JMenu editMenu = new LJMenu(Keys.edit, flp);
        menuBar.add(editMenu);

        // add menu items
        editMenu.add(new JMenuItem(copyCurrentSelectionAction));
        editMenu.add(new JMenuItem(pasteFromClipboardAction));
        editMenu.add(new JMenuItem(cutCurrentSelectionAction));

        // add menu info
        JMenu infoMenu = new LJMenu(Keys.info, flp);
        menuBar.add(infoMenu);

        // add info items
        infoMenu.add(new JMenuItem(getStatisticalInfoAction));

        // add menu languages
        JMenu languageMenu = new LJMenu(Keys.languages, flp);
        menuBar.add(languageMenu);

        // add language items
        languageMenu.add(new JMenuItem(changeLanguageToEnglishAction));
        languageMenu.add(new JMenuItem(changeLanguageToGermanAction));
        languageMenu.add(new JMenuItem(changeLanguageToCroatianAction));

        // add menu tools
        JMenu toolsMenu = new LJMenu(Keys.tools, flp);
        menuBar.add(toolsMenu);

        // add submenu case
        JMenu caseMenu = new LJMenu(Keys.changeCase, flp);
        toolsMenu.add(caseMenu);

        // add case items
        caseMenu.add(new JMenuItem(upperCaseAction));
        caseMenu.add(new JMenuItem(lowerCaseAction));
        caseMenu.add(new JMenuItem(switchCaseAction));

        // add submenu sort
        JMenu sortMenu = new LJMenu(Keys.sort, flp);
        toolsMenu.add(sortMenu);

        // add sort items
        sortMenu.add(new JMenuItem(sortAscendingAction));
        sortMenu.add(new JMenuItem(sortDescendingAction));

        // set the menu bar
        this.setJMenuBar(menuBar);
    }

    /** A private method that creates the toolbar */
    private void createToolbars() {
        JToolBar toolBar = new JToolBar(flp.getString(Keys.tools));
        toolBar.setFloatable(true);

        // add all file actions
        toolBar.add(new JButton(openDocumentAction));
        toolBar.add(new JButton(newDocumentAction));
        toolBar.add(new JButton(saveDocumentAction));
        toolBar.add(new JButton(saveDocumentAsAction));
        toolBar.add(new JButton(closeCurrentDocumentAction));

        toolBar.addSeparator();

        // add all edit actions
        toolBar.add(new JButton(copyCurrentSelectionAction));
        toolBar.add(new JButton(pasteFromClipboardAction));
        toolBar.add(new JButton(cutCurrentSelectionAction));

        toolBar.addSeparator();

        // add info actions
        toolBar.add(new JButton(getStatisticalInfoAction));

        this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    /**
     * A private method that initializes a single action by setting its accelerator
     * key and mnenmonic key event
     */
    private void initializeAction(Action action, String keystroke, int keyEvent) {
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(keystroke));
        action.putValue(Action.MNEMONIC_KEY, keyEvent);
    }

    /**
     * A main method that starts the GUI
     *
     * @param args should be empty or ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }

    /**
     * A private class that extends {@link JPanel} and represents the status bar of
     * a single document
     *
     * @author Marko Tunjić
     */
    private class StatusBar extends JPanel {
        /**
         * An private attribitue that contains the GUI element that contains the length
         * of the file
         */
        private JLabel length;

        /**
         * An private attribitue that contains the GUI element that contains the caret
         * position
         */
        private JLabel caret;

        /**
         * An private attribitue that contains the GUI element that contains the time
         */
        private JLabel time;

        /**
         * An private attribitue that contains the GUI element that contains the
         * reference to the current text area
         */
        private JTextArea currentTextArea;

        /**
         * An private attribitue that contains the reference to the caret listener to be
         * added and removed
         */
        private CaretListener caretListener;

        /**
         * An private attribitue that contains the reference to the document listener to
         * be added and removed
         */
        private DocumentListener documentListener;

        /**
         * An private attribute that contains the number of characters in the document
         */
        private int numOfCharacters;

        /** A default constructor that initializes this component */
        public StatusBar() {
            setLayout(new GridLayout(1, 3));
            numOfCharacters = 0;

            // initial values
            length = new JLabel(String.format("%s: %d", flp.getString(Keys.length), numOfCharacters));
            caret = new JLabel(String.format("Ln: %d Col: %d Sel: %d", 0, 0, 0));
            time = new JLabel("", SwingConstants.RIGHT);
            updateTime();
            caretListener = new ChangeCaretStatusBar();

            // localization listener to change the text
            flp.addLocalizationListener(
                    () -> length.setText(String.format("%s: %d", flp.getString(Keys.length), numOfCharacters)));

            // docuemnt listener to update the info
            documentListener = new TextChanged();

            // timer to change the time
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateTime();
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();

            // add components
            add(length);
            add(caret);
            add(time);
        }

        /** A private method that sets the current time */
        private void updateTime() {
            String format = "YYYY/MM/DD HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            time.setText(sdf.format(new Date()));
        }

        /** A private method that updates the care status */
        private void updateCaretStatus() {
            int line;
            int column;
            // try getting caret
            try {
                // calculate the values
                line = currentTextArea.getLineOfOffset(currentTextArea.getCaretPosition());
                column = currentTextArea.getCaretPosition() - currentTextArea.getLineStartOffset(line);
            }
            // caret exceptions
            catch (BadLocationException ex) {
                System.out.println("Error whle working with carret");
                return;
            }

            // get the selection
            int selection = currentTextArea.getSelectionEnd() - currentTextArea.getSelectionStart();

            // set the caret info
            caret.setText(String.format("Ln: %d Col: %d Sel: %d", line + 1, column, selection));
        }

        /**
         * A private method that updates the status of the document by reseting its
         * length
         */
        private void updateDocumentStatus() {
            numOfCharacters = currentTextArea.getText().length();
            length.setText(String.format("%s: %d", flp.getString(Keys.length), numOfCharacters));
        }

        /**
         * A private class that represents a caret listener and is used to update the
         * caret status on each caret update
         *
         * @author Marko TUnjić
         */
        private class ChangeCaretStatusBar implements CaretListener {

            @Override
            public void caretUpdate(CaretEvent e) {
                if (tabbedPane.getCurrentDocument().getTextComponent().getSelectedText() == null
                        || tabbedPane.getCurrentDocument().getTextComponent().getSelectedText().length() == 0) {
                    upperCaseAction.setEnabled(false);
                    lowerCaseAction.setEnabled(false);
                    switchCaseAction.setEnabled(false);
                } else {
                    upperCaseAction.setEnabled(true);
                    lowerCaseAction.setEnabled(true);
                    switchCaseAction.setEnabled(true);
                }
                updateCaretStatus();

            }
        }

        /**
         * A private class that represents a {@link MultipleDocumentListener} and is
         * used to update the
         * caret status, and document lenght on each document change
         *
         * @author Marko TUnjić
         */
        private class DocumentChanged implements MultipleDocumentListener {

            /**
             * A method that gets the values of the current dpcuemnt and removes the
             * listeners from the previous docuemnt
             *
             * @param previousModel the previous docuemnt
             * @param currentModel  the current docuemnt
             */
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                // remove listeners
                if (previousModel != null) {
                    previousModel.getTextComponent().removeCaretListener(caretListener);
                    previousModel.getTextComponent().getDocument().removeDocumentListener(documentListener);
                }

                // if no docuemnt active set to deafult aka 0
                if (currentModel == null) {
                    currentTextArea = null;
                    caret.setText(String.format("Ln: %d Col: %d Sel: %d", 0, 0, 0));
                    length.setText(String.format("%s: %d", flp.getString(Keys.length), 0));
                    return;
                }

                // add new listeners and update state
                currentTextArea = currentModel.getTextComponent();
                currentTextArea.addCaretListener(caretListener);
                currentTextArea.getDocument().addDocumentListener(documentListener);

                // update status
                updateCaretStatus();
                updateDocumentStatus();
            }

            /** Empty method becouse its not needed */
            @Override
            public void documentAdded(SingleDocumentModel model) {
            }

            /** Empty method becouse its not needed */
            @Override
            public void documentRemoved(SingleDocumentModel model) {
            }
        }

        /**
         * An private class that represents a {@link DocumentListener} that will update
         * the
         * lenght of the doucment
         *
         * @author Marko TUnjić
         */
        private class TextChanged implements DocumentListener {

            /** A method that calls the updateDocumentStatus on each insert update */
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateDocumentStatus();
            }

            /** A method that calls the updateDocumentStatus on each remove update */
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateDocumentStatus();
            }

            /** A method that calls the updateDocumentStatus on each change update */
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateDocumentStatus();
            }
        }
    }

    /**
     * A private class that represents a {@link MultipleDocumentListener} that is
     * ised to update the Title of app whenever a document focus is changed or the
     * docuemnt path has changed
     *
     * @author Marko Tunjić
     */
    private class ChangeTitle implements MultipleDocumentListener {

        /**
         * A private method that refreshes the app title whenever the current docuemnt
         * has Changed
         *
         * @param previousModel the prevuous docuemnt
         * @param currentModel  the current document
         */
        @Override
        public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
            String name;
            if (currentModel == null)
                name = "JNotepad++";
            else
                name = (currentModel.getFilePath() == null ? DefaultMultipleDocumentModel.UNAMED
                        : currentModel.getFilePath().toAbsolutePath().toString()) + " - JNotepad++";
            JNotepadPP.this.setTitle(name);
        }

        /** Empty method becouse not needed */
        @Override
        public void documentAdded(SingleDocumentModel model) {
        }

        /** Empty method becouse not needed */
        @Override
        public void documentRemoved(SingleDocumentModel model) {
        }

    }
}
