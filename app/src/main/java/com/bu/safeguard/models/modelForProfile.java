package com.bu.safeguard.models;

public class    modelForProfile {
    String fullname , nickName , personalPhone , emerph1 , emerph2  , area , uid , ppLink  ,emerph3  ;
    Double currentLat , currentLong ;


    public modelForProfile() {
    }

    public modelForProfile(String fullname, String nickName, String personalPhone, String emerph1, String emerph2, String area, String uid, String ppLink, String emerph3, Double currentLat, Double currentLong) {
        this.fullname = fullname;
        this.nickName = nickName;
        this.personalPhone = personalPhone;
        this.emerph1 = emerph1;
        this.emerph2 = emerph2;
        this.area = area;
        this.uid = uid;
        this.ppLink = ppLink;
        this.emerph3 = emerph3;
        this.currentLat = currentLat;
        this.currentLong = currentLong;

    }

    public Double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(Double currentLat) {
        this.currentLat = currentLat;
    }

    public Double getCurrentLong() {
        return currentLong;
    }

    public void setCurrentLong(Double currentLong) {
        this.currentLong = currentLong;
    }

    public String getEmerph3() {
        return emerph3;
    }

    public void setEmerph3(String emerph3) {
        this.emerph3 = emerph3;
    }

    public String getPpLink() {
        return ppLink;
    }

    public void setPpLink(String ppLink) {
        this.ppLink = ppLink;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNickName() {
        return nickName;
    }

//    Unable to find explicit activity class {com.bu.safeguard/com.bu.safeguard.chatOperation.chatPage};
//    have you declared this activity in your AndroidManifest.xml? seriouslu toma

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    public String getEmerph1() {
        return emerph1;
    }

    public void setEmerph1(String emerph1) {
        this.emerph1 = emerph1;
    }

    public String getEmerph2() {
        return emerph2;
    }

    public void setEmerph2(String emerph2) {
        this.emerph2 = emerph2;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
