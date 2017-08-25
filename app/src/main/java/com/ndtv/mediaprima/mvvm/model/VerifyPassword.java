package com.ndtv.mediaprima.mvvm.model;

/**
 * Created by nitesh on 2/7/2017.
 */

public class VerifyPassword {

    private String email;
    private String password_reset_code;
    private String new_password;
    private String new_password_confirm;

    public VerifyPassword(String email, String password_reset_code, String new_password, String new_password_confirm) {
        this.email = email;
        this.password_reset_code = password_reset_code;
        this.new_password = new_password;
        this.new_password_confirm = new_password_confirm;
    }
}
