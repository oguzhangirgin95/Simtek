package com.example.backend;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CryptologyService {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private final SecretKeySpec key;

    private final IvParameterSpec iv;

    public CryptologyService(@Value("${app.crypto.key}") String cryptoKey) {

        byte[] bytes = cryptoKey == null ? new byte[0] : cryptoKey.getBytes(StandardCharsets.UTF_8);

        if (bytes.length != 32) {
            throw new IllegalStateException("app.crypto.key 32 karakter olmali. Ortam dosyasini kontrol edin.");
        }

        this.key = new SecretKeySpec(bytes, "AES");

        this.iv = new IvParameterSpec(Arrays.copyOf(bytes, 16));
    }

    public String encryption(String value) {

        if (value == null || value.isEmpty()) {
            return "";
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception exception) {
            throw new IllegalStateException("Sifreleme yapilamadi.", exception);
        }
    }

    public String decryption(String value) {

        if (value == null || value.isEmpty()) {
            return "";
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            return new String(cipher.doFinal(Base64.getDecoder().decode(value)), StandardCharsets.UTF_8);

        } catch (Exception exception) {
            return "";
        }
    }
}
