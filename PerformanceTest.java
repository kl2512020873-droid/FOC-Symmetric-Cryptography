import java.nio.charset.StandardCharsets;

public class PerformanceTest
{
    public static void main(String[] args)
    {
        try
        {
            String key = "biru";

            String text1KB = createTestData(1024);
            String text100KB = createTestData(100 * 1024);
            String text1MB = createTestData(1024 * 1024);

            System.out.println("==============================================");
            System.out.println("       PERFORMANCE TESTING");
            System.out.println("==============================================");

            // Warm-up
            warmUp(text1KB, key);

            System.out.println();
            System.out.println("FILE SIZE: 1 KB");
            testRC4(text1KB, key);
            testAES(text1KB, key);

            System.out.println();
            System.out.println("FILE SIZE: 100 KB");
            testRC4(text100KB, key);
            testAES(text100KB, key);

            System.out.println();
            System.out.println("FILE SIZE: 1 MB");
            testRC4(text1MB, key);
            testAES(text1MB, key);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String createTestData(int size)
    {
        String text = "CONFIDENTIAL 0826 ";
        StringBuilder data = new StringBuilder();

        while (data.length() < size)
        {
            data.append(text);
        }

        return data.substring(0, size);
    }

    public static void warmUp(String plaintext, String key)
        throws Exception
    {
        byte[] data =
            plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] keyBytes =
            key.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < 10; i++)
        {
            StreamCipherRC4.encryptDecrypt(data, keyBytes);

            byte[] encrypted =
                BlockCipherAES.encrypt(plaintext, key);

            BlockCipherAES.decrypt(encrypted, key);
        }
    }

    public static void testRC4(String plaintext, String key)
    {
        byte[] data =
            plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] keyBytes =
            key.getBytes(StandardCharsets.UTF_8);

        int repetitions = 10;

        long totalEncrypt = 0;
        long totalDecrypt = 0;

        byte[] ciphertext = null;
        byte[] decrypted = null;

        for (int i = 0; i < repetitions; i++)
        {
            long startEncrypt = System.nanoTime();

            ciphertext =
                StreamCipherRC4.encryptDecrypt(data, keyBytes);

            long endEncrypt = System.nanoTime();

            totalEncrypt +=
                endEncrypt - startEncrypt;

            long startDecrypt = System.nanoTime();

            decrypted =
                StreamCipherRC4.encryptDecrypt(
                    ciphertext,
                    keyBytes
                );

            long endDecrypt = System.nanoTime();

            totalDecrypt +=
                endDecrypt - startDecrypt;
        }

        double encryptionTime =
            totalEncrypt / (double) repetitions / 1_000_000.0;

        double decryptionTime =
            totalDecrypt / (double) repetitions / 1_000_000.0;

        boolean correct =
            plaintext.equals(
                new String(
                    decrypted,
                    StandardCharsets.UTF_8
                )
            );

        System.out.println("RC4");
        System.out.println(
            "Average Encryption Time : " +
            encryptionTime +
            " ms"
        );

        System.out.println(
            "Average Decryption Time : " +
            decryptionTime +
            " ms"
        );

        System.out.println(
            "Decryption Correct : " +
            correct
        );
    }

    public static void testAES(
        String plaintext,
        String key
    ) throws Exception
    {
        int repetitions = 10;

        long totalEncrypt = 0;
        long totalDecrypt = 0;

        byte[] ciphertext = null;
        String decrypted = null;

        for (int i = 0; i < repetitions; i++)
        {
            long startEncrypt = System.nanoTime();

            ciphertext =
                BlockCipherAES.encrypt(
                    plaintext,
                    key
                );

            long endEncrypt = System.nanoTime();

            totalEncrypt +=
                endEncrypt - startEncrypt;

            long startDecrypt = System.nanoTime();

            decrypted =
                BlockCipherAES.decrypt(
                    ciphertext,
                    key
                );

            long endDecrypt = System.nanoTime();

            totalDecrypt +=
                endDecrypt - startDecrypt;
        }

        double encryptionTime =
            totalEncrypt / (double) repetitions / 1_000_000.0;

        double decryptionTime =
            totalDecrypt / (double) repetitions / 1_000_000.0;

        boolean correct =
            plaintext.equals(decrypted);

        System.out.println("AES");
        System.out.println(
            "Average Encryption Time : " +
            encryptionTime +
            " ms"
        );

        System.out.println(
            "Average Decryption Time : " +
            decryptionTime +
            " ms"
        );

        System.out.println(
            "Decryption Correct : " +
            correct
        );
    }
}
