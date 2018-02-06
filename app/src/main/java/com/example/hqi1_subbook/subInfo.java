/*
 * subInfo
 *
 *
 * feb 5, 2018
 *
 * Copyright (c) 2018 Haotian Qi. CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and condition of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact me.
 */
package com.example.hqi1_subbook;

/**
 * the class used to handle object for subList
 *
 * @see MainActivity
 * @see objectSubInfo
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
