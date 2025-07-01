package com.btp.appfx.service;
// Java program to demonstrate the creation
// of Encryption and Decryption with Java AES
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherService {
    // Class private variables
    private static final String SECRET_KEY
            = "MySecretKey";

    private static final String SALT = "MySalt";

    // Method to encrypt all values in the XML document
    public static Document encryptXMLValues(Document doc) {
        traverseAndEncrypt(doc.getDocumentElement());
        return doc;
    }

    // Recursive method to traverse nodes and encrypt values
    private static void traverseAndEncrypt(Node node) {
        // If the node is an element node
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            // Encrypt the text content of the node
            if (node.hasChildNodes()) {
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node childNode = childNodes.item(i);
                    // If the child node is a text node, encrypt its value
                    if (childNode.getNodeType() == Node.TEXT_NODE) {
                        String originalValue = childNode.getNodeValue();
                        String encryptedValue = encrypt(originalValue);
                        childNode.setNodeValue(encryptedValue);
                    } else {
                        // Recursively traverse child elements
                        traverseAndEncrypt(childNode);
                    }
                }
            }
        }
    }

    // This method use to encrypt to string
    public static String encrypt(String value)
    {
        try {
            // Create default byte array
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec
                    = new IvParameterSpec(iv);

            // Create SecretKeyFactory object
            SecretKeyFactory factory
                    = SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA256");

            // Create KeySpec object and assign with
            // constructor
            KeySpec spec = new PBEKeySpec(
                    SECRET_KEY.toCharArray(), SALT.getBytes(),
                    65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(
                    tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance(
                    "AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,
                    ivspec);
            // Return encrypted string
            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(value.getBytes(
                            StandardCharsets.UTF_8)));
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: "
                    + e.toString());
            return  null;
        }
    }

    // Method to decrypt all values in the XML document
    public static Document decryptXMLValues(Document doc) {
        traverseAndDecrypt(doc.getDocumentElement());
        return doc;
    }

    // Recursive method to traverse nodes and decrypt values
    private static void traverseAndDecrypt(Node node) {
        // If the node is an element node
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            // Decrypt the text content of the node
            if (node.hasChildNodes()) {
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node childNode = childNodes.item(i);
                    // If the child node is a text node, decrypt its value
                    if (childNode.getNodeType() == Node.TEXT_NODE) {
                        String encryptedValue = childNode.getNodeValue();
                        String decryptedValue = decrypt(encryptedValue);
                        childNode.setNodeValue(decryptedValue);
                    } else {
                        // Recursively traverse child elements
                        traverseAndDecrypt(childNode);
                    }
                }
            }
        }
    }

    // This method use to decrypt to string
    public static String decrypt(String strToDecrypt)
    {
        try {

            // Default byte array
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0 };
            // Create IvParameterSpec object and assign with
            // constructor
            IvParameterSpec ivspec
                    = new IvParameterSpec(iv);

            // Create SecretKeyFactory Object
            SecretKeyFactory factory
                    = SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA256");

            // Create KeySpec object and assign with
            // constructor
            KeySpec spec = new PBEKeySpec(
                    SECRET_KEY.toCharArray(), SALT.getBytes(),
                    65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(
                    tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance(
                    "AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey,
                    ivspec);
            // Return decrypted string
            return new String(cipher.doFinal(
                    Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: "
                    + e.toString());
        }
        return null;
    }
}