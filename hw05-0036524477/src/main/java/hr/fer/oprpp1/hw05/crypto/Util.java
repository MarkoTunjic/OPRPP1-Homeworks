package hr.fer.oprpp1.hw05.crypto;

/**
 * A class taht provides static methods for converting hexencoded string to a
 * byte array and reverse
 *
 * @author Marko Tunjic
 */
public class Util {

    /**
     * A static method that converts a hexencoded string to a bytearray.
     * Throws a {@link IllegalArgumentException} if the string doesn't contain mod 2
     * characters or if it contains illegal characters, and
     * {@link NullPointerException} if null was given.
     *
     * @param hexString the hexencoded string to be converted
     *
     * @return the byte array represented by the hexencoded string
     *
     * @throws IllegalArgumentException if the string doesn't contain mod 2
     *                                  characters
     *                                  or if it contains illegal characters.
     * @throws NullPointerException     if null was given
     */
    public static byte[] hextobyte(String hexString) {
        // check if null
        if (hexString == null)
            throw new NullPointerException("Hex string can not be null");

        // check if valid length
        if (hexString.length() % 2 != 0)
            throw new IllegalArgumentException("Hexstring must have 32 characters!");

        // create new array
        byte[] result = new byte[hexString.length() / 2];

        // loop through the hexString and group characters in groups of 2
        for (int i = 0; i < result.length; i++) {
            // get current group
            int currentHex = i * 2;

            // try parsing if failed then invalid characters are in the string
            try {
                // create byte from hex string
                result[i] = (byte) (Integer.parseInt(hexString.substring(currentHex, currentHex + 2), 16));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Hex string must contain only numbers and characters from a to f");
            }
        }
        return result;
    }

    /**
     * A method that converts a given byte array to a hexencoded string. Throws
     * {@link NullPointerException} if null was given
     *
     * @param byteArray the byte array to be converted to a hexencoded string
     *
     * @return the hexencoded string represented by the given bytearray
     *
     * @throws NullPointerException if null was given
     */
    public static String bytetohex(byte[] byteArray) {
        // check if null
        if (byteArray == null)
            throw new NullPointerException("Byte array can not be null");

        // create string builder for solution
        StringBuilder sb = new StringBuilder();

        // loop through each byte
        for (byte element : byteArray) {
            // convert to string
            String fullHexString = Integer.toHexString(element);

            // check if negative number
            if (fullHexString.length() >= 2)

                // take only last two characters
                sb.append(fullHexString.substring(fullHexString.length() - 2));

            // if positive and les then 17
            else
                sb.append(0).append(fullHexString);
        }
        return sb.toString();
    }
}
