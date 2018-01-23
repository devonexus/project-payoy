package com.example.apaodevo.basura_juan.Services;

import android.app.Application;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;

/**
 * Created by apaodevo on 11/15/2017.
 */

public class GlobalData extends Application {
    private String       fullname;
    private String       image_url;
    private String       userid,email_address, username,  password, firstname, login_status, lastname, middleInitial;
    private String       latitude, longitude;
    public String        address  = "";
    public String        name;

    public String getFullname() {
        return fullname;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getUserid() {
        return userid;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    // fast way to call Toast
    public void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify
                .with(new FontAwesomeModule());
    }
}