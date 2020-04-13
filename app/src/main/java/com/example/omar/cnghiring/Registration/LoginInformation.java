package com.example.omar.cnghiring.Registration;

class LoginInformation {
    String namelogin, nidlogin, emaillogin, pass1login;

    public LoginInformation(String namelogin, String nidlogin, String emaillogin, String pass1login) {
        this.namelogin = namelogin;
        this.nidlogin = nidlogin;
        this.emaillogin = emaillogin;
        this.pass1login = pass1login;
    }

    public String getNamelogin() {
        return namelogin;
    }

    public String getNidlogin() {
        return nidlogin;
    }

    public String getEmaillogin() {
        return emaillogin;
    }


    public String getPass1login() {
        return pass1login;
    }
}
