package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * A class that acts a bridge between the real Provider and the interested
 * objects so the object can not connect itself multiple times. And provides
 * methods for connecting and disconnecting
 *
 * @author Marko Tunjić
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
    /** An private attribute that says if the object s connected or not */
    private boolean connected;

    /**
     * An private attribute that contains the reference to the real localization
     * provider
     */
    private ILocalizationProvider parent;

    /**
     * An private attribute that is a listener and informs all of the interested
     * listeners that the localization has changed, and updates the current language
     * attribute
     */
    private ILocalizationListener listener = () -> {
        this.currentLanguage = parent.getLanguage();
        this.fire();
    };

    /**
     * An private attribute that contains the current active language used for
     * cheking if the language changed while the interested object was disconected
     */
    private String currentLanguage;

    /**
     * A constructor that creates a {@link LocalizationProviderBridge} object from
     * the given params. Throws {@link NullPointerException} if null was given
     *
     * @param parent
     */
    public LocalizationProviderBridge(ILocalizationProvider parent) {
        if (parent == null)
            throw new NullPointerException("Parent provider can not be null in LocalizationProviderBridge");
        this.parent = parent;
        currentLanguage = parent.getLanguage();

        // register the listener that informs the other listeners
        parent.addLocalizationListener(listener);
    }

    /**
     * A method that connects the interested object to the provider if not already
     * connected. And informs the interested objects if the localization has changed
     * while they were disconected
     */
    public void connect() {
        if (!connected) {
            parent.addLocalizationListener(listener);
            if (!currentLanguage.equals(parent.getLanguage())) {
                currentLanguage = parent.getLanguage();
                this.fire();
            }
        }

        connected = true;
    }

    /**
     * A method that disconnects the interested object from the provider.
     */
    public void disconnect() {
        if (connected)
            parent.removeLocalizationListener(listener);
        this.connected = false;
    }

    /**
     * A method that returns the localized string for the given key by delegating
     * the job to a {@link ILocalizationProvider} object
     */
    @Override
    public String getString(String key) {
        return parent.getString(key);
    }

    /**
     * A method that returns the current language by delegating
     * the job to a {@link ILocalizationProvider} object
     */
    @Override
    public String getLanguage() {
        return parent.getLanguage();
    }
}
