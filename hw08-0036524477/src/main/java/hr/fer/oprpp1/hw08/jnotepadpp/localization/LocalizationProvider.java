package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A class that extends {@link AbstractLocalizationProvider} and provides the
 * methods that are not defined in {@link AbstractLocalizationProvider} becouse
 * they are not universal. The methods are: method for getting the current
 * language and getting the string for the string key
 *
 * @author Marko Tunjić
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
    /** An private attribute that represents the current languge */
    private String language;

    /**
     * An private attribute that contains the bundle of all supported languages and
     * their strings with corresponding keys
     */
    private ResourceBundle bundle;

    /**
     * A static constant that creates a singletone instance of the
     * {@link LocalizationProvider}
     */
    private static final LocalizationProvider instance = new LocalizationProvider();

    /**
     * An private constructor that creates a {@link LocalizationProvider} object by
     * setting the initial language which is english and it gets the Resource bundle
     */
    private LocalizationProvider() {
        this.language = "en";
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.localization.translation", locale);
    }

    /**
     * A method that changes the current language to the given Locale. Throws
     * {@link NullPointerException} if null was given
     *
     * @param the language Locale that is going to replace the current
     */
    public void setLanguage(String language) {
        if (language == null)
            throw new NullPointerException("Language can not be null");

        // change language
        this.language = language;

        // update bundle
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.localization.translation", locale);

        // inform all registered listeners
        fire();
    }

    /**
     * A method that returns the singletone instance of a
     * {@link LocalizationProvider}
     *
     * @return the the singletone instance of a {@link LocalizationProvider}
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * A method that returns the current language {@link java.util.Locale}
     *
     * @return current language {@link java.util.Locale}
     */
    @Override
    public String getLanguage() {
        return language;
    }

    /**
     * A method that gets the localized string for the given key.Throws
     * {@link NullPointerException} if null was given.
     *
     * @param key the key of the string
     * @return the localized string fro the provided key
     * @thrown {@link NullPointerException} if null was given
     */
    @Override
    public String getString(String key) {
        if (key == null)
            throw new NullPointerException("Key can not be null");
        return bundle.getString(key);
    }
}
