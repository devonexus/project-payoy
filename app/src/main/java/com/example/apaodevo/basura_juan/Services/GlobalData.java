package com.example.apaodevo.basura_juan.Services;

import android.app.Application;
import android.util.StringBuilderPrinter;

/**
 * Created by apaodevo on 11/15/2017.
 */

public class GlobalData extends Application {

    private String someVariable;
    private String image_url;
    private String email_address, username,  password, firstname, login_status, lastname, middleInitial;

    public String getSomeVariable() {

        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }
    public void setEmailAddress(String email_address){
        this.email_address = email_address;
    }
    public void setImageUrl(String image_url){

        this.image_url = image_url;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public void setMiddleInitial(String middleInitial){
        this.middleInitial = middleInitial;
    }

    public String getImageUrl(){
        return image_url;
    }
    public String getEmailAddress(){
        return email_address;
    }
    public String getUsername(){
        return username;
    }
    public String getFirstname(){
        return firstname;
    }
    public String getLastname(){
        return lastname;
    }
    public String getPassword(){
        return password;
    }
    public String getLoginStatus(){
        return login_status;
    }
    public String getMinitial(){
        return middleInitial;
    }
}