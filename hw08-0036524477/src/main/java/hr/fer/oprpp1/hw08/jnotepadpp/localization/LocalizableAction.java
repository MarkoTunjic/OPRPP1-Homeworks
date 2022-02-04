package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * A class that represents a {@link AbstractAction} that changes its name and
 * description based on the current Localization
 *
 * @author Marko tunjić
 */
public abstract class LocalizableAction extends AbstractAction {

    /**
     * A constructor that creates a {@link LocalizableAction} from the given params.
     * Throws {@link NullPointerException} if null was given
     *
     * @param nameKey        the key of the string for the name of the action
     * @param descriptionKey the key of the string for the description of the action
     * @param lp             the localization provider that provides the localized
     *                       string for the given key
     *
     * @throws NullPointerException if null was given
     */
    public LocalizableAction(String nameKey, String descriptionKey, ILocalizationProvider lp) {
        if (nameKey == null || descriptionKey == null || lp == null)
            throw new NullPointerException("LocalizableAction params can not be null");

        // set initial values
        this.putValue(Action.NAME, lp.getString(nameKey));
        this.putValue(Action.SHORT_DESCRIPTION, lp.getString(descriptionKey));

        // register a listener that changes the inital values
        lp.addLocalizationListener(() -> {
            this.putValue(Action.NAME, lp.getString(nameKey));
            this.putValue(Action.SHORT_DESCRIPTION, lp.getString(descriptionKey));
        });
    }
}
