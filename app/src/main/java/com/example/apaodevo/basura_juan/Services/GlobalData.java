package com.example.apaodevo.basura_juan.Services;

import android.app.Application;

/**
 * Created by apaodevo on 11/9/2017.
 */

public class GlobalData extends Application {

    public static String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }
}
