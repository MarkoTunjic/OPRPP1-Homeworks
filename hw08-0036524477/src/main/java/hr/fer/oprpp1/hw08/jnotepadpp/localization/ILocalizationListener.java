package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * An interface that is used to inform objects that a Localization change has
 * happened
 *
 * @author Marko Tunjić
 */
public interface ILocalizationListener {
    /** A method that is called when a localization change happened */
    void localizationChanged();
}
