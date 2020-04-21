package com.littleit.whatsappclone.model;

public class Chatlist {
    private String userID;
    private String userName;
    private String description;
    private String date;
    private String urlProfile;

    public Chatlist() {
    }

    public Chatlist(String userID, String userName, String description, String date, String urlProfile) {
        this.userID = userID;
        this.userName = userName;
        this.description = description;
        this.date = date;
        this.urlProfile = urlProfile;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }
}
