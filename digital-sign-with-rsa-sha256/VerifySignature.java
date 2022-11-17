import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;

import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class VerifySignature {
    public static void main(String[] args) {
        try {
            String publicKeyPath="digital-sign-with-rsa-sha256/Recieved/publicKey.pub";
            String signPath="digital-sign-with-rsa-sha256/Output/dig-sign.txt";
            String decryptionPath="digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt";
            String inputPath="digital-sign-with-rsa-sha256/Recieved/recieved-input.txt";
            String outputPath="digital-sign-with-rsa-sha256/Verification/hashed-recieved-input.txt";
            String verficationFilePath = "digital-sign-with-rsa-sha256/Verification/verification-result.txt";
            verifySign(publicKeyPath,signPath,decryptionPath,inputPath,outputPath,verficationFilePath);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
       
    }
    public static void verifySign(String pubKeyPath, String signPath, String decryptionPath,
    String recievedFilePath, String hashedFilePath, String verificationresultFilePath){
        try(FileInputStream in = new FileInputStream(signPath);
        FileOutputStream out = new FileOutputStream(decryptionPath)) {
            byte[] bytes = Files.readAllBytes(Paths.get(pubKeyPath));
            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pub = kf.generatePublic(ks);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pub);
            
            processFile(cipher, in, out);

            
            GenerateHash.generateHash(recievedFilePath, hashedFilePath);

            int res = matchFiles(decryptionPath,hashedFilePath);
            String verificationResult="Signature verification falied..!";
            if(res==-1){
                verificationResult="Signature verified...!";     
            }

            System.out.println(verificationResult);

            Files.write(Paths.get(verificationresultFilePath), verificationResult.getBytes());
 
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
    }

    static int matchFiles(String decryptedDigSignPath, String hashedinputPath) throws IOException{
        //boolean isMatching = false; 
        //File inputFile = new File("digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt");
        //File outputFile = new File("digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt");
        
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
        if (bf2.readLine() == null) {
            return -1;
        }
        else {
            return lineNumber;
        }
    }
    }

    static private void processFile(Cipher ci,InputStream in,OutputStream out)
        throws javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException
        {
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = ci.update(ibuf, 0, len);
                if ( obuf != null ) out.write(obuf);
            }
            byte[] obuf = ci.doFinal();
            if ( obuf != null ) out.write(obuf);
        }
}
