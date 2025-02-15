package com.btp.event_manager.service;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class WriteEventsService {
    public static void write(String eventCreator, BaseEvent newEvent, AppService appService) {
        try {
            File file = new File("Eventful/dat/" + eventCreator + ".xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            Element event = document.createElement("event");

            // mandatory
            Element title = document.createElement("title");
            title.appendChild(document.createTextNode(newEvent.getEventName()));
            event.appendChild(title);

            Element startDate = document.createElement("startDate");
            startDate.appendChild(document.createTextNode(newEvent.getStartDate().toString()));
            event.appendChild(startDate);

            Element endDate = document.createElement("endDate");
            endDate.appendChild(document.createTextNode(newEvent.getEndDate().toString()));
            event.appendChild(endDate);
            //////////////////////////////////////////////////////////////////////////////////

            // metadata
            Element creator = document.createElement("creator");
            creator.appendChild(document.createTextNode(eventCreator));
            event.appendChild(creator);

            Element lastAccessed = document.createElement("lastAccessed");
            lastAccessed.appendChild(document.createTextNode(appService.getSysDateTime().toString()));
            event.appendChild(lastAccessed);
            //////////////////////////////////////////////////////////////////////////////////


            document.getDocumentElement().appendChild(event);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource,streamResult);
            System.out.println("New event added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
