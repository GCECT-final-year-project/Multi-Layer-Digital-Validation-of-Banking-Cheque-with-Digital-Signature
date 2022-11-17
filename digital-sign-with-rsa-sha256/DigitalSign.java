import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

public class DigitalSign {
    public static void main(String[] args) {
        try{
            GenerateHash.generateHash("digital-sign-with-rsa-sha256/Input/input.txt", "digital-sign-with-rsa-sha256/Input/hashed-input.txt");
            FileInputStream in = new FileInputStream("digital-sign-with-rsa-sha256/Input/hashed-input.txt");
            FileOutputStream out = new FileOutputStream("digital-sign-with-rsa-sha256/Output/dig-sign.txt");
            signFile("digital-sign-with-rsa-sha256/Keys/privateKey.key", in, out);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        
       
    }
    public static void signFile(String keyPath, FileInputStream inputFile, FileOutputStream outputFile){
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(keyPath));
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pvt = kf.generatePrivate(ks);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pvt);

            
            processFile(cipher, inputFile, outputFile);
            System.out.println("done");
        
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
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
