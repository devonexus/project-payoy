package com.example.apaodevo.basura_juan.Services;

import android.app.Application;

/**
 * Created by apaodevo on 11/15/2017.
 */

public class GlobalData extends Application {

    private String someVariable;
    private String image_url;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }

    public void setImageUrl(String image_url){
        this.image_url = image_url;
    }
    public String getImageUrl(){
        return image_url;
    }
}