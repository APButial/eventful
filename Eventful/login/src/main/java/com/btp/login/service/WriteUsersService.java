package com.btp.login.service;

import com.btp.appfx.model.User;
import com.btp.appfx.service.CipherService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class WriteUsersService {
    public static void write(User newUser) {
        try {
            File file = new File("Eventful - Event Management System/dat/users.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            Element user = document.createElement("user");
            Element username = document.createElement("username");
            username.appendChild(document.createTextNode(newUser.getUsername()));
            user.appendChild(username);

            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(newUser.getPassword()));
            user.appendChild(password);

            document.getDocumentElement().appendChild(user);

            File tempFile = new File("Eventful - Event Management System/dat/users_temp.xml");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(CipherService.encryptXMLValues(document));
            StreamResult streamResult = new StreamResult(tempFile);
            transformer.transform(domSource, streamResult);

            // Replace the original file with the encrypted temporary file
            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);

            if (tempFile.exists()) {
                tempFile.delete();
            }

            System.out.println("New user added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
