package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the interface {@link ILocalizationProvider} and
 * implements all the methods that are same for each localization provider to
 * reduce redundancy
 *
 * @author Marko Tunjić
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
    /** An private attribute that contains all the connected listeners */
    private List<ILocalizationListener> listeners = new ArrayList<>();

    /**
     * A method that adds the listener to the listener collection. Throws
     * {@link NullPointerException} if null was given
     *
     * @param listener the listener that is interested in localization changes
     *
     * @throws NullPointerException if null was given
     */
    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener can not be null");
        listeners.add(listener);
    }

    /**
     * A method that removes the listener from the listener collection.
     *
     * @param listener the listener that was interested in localization changes
     */
    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * A method that informs the all the reigstered listeners that a localization
     * change happened
     */
    public void fire() {
        for (var listener : listeners)
            listener.localizationChanged();
    }
}
