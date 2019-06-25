package com.example.zaimzamrii.psmmasjid.sqlite;

public class NotesDBModel {


    private String title;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public NotesDBModel(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public NotesDBModel() {
    }
}
