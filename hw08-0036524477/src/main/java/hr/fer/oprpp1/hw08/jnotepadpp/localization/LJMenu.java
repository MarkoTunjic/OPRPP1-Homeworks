package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.JMenu;

/**
 * A class that extends {@link JMenu} and changes the text of the {@link JMenu}
 * when the localization changes
 *
 * @author Marko Tunjić
 */
public class LJMenu extends JMenu {
    /**
     * A constructor that creates a localizable JMenu aka {@link LJMenu} object from
     * the given params. Throws {@link NullPointerException} if null was given.
     *
     * @param key the key of the text that will be displayed but in different
     *            languages
     * @param lp  the localization provided that will provide the string for the
     *            given key
     *
     * @throws NullPointerException if null was given
     */
    public LJMenu(String key, ILocalizationProvider lp) {
        // check if null
        if (key == null || lp == null)
            throw new NullPointerException("Params can not be null");

        // set initial text
        this.setText(lp.getString(key));

        // register a listener
        lp.addLocalizationListener(() -> this.setText(lp.getString(key)));
    }
}
