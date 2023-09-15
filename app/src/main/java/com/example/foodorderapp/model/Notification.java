package com.example.foodorderapp.model;

import java.util.Date;

public class Notification {
    private String id;
    private  String name;
    private Object infomation;
    private Date date;
    private boolean isClick;

    public Notification(String id, String name, Object infomation, Date date, boolean isClick) {
        this.id = id;
        this.name = name;
        this.infomation = infomation;
        this.date = date;
        this.isClick = isClick;
    }

    public Notification( String name, Object infomation, Date date, boolean isClick) {
        this.name = name;
        this.infomation = infomation;
        this.date = date;
        this.isClick = isClick;
    }

    public Notification() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Object getInfomation() {
        return infomation;
    }

    public void setInfomation(Object infomation) {
        this.infomation = infomation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
