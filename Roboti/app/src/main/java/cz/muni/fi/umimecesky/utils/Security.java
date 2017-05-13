package cz.muni.fi.umimecesky.utils;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {

    private static final String ENCRYPTED_FILE = "encrypted.txt";

    private static final String AES = "AES/CBC/PKCS5PADDING";
    private static final byte[] PSWD_BYTES = {115, 116, 97, 98, 108, 101, 72, 79, 82, 83, 69, 118, 114, 121, 52, 56, 53, 54, 64, 40, 36, 41, 97, 107, 98, 121, 110, 97, 104, 111, 100, 111};
    private static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(PSWD_BYTES, AES);

    public static BufferedReader getCipheredReader(AssetManager manager) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC, getIv());
        CipherInputStream cis = new CipherInputStream(manager.open(ENCRYPTED_FILE), cipher);

        return new BufferedReader(new InputStreamReader(cis));
    }

    private static IvParameterSpec getIv() {
        SecureRandom random = new SecureRandom();
        byte[] randBytes = new byte[16];
        random.nextBytes(randBytes);
        return new IvParameterSpec(randBytes);
    }

}
