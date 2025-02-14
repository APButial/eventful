package com.btp.login.service;

import com.btp.appfx.model.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Files;

public class UnmarshalUserService {
    public static void unmarshalUsers() {
        File file = new File("/dat/users.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(Users.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
