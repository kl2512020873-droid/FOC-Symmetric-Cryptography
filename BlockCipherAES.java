import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class BlockCipherAES
{
    private static SecretKeySpec generateAESKey(String password) throws Exception 
    {
        byte[] key = password.getBytes(StandardCharsets.UTF_8);

        MessageDigest sha = MessageDigest.getInstance("SHA-256");

        key = sha.digest(key);

        key = Arrays.copyOf(key, 16);

        return new SecretKeySpec(key, "AES");
    }

    public static byte[] encrypt(String plaintext, String password) throws Exception 
    {
        SecretKeySpec secretKey = generateAESKey(password);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(
            plaintext.getBytes(StandardCharsets.UTF_8)
        );
    }

    public static String decrypt(byte[] ciphertext, String password) throws Exception 
    {
        SecretKeySpec secretKey = generateAESKey(password);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedBytes = cipher.doFinal(ciphertext);

        return new String(
            decryptedBytes,
            StandardCharsets.UTF_8
        );
    }

    public static String bytesToHex(byte[] bytes) 
    {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) 
        {
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }

    public static void main(String[] args) 
    {
        try 
        {
            java.util.Scanner scanner = new java.util.Scanner(System.in);

            System.out.print("enter your message : ");
            String messageInput = scanner.nextLine();

            System.out.print("enter your secret key : ");
            String keyInput = scanner.nextLine();

            byte[] ciphertext = encrypt(messageInput, keyInput);

            System.out.println("ENCRYPTION");
            System.out.println("Original Message : " + messageInput);
            System.out.println("Ciphertext (Hex) : " + bytesToHex(ciphertext));

            String decryptedText = decrypt(ciphertext, keyInput);

            System.out.println("DECRYPTION");
            System.out.println("Decrypted Message: " + decryptedText);

            scanner.close();
        }
        catch (Exception e) 
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
