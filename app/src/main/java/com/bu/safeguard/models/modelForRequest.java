package com.bu.safeguard.models;

public class modelForRequest {
    String  postid  , name  , number , uid , lon , lat , time , sosImage, sosMsg  , userImage ;

    public modelForRequest() {
    }

    public modelForRequest(String postid, String name, String number, String uid, String lon, String lat, String time, String sosImage, String sosMsg, String userImage) {
        this.postid = postid;
        this.name = name;
        this.number = number;
        this.uid = uid;
        this.lon = lon;
        this.lat = lat;
        this.time = time;
        this.sosImage = sosImage;
        this.sosMsg = sosMsg;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getSosImage() {
        return sosImage;
    }

    public void setSosImage(String sosImage) {
        this.sosImage = sosImage;
    }

    public String getSosMsg() {
        return sosMsg;
    }

    public void setSosMsg(String sosMsg) {
        this.sosMsg = sosMsg;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
