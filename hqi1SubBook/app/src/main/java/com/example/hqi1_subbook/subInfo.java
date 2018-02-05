package com.example.hqi1_subbook;

/**
 * Created by kq on 2/5/2018.
 */

public class subInfo {
    private String name;
    private String date;
    private double charge;
    private String comment;
    private String info;

    subInfo(String info){
        this.info = info;
    }

    subInfo(String name, String date, double charge, String comment){
        this.name = name;
        this.date = date;
        this.charge = charge;
        this.comment = comment;
    }

    public String getName(){ return name; }
    public String getDate(){ return date; }
    public double getCharge(){ return charge; }
    public String getComment(){ return comment; }
    public String getInfo(){ return info; }

    public void setName(String name){
            this.name = name;
    }

    public void setComment(String comment) {
            this.comment = comment;
    }

    public String toString(){ return info; }
}
