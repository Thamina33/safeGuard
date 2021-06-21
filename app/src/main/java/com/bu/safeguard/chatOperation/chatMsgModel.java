package com.bu.safeguard.chatOperation;

public class chatMsgModel {
    String msg , msg_id , uid  , content_type , content_link    ;
    Long time ;
    String  userName , user_pp ;


    public chatMsgModel() {
    }


    public chatMsgModel(String msg, String msg_id, String uid, String content_type, String content_link, Long time, String userName, String user_pp) {
        this.msg = msg;
        this.msg_id = msg_id;
        this.uid = uid;
        this.content_type = content_type;
        this.content_link = content_link;
        this.time = time;
        this.userName = userName;
        this.user_pp = user_pp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser_pp() {
        return user_pp;
    }

    public void setUser_pp(String user_pp) {
        this.user_pp = user_pp;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent_link() {
        return content_link;
    }

    public void setContent_link(String content_link) {
        this.content_link = content_link;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
