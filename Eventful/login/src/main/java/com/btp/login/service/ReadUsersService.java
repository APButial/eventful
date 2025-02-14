package com.btp.login.service;

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

            Element root = document.getDocumentElement();
            System.out.printf("Users read");
            return document.getElementsByTagName("user");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
