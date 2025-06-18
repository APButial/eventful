package com.btp.login.service;

import com.btp.appfx.model.User;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ValidateNewUserService {
    public static boolean validate(User user) {
        NodeList users = ReadUsersService.read();

        if(users == null) {
            InitUsersXml.init(user);
        } else{
            for (int i = 0; i < users.getLength(); i++) {
                Element tempUser = (Element) users.item(i);
                if(user.getUsername().equals(tempUser.getElementsByTagName("username").item(0).getTextContent())) {
                    return false;
                }
            }

            user.setPassword(user.getPassword());
            WriteUsersService.write(user);
        }
        return true;
    }

}


