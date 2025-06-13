package com.example.spring_play_ground;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public final class AesEncrypter {

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AUTH_TAG_LENGTH = 128;
    private static final Charset CHARSET = StandardCharsets.UTF_8;



    
    public String encrypt(String data, String encryptionKey) throws Exception {
        if (data.isEmpty() || encryptionKey.isEmpty()) {
            throw new Exception("Invalid data or encryption key supplied");
        }

        try {
            final Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

            String nonce = getIV();
            log.info("--- {}", nonce);

            byte[] iv = nonce.getBytes();

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(encryptionKey), new GCMParameterSpec(AUTH_TAG_LENGTH, iv));

            byte[] encryptedDataBytes = cipher.doFinal(data.getBytes(CHARSET));


            String encryptedData = Base64.getEncoder().encodeToString(encryptedDataBytes);
            log.info("Encrypted data {}", encryptedData);
            String builder = nonce + ":" + encryptedData;

            return Base64.getEncoder().encodeToString(builder.getBytes(CHARSET));

        } catch (Exception e) {
            throw new Exception("An error occurred while encrypting data", e);
        }
    }

    private String getIV() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    
    public String decrypt(String data, String encryptionKey) throws Exception {
        if (data.isEmpty() || encryptionKey.isEmpty()) {
            throw new Exception("Invalid data or encryption key supplied");
        }

        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);


            String combinedData = new String(Base64.getDecoder().decode((data)));

            int lastIndexOfColon = combinedData.lastIndexOf(':');

            String nonce = combinedData.substring(0, lastIndexOfColon);

            String encryptedDataString = combinedData.substring(lastIndexOfColon + 1);

            byte[] iv = nonce.getBytes();

            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(encryptionKey), new GCMParameterSpec(AUTH_TAG_LENGTH, iv));


            byte[] encryptedData = Base64.getDecoder().decode(encryptedDataString);
                                    encryptedDataString.getBytes();

            byte[] decryptedDataBytes = cipher.doFinal(encryptedData);

            return new String(decryptedDataBytes);
        } catch (Exception e) {
            throw new Exception("An error occurred while decrypting data", e);
        }
    }



    private SecretKeySpec getSecretKeySpec(String encryptionKey) {
        return new SecretKeySpec(encryptionKey.getBytes(CHARSET), "AES");
    }

    public static String encryptEcb(String data, String encryptionKey) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, toSecretKey(encryptionKey));
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            log.info("An error occurred while encrypting data", e);
        }
        return null;
    }

    private static SecretKey toSecretKey(String encodedKey) {
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        // rebuild key using SecretKeySpec
        return new SecretKeySpec(decodedKey, "AES");
    }

    public static String generateEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // 256-bit key
        SecretKey originalKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(originalKey.getEncoded());
    }
    public String decryptECB(String signature, String encodedKey) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, toSecretKey(encodedKey));
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(signature));
            return new String(plainText);
        } catch (Exception e) {
            log.error("An error occurred while decrypting data, ",e);
            throw new Exception("An error occurred while decrypting data");
        }
    }



}