package com.btp.login.service;

import com.btp.appfx.model.User;
import com.btp.appfx.service.XMLCipherService;
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
    public static void init(User newUser) {
        try{
            String datDirPath = "Eventful/dat";
            File datDir = new File(datDirPath);

            if (!datDir.exists()) {
                datDir.mkdirs();
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element root = document.createElement("users");
            document.appendChild(root);

            Element user = document.createElement("user");
            root.appendChild(user);

            Element username = document.createElement("username");
            username.appendChild(document.createTextNode(newUser.getUsername()));
            user.appendChild(username);

            Element password =  document.createElement("password");
            password.appendChild(document.createTextNode(newUser.getPassword()));
            user.appendChild(password);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(XMLCipherService.encryptXMLValues(document));
            StreamResult streamResult = new StreamResult(new File("Eventful/dat/users.xml"));
            transformer.transform(domSource, streamResult);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
