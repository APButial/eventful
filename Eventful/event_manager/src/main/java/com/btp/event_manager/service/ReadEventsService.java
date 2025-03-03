package com.btp.event_manager.service;

import com.btp.appfx.service.AppService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ReadEventsService {
    public static NodeList read(AppService appService) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            File file = new File("Eventful/dat/" + appService.getCurrUser().getUsername() + "/events.xml") ;
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();
            System.out.printf("Events read");
            return document.getElementsByTagName("event");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
