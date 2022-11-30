import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

//program for generating RSA key pairs which will be stored in privateKey.key and publicKey.pub files in the keys directory

public class GenerateKeyPair {
    public static void main(String[] args) {
        // Using keyPairGenerator class for key generation which takes Algorithm name as
        // parameter
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);

            // generating key pairs by calling generateKeyPair() method on keyPairGenerator
            // object
            KeyPair kp = kpg.generateKeyPair();

            // public key can be accessed by calling getPublic() method //PublicKey pub =
            // kp.getPublic();
            // private key can be accessed by calling getPrivate() method//PrivateKey priv =
            // kp.getPrivate();
            // storing the key pair in the corresponding text files
            try (FileOutputStream out = new FileOutputStream("digital-sign-with-rsa-sha256/Keys/privateKey" + ".key")) {
                out.write(kp.getPrivate().getEncoded());
            }

            try (FileOutputStream out = new FileOutputStream("digital-sign-with-rsa-sha256/Keys/publicKey" + ".pub")) {
                out.write(kp.getPublic().getEncoded());
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }
}
