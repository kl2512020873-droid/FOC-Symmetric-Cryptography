public class StreamCipherRC4 
{
    public static int[] ksa(byte[] key) 
    {
        int[] S = new int[256];

        for (int i = 0; i < 256; i++) 
        {
            S[i] = i;
        }

        int j = 0;

        for (int i = 0; i < 256; i++) 
        {
            j = (j + S[i] + (key[i % key.length] & 0xFF)) % 256;

            int temp = S[i];
            S[i] = S[j];
            S[j] = temp;
        }

        return S;
    }

    public static byte[] encryptDecrypt(byte[] data, byte[] key)
    {
        int[] S = ksa(key);

        byte[] result = new byte[data.length];

        int i = 0;
        int j = 0;

        for (int k = 0; k < data.length; k++) 
        {
            i = (i + 1) % 256;
            j = (j + S[i]) % 256;

            int temp = S[i];
            S[i] = S[j];
            S[j] = temp;

            int keystreamByte = S[(S[i] + S[j]) % 256];

            result[k] = (byte) (data[k] ^ keystreamByte);
        }

        return result;
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
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.print("enter your message : ");
        String messageInput = scanner.nextLine();

        System.out.print("enter your secret key : ");
        String keyInput = scanner.nextLine();

        byte[] dataBytes = messageInput.getBytes();
        byte[] keyBytes = keyInput.getBytes();

        byte[] ciphertext = encryptDecrypt(dataBytes, keyBytes);

        System.out.println("ENCRYPTION");
        System.out.println("Original Message : " + messageInput);
        System.out.println("Ciphertext (Hex) : " + bytesToHex(ciphertext));

        byte[] decryptedBytes = encryptDecrypt(ciphertext, keyBytes);

        String decryptedText = new String(decryptedBytes);

        System.out.println("DECRYPTION");
        System.out.println("Decrypted Message: " + decryptedText);

        scanner.close();
    }
}
