package com.btp.login.service;

import com.btp.appfx.model.User;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class ValidateNewUserService {
    public static boolean validate(User user, String confirmPass) {
        NodeList users = ReadUsersService.read();

        if(users == null) {
            InitUsersXml.init();
        } else{
            for (int i = 0; i < users.getLength(); i++) {
                Element tempUser = (Element) users.item(i);
                if(tempUser.getAttribute("username").equals(user.getUsername())) {
                    return false;
                }
            }

            WriteUsersService.write(user);
        }
        return true;
    }

}


