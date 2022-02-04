package hr.fer.oprpp1.hw05;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.hw05.crypto.Util;

/**
 * A test class that contains tests for the
 * {@link hr.fer.oprpp1.hw05.crypto.Util} class
 *
 * @author Marko Tunjic
 */
public class UtilTest {

    @Test
    public void testHexToByteWhenPassingInvalidNumberOfCharacters() {
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("aee2f5b3ccd4a99923f3ee4ab3aeab3"));
    }

    @Test
    public void testHexToByteWhenPassingIvalidCharactersInStringWithLowerCaseLetters() {
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("aee2,5b3ccd4a99923f3ee4ab3aeab33"));
    }

    @Test
    public void testHexToByteWhenPassingEmptString() {
        assertArrayEquals(new byte[] {}, Util.hextobyte(""));
    }

    @Test
    public void testHexToByteWhenPassingStringWithLowerCaseLetters() {
        assertArrayEquals(new byte[] { -82, -30, -11, -77, -52, -44, -87, -103, 35, -13, -18, 74, -77, -82, -85, 51 },
                Util.hextobyte("aee2f5b3ccd4a99923f3ee4ab3aeab33"));
    }

    @Test
    public void testHexToByteWhenPassingStringWithUpperCaseLetters() {
        assertArrayEquals(new byte[] { -82, -30, -11, -77, -52, -44, -87, -103, 35, -13, -18, 74, -77, -82, -85, 51 },
                Util.hextobyte("aee2f5b3ccd4a99923f3ee4ab3aeab33".toUpperCase()));
    }

    @Test
    public void testByteToHexWhenPassingEmptyArray() {
        assertEquals("", Util.bytetohex(new byte[] {}));
    }

    @Test
    public void testByteToHexWhenPassingValidArray() {
        assertEquals("aee2f5b3ccd4a99923f3ee4ab3aeab33", Util.bytetohex(
                new byte[] { -82, -30, -11, -77, -52, -44, -87, -103, 35, -13, -18, 74, -77, -82, -85, 51 }));
    }
}
