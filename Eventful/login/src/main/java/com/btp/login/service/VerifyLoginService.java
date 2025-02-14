package com.btp.login.service;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VerifyLoginService {
    public static boolean verify(String username, String password) {
        NodeList users = ReadUsersService.read();

        if(users == null) {
            InitUsersXml.init();
        }

        for (int i = 0; i < users.getLength(); i++) {
            Element tempUser = (Element) users.item(i);
            if(tempUser.getElementsByTagName("username").item(0).getTextContent().equals(username)) {
                if (PassHashService.hash(password).equals(tempUser.getElementsByTagName("password").item(0).getTextContent()))
                    return true;
            }
        }
        return  false;
    }
}
