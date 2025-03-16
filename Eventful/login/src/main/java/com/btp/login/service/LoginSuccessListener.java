package com.btp.login.service;

public interface LoginSuccessListener {
    void onLoginSuccess();
    void returnLogin() throws Exception;
}
