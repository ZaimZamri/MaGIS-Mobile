package com.example.zaimzamrii.psmmasjid.JB;

public class ActivityJB {

    private String name;
    private String picture;
    private String activityName;
    private String activityDate;
    private String activityPlace;
    private String location;
    private String time;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public ActivityJB(){

    }

    public ActivityJB(String name, String picture, String activityName, String activityDate, String activityPlace){
        this.name = name;
        this.picture = picture;
        this.activityName = activityName;
        this.activityDate = activityDate;
        this.activityPlace = activityPlace;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityPlace() {
        return activityPlace;
    }

    public void setActivityPlace(String activityPlace) {
        this.activityPlace = activityPlace;
    }






}
