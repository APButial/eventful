package com.btp.login.service;

import com.btp.appfx.model.User;
import com.btp.appfx.service.AppDataPath;
import com.btp.appfx.service.CipherService;
import com.btp.appfx.service.UserKeyGenerator;
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
            String datDirPath = AppDataPath.loadPath() + "/dat";
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

            Element username = document.createElement("username");
            username.appendChild(document.createTextNode(newUser.getUsername()));
            user.appendChild(username);

            Element password =  document.createElement("password");
            password.appendChild(document.createTextNode(newUser.getPassword()));
            user.appendChild(password);

            Element userKey = document.createElement("key");
            userKey.appendChild(document.createTextNode(UserKeyGenerator.getKey()));
            user.appendChild(userKey);

            root.appendChild(user);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(CipherService.encryptXMLValues(document));
            StreamResult streamResult = new StreamResult(new File(AppDataPath.loadPath() + "/dat/users.xml"));
            transformer.transform(domSource, streamResult);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
