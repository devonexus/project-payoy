package com.example.apaodevo.basura_juan.Models;

/**
 * Created by Brylle on 2/5/2018.
 */

public class NotificationModel {
    private String notificationMessage;
    private String notificationDate;
    private String notificationTime;
    private String notificationTitle;

    private int notificationId;
    private int notificationCount;
    public static NotificationModel notificationInstance;
    public static NotificationModel getInstance()
    {
        if(notificationInstance == null)
        {
            return notificationInstance = new NotificationModel();
        }
        return notificationInstance;
    }
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }







    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }




    public NotificationModel(){

    }



    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }




    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }


}
