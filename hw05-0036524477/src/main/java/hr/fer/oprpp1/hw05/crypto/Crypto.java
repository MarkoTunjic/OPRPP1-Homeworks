package hr.fer.oprpp1.hw05.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    /** A constant that determines the buffer size for reading from files */
    private static final int BUFF_SIZE = 4000;

    /** A constant that specifies the path to the resources folder */
    private static final String PATH_TO_RESOURCES = "src/main/resources/";

    /**
     * A main method that determines what to do based on the given args. Cheking
     * digest for args[0]="checksha", encrypting for args[0]="enrypt", decrypting
     * for args[0]="decrypt". For checksha args[1] should be a filename for which
     * the digest should be cheked and for encrypt and decrypt args[0] and args[1]
     * are the source and destionation file
     *
     * @param args the arguments for this main method
     */
    public static void main(String[] args) {

        // try gettings arguments and excetuing commands
        try {
            // get mode
            String mode = args[0];

            // determine which mode and calling corresponding function
            if (mode.equals("checksha")) {
                checkSha(args[1]);
            } else if (mode.equals("encrypt")) {
                cipher(true, args[1], args[2]);
            } else if (mode.equals("decrypt")) {
                cipher(false, args[1], args[2]);
            } else {
                System.out.println("Invalid mode was given!");
            }
        }
        // a exception from the called functions
        catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
        // if not enough arguments were given
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Invalid number of arguments given!");
        }

    }

    private static void cipher(boolean encrypt, String inputFileName, String outputFileName) {
        try (Scanner sc = new Scanner(System.in);
                InputStream is = Files.newInputStream(Path.of(PATH_TO_RESOURCES + inputFileName));
                OutputStream os = Files.newOutputStream(Path.of(PATH_TO_RESOURCES + outputFileName))) {

            // print message
            System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");

            // read user input for password
            String keyText = sc.nextLine();
            if (keyText.length() != 32)
                throw new IllegalArgumentException("Key must be of lenght 16 bytes aka 32 hex-digits!");

            // print message
            System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");

            // read user input for IV
            String ivText = sc.nextLine();
            if (ivText.length() != 32)
                throw new IllegalArgumentException("IV must be of lenght 16 bytes aka 32 hex-digits!");

            // genereate keyspec from password
            SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");

            // generate parameter spec with given IV
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));

            // initialize cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

            // the number of bytes that were read from the file
            int numOfReadBytes = 0;

            do {
                // create buffer
                byte[] buff = new byte[Crypto.BUFF_SIZE];

                // read file
                numOfReadBytes = is.read(buff);

                // encrypt/decrypt data from input stream
                byte[] result = cipher.update(buff, 0, numOfReadBytes == -1 ? 0 : numOfReadBytes);

                // write result to output stream or nothing if empty
                os.write(result == null ? new byte[] {} : result);

            } while (numOfReadBytes != -1);

            // print success
            System.out.println(String.format("%s completed. Generated file %s based on file %s.",
                    encrypt ? "Encryption" : "Decryption", outputFileName, inputFileName));
        }
        // input stream, output stream or scanner
        catch (IOException e) {
            System.out.println("Could not open file");
        }
        // cipher instance
        catch (NoSuchAlgorithmException ex) {
            System.out.println("Invalid algorithm for encrypting/decrypting");
        }
        // cipher instance
        catch (NoSuchPaddingException ex) {
            System.out.println("Invalid padding for encrypting/decrypting");
        }
        // cipher init
        catch (InvalidKeyException e) {
            System.out.println("Invalid key for encrypting/decrypting");
        }
        // cipher init
        catch (InvalidAlgorithmParameterException e) {
            System.out.println("Invalid algorith parameters for encrypting/decrypting");
        }

    }

    /**
     * A method that gets user input which represents the expected digest and
     * compares that digest to the real provided files digest and prints a
     * corresponding message. Thros a {@link IllegalArgumentException} if invalid
     * digest was inputted
     *
     * @param fileName the file for which the digest should be checked
     *
     * @throws IllegalArgumentException if invalid digest was inputted
     */
    private static void checkSha(String fileName) {
        // scanner for getting user input and input stream for reading bytes from file
        try (InputStream is = Files.newInputStream(Path.of(PATH_TO_RESOURCES + fileName));
                Scanner sc = new Scanner(System.in)) {

            // print message
            System.out.println(String.format("Please provide expected sha-256 digest for %s:", fileName));

            // read user input
            String expectedSHA256Digest = sc.nextLine();

            // initialize MessageDigest
            MessageDigest sha = MessageDigest.getInstance("SHA-256");

            // the number of bytes that were read from the file
            int numOfReadBytes = 0;

            do {
                // create buffer
                byte[] buff = new byte[Crypto.BUFF_SIZE];

                // read file
                numOfReadBytes = is.read(buff);

                // update digest
                sha.update(buff, 0, numOfReadBytes == -1 ? 0 : numOfReadBytes);
            } while (numOfReadBytes != -1);

            // calculate hex values for bytes
            byte[] hash = sha.digest();
            String calculatedSHA256Digest = Util.bytetohex(hash);

            // compare and print message
            if (!expectedSHA256Digest.equals(calculatedSHA256Digest)) {
                System.out.println(
                        String.format(
                                "Digesting completed. Digest of %s does not match the expected digest. Digest was: %s",
                                fileName, calculatedSHA256Digest));
                return;
            }

            System.out.println(String.format("Digesting completed. Digest of %s matches expected digest.", fileName));
        }
        // if scanner or input string could not be opened
        catch (IOException ex) {
            System.out.println("Could nit open file: " + fileName);
        }
        // if invalid algorithm for digesting was given
        catch (NoSuchAlgorithmException ex) {
            System.out.println("Invalid algorithm for digest was given");
        }
    }
}
