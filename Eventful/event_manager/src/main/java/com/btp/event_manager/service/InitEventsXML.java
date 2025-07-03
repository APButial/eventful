package com.btp.event_manager.service;

import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.service.AppDataPath;
import com.btp.appfx.service.AppService;
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

public class InitEventsXML {
    public static void init(BaseEvent newEvent, AppService appService) {
        try {
            String path = AppDataPath.loadPath() + "/dat/" + appService.getCurrUser().getUsername();
            File userDir = new File(path);

            if (!userDir.exists()) {
                userDir.mkdirs();
            }

            File file = new File(path + "/events.xml");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element root = document.createElement("events");
            document.appendChild(root);

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
            creator.appendChild(document.createTextNode(appService.getCurrUser().getUsername()));
            event.appendChild(creator);

            Element userkey = document.createElement("key");
            userkey.appendChild(document.createTextNode(appService.getCurrUser().getUserKey()));
            event.appendChild(userkey);

            Element lastAccessed = document.createElement("lastAccessed");
            lastAccessed.appendChild(document.createTextNode(appService.getSysDateTime().toString()));
            event.appendChild(lastAccessed);
            //////////////////////////////////////////////////////////////////////////////////


            document.getDocumentElement().appendChild(event);

            File tempFile = new File(path + "events_temp.xml");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(CipherService.encryptXMLValues(document));
            StreamResult streamResult = new StreamResult(tempFile);
            transformer.transform(domSource, streamResult);

            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);

            if (tempFile.exists()) {
                tempFile.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
