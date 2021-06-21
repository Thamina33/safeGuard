package com.bu.safeguard.models;

public class groupFriendModel {
    String userId = "";
    boolean idSelected = false;


    public groupFriendModel(String userId, boolean idSelected) {
        this.userId = userId;
        this.idSelected = idSelected;
    }

    public groupFriendModel(boolean idSelected) {
        this.idSelected = idSelected;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isIdSelected() {
        return idSelected;
    }

    public void setIdSelected(boolean idSelected) {
        this.idSelected = idSelected;
    }



}
