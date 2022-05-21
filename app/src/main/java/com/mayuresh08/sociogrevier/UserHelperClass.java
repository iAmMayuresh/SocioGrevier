package com.mayuresh08.sociogrevier;

public class UserHelperClass {

    String email,password,conf_pass;

    public UserHelperClass() {

    }



    public UserHelperClass(String email,String conf_pass, String password) {
        this.email = email;
        this.password = password;
        this.conf_pass = conf_pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConf_pass() {
        return conf_pass;
    }

    public void setConf_pass(String conf_pass) {
        this.conf_pass = conf_pass;
    }
}
