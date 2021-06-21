package com.bu.safeguard.models;

import java.util.ArrayList;
import java.util.List;

public class GroupListModel {
    String id , name  ;
    List<String> userList  = new ArrayList<>() ;

    public GroupListModel() {
    }

    public GroupListModel(String id, String name, List<String> userList) {
        this.id = id;
        this.name = name;
        this.userList = userList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}
