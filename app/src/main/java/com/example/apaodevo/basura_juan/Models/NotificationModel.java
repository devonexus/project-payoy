package com.example.apaodevo.basura_juan.Models;

/**
 * Created by Brylle on 2/5/2018.
 */

public class NotificationModel {
    private String notificationMessage;
    private String notificationDate;

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

    private String notificationTime;
}
