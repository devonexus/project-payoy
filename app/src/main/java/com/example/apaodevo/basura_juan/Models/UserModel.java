package com.example.apaodevo.basura_juan.Models;

/**
 * Created by Brylle on 2/11/2018.
 */

public class UserModel {
    private int userId;
    public static UserModel userInstance;
    public static UserModel getInstance()
    {
        if(userInstance == null)
        {
            return userInstance = new UserModel();
        }
        return userInstance;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }





}
