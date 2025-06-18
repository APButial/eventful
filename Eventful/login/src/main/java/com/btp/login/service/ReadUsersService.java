package com.btp.login.service;

import com.btp.appfx.service.XMLCipherService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ReadUsersService {
    public static NodeList read() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            File file = new File("Eventful/dat/users.xml") ;
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            return XMLCipherService.decryptXMLValues(document).getElementsByTagName("user");

        } catch (Exception e) {
            return null;
        }
    }

}
