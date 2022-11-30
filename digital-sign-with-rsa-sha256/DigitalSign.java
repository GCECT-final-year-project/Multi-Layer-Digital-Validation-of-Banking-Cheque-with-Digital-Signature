import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

//program for generating the digital signature form the input.txt file by using private key

public class DigitalSign {
    public static void main(String[] args) {
        try {
            // hashing input.txt file and storing the MessageDigest in the hashedInput.txt
            // file inside input directory
            GenerateHash.generateHash("digital-sign-with-rsa-sha256/Input/input.txt",
                    "digital-sign-with-rsa-sha256/Input/hashed-input.txt");

            // MessageDigest file input stream object
            FileInputStream in = new FileInputStream("digital-sign-with-rsa-sha256/Input/hashed-input.txt");

            // DigitalSignature (encrypted Message Digest) file output stream object
            FileOutputStream out = new FileOutputStream("digital-sign-with-rsa-sha256/Output/dig-sign.txt");

            // Encrypting Message Digest with RSA private key
            signFile("digital-sign-with-rsa-sha256/Keys/privateKey.key", in, out);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }

    // method for generating the signature
    public static void signFile(String keyPath, FileInputStream inputFile, FileOutputStream outputFile) {
        try {
            // getting the private key as a array of bytes
            byte[] bytes = Files.readAllBytes(Paths.get(keyPath));

            // getting the private key for encryption
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pvt = kf.generatePrivate(ks);

            // Cipher class is used to encrypt/ decrypt
            // creating the Cipher object by mentioning RSA as the encryption Algorithm
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pvt);

            // encrypting the input file using the cipher object and then encrypted data is
            // stored in the output file
            processFile(cipher, inputFile, outputFile);
            System.out.println("done");

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
    }

    // method for generating DigitalSignature from the MessageDigest input file
    static private void processFile(Cipher ci, InputStream in, OutputStream out)
            throws javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException {
        byte[] ibuf = new byte[1024];
        int len;
        while ((len = in.read(ibuf)) != -1) {
            byte[] obuf = ci.update(ibuf, 0, len);
            if (obuf != null)
                out.write(obuf);
        }
        byte[] obuf = ci.doFinal();
        if (obuf != null)
            out.write(obuf);
    }
}
