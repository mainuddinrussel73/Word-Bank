package com.example.mainuddin.myapplication34.ui.news;

public class News {
    int ID;


    String TITLE;
    String BODY;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    String URL;

    public News(int id, String title, String body){
        this.ID = id;
        this.TITLE = title;
        this.BODY = body;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getBODY() {
        return BODY;
    }

    public void setBODY(String BODY) {
        this.BODY = BODY;
    }

    public News(){

    }

}
