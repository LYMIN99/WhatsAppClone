package com.littleit.whatsappclone.model;

public class StatusModel {
    private String id;
    private String userID;
    private String createdDate;
    private String imageStatus;
    private String textStatus;
    private String viewCount;

    public StatusModel() {
    }

    public StatusModel(String id, String userID, String createdDate, String imageStatus, String textStatus, String viewCount) {
        this.id = id;
        this.userID = userID;
        this.createdDate = createdDate;
        this.imageStatus = imageStatus;
        this.textStatus = textStatus;
        this.viewCount = viewCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getTextStatus() {
        return textStatus;
    }

    public void setTextStatus(String textStatus) {
        this.textStatus = textStatus;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }
}
