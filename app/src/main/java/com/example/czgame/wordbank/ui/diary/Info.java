package com.example.czgame.wordbank.ui.diary;

public class Info {
    int type;
    private String id;
    private String subject;
    private String medialink;
    private String description;
    private String dateTime;

    public  Info (String id, String subject,  String description,String medialink, String dateTime) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.medialink = medialink;
        this.dateTime = dateTime;
    }

    public String getMedialink() {
        return medialink;
    }

    public void setMedialink(String medialink) {
        this.medialink = medialink;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    // private int IconImage;

    @Override
    public String toString() {
        return "Info{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", medialink='" + medialink + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}