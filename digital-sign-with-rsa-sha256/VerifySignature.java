import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

//program for verifying the signature using the public key

public class VerifySignature {
    public static void main(String[] args) {
        try {
            String publicKeyPath = "digital-sign-with-rsa-sha256/Recieved/publicKey.pub";
            String signPath = "digital-sign-with-rsa-sha256/Output/dig-sign.txt";
            String decryptionPath = "digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt";
            // input plain text file
            String inputPath = "digital-sign-with-rsa-sha256/Recieved/recieved-input.txt";
            // path for MessageDigest from the input.txt file
            String outputPath = "digital-sign-with-rsa-sha256/Verification/hashed-recieved-input.txt";
            // path for storing the verfication result
            String verficationFilePath = "digital-sign-with-rsa-sha256/Verification/verification-result.txt";
            // method for signature verification
            verifySign(publicKeyPath, signPath, decryptionPath, inputPath, outputPath, verficationFilePath);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }

    // method for signature verification
    public static void verifySign(String pubKeyPath, String signPath, String decryptionPath,
            String recievedFilePath, String hashedFilePath, String verificationresultFilePath) {
        try (FileInputStream in = new FileInputStream(signPath);
                FileOutputStream out = new FileOutputStream(decryptionPath)) {

            // getting the public key from the key file
            byte[] bytes = Files.readAllBytes(Paths.get(pubKeyPath));
            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pub = kf.generatePublic(ks);

            // Cipher class is used to encrypt/ decrypt
            // creating the Cipher object by mentioning RSA as the decryption Algorithm
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pub);

            // decrypting the output file using the cipher object and then decrypted Message
            // Digest is stored in the output file
            processFile(cipher, in, out);

            // generating hash from the input plain text and then storing the hash in the
            // hashed-recieved-input.txt file
            GenerateHash.generateHash(recievedFilePath, hashedFilePath);

            // matching the decrypted message digest and hashed digest and then getting the
            // result as integer value
            int res = matchFiles(decryptionPath, hashedFilePath);
            String verificationResult = "Signature verification falied..!";
            if (res == -1) {
                verificationResult = "Signature verified...!";
            }

            System.out.println(verificationResult);

            // writing the verfication result to the verification-result.txt file
            Files.write(Paths.get(verificationresultFilePath), verificationResult.getBytes());

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
    }

    // method for matching decrypted message digest and hashed digest
    static int matchFiles(String decryptedDigSignPath, String hashedinputPath) throws IOException {

        try (BufferedReader bf1 = Files.newBufferedReader(Paths.get(decryptedDigSignPath));
                BufferedReader bf2 = Files.newBufferedReader(Paths.get(hashedinputPath))) {

            int lineNumber = 1;
            String line1 = "", line2 = "";
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return lineNumber;
                }
                lineNumber++;
            }
            // returning -1 if the both files are same
            if (bf2.readLine() == null) {
                return -1;
            }
            // if different then returning the line number where it differs
            else {
                return lineNumber;
            }
        }
    }

    // method for generating MessageDigest from the DigitalSignature input file
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
