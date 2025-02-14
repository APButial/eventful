package com.btp.login.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class InitUsersXml {
    public static void init() {
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element root = document.createElement("users");
            document.appendChild(root);

            Element user = document.createElement("user");
            root.appendChild(user);

            Element username = document.createElement("username");
            username.appendChild(document.createTextNode("test"));
            user.appendChild(username);

            Element password =  document.createElement("password");
            password.appendChild(document.createTextNode("test"));
            user.appendChild(password);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("Eventful/dat/users.xml"));
            transformer.transform(domSource, streamResult);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
