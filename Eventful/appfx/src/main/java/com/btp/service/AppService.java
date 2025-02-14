package com.btp.service;

public interface AppService {
    // Basic
    void save(String filename);
    void exit();
    void close();

    // Login
    void login();
    void createAccount();
    void logout();

    // Event

}
