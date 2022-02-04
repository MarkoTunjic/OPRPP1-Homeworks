package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * A class that extends the class {@link LocalizationProviderBridge} and acts as
 * a bridge between the localization provider and the {@link JFrame} so that
 * when the window is closed the listener is disconected
 *
 * @author Marko Tunjić
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    /**
     * A constructor that creates a {@link FormLocalizationProvider} from the given
     * params.
     * And adds a window listener on the given frame so that when it closes the
     * listener is disconected. Throws {@link NullPointerException} if null was
     * given
     *
     * @param parent the real Provider
     * @param frame  the frame that is interested in localization chnages
     *
     * @throws NullPointerException if null was given
     */
    public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
        // call the super constructor
        super(parent);

        // check if null
        if (frame == null)
            throw new NullPointerException("Frame can not be null");

        // create the window listener
        frame.addWindowListener(new WindowListener() {

            /**
             * A method that connects the frame to the localization provider on open
             *
             * @param e the event that started the action
             */
            @Override
            public void windowOpened(WindowEvent e) {
                FormLocalizationProvider.this.connect();
            }

            /**
             * A method that disconects the frame to the localization provider on close
             *
             * @param e the event that started the action
             */
            @Override
            public void windowClosing(WindowEvent e) {
                FormLocalizationProvider.this.disconnect();
            }

            /**
             * A method that disconects the frame to the localization provider on close
             *
             * @param e the event that started the action
             */
            @Override
            public void windowClosed(WindowEvent e) {
                FormLocalizationProvider.this.disconnect();
            }

            /** Empty method aka does nothing */
            @Override
            public void windowIconified(WindowEvent e) {
            }

            /** Empty method aka does nothing */
            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            /** Empty method aka does nothing */
            @Override
            public void windowActivated(WindowEvent e) {
            }

            /** Empty method aka does nothing */
            @Override
            public void windowDeactivated(WindowEvent e) {
            }

        });
    }

}
