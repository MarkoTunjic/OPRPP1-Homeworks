package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * An interface that defines the behaviour that a class must provide if it wants
 * to be a LocalizationProvider.
 *
 * @author Marko Tunjić
 */
public interface ILocalizationProvider {
    /**
     * A method that registers a listener on LocalizationChanges. Throws
     * {@link NullPointerException} if null was given
     *
     * @param listener the listener that is interested in localization changes
     *
     * @throws NullPointerException if null was given
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * A method that removes a listener from interested listeners
     *
     * @param listener the listener that was interested in localization changes
     */
    void removeLocalizationListener(ILocalizationListener listener);

    /**
     * A method that gets the localized string for the given key.Throws
     * {@link NullPointerException} if null was given.
     *
     * @param key the key of the string
     * @return the localized string fro the provided key
     * @thrown {@link NullPointerException} if null was given
     */
    String getString(String key);

    /**
     * A method that returns the current language {@link java.util.Locale}
     *
     * @return current language {@link java.util.Locale}
     */
    String getLanguage();

}
