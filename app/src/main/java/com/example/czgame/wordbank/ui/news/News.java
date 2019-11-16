package com.example.czgame.wordbank.ui.news;

import java.util.ArrayList;

public class News extends ArrayList<News> implements Comparable<News> {
    int ID;


    String TITLE;
    String BODY;
    String URL;

    public News(int id, String title, String body,String url) {
        this.ID = id;
        this.TITLE = title;
        this.URL = url;
        this.BODY = body;

    }

    public News() {

    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
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

    @Override
    public int compareTo(News o) {
        return this.getID() - (o.getID());
    }

}
