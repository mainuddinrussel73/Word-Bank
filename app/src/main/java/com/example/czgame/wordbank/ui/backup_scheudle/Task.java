package com.example.czgame.wordbank.ui.backup_scheudle;

public class Task {
    String DAY;
    String WEEK;
    String MONTH;
    String YEAR;
    int TIME;
    int ID;

    public Task() {

    }


    public String getDAY() {
        return DAY;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }
    public String getWEEK() {
        return WEEK;
    }

    public void setWEEK(String WEEK) {
        this.WEEK = WEEK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "DAY='" + DAY + '\'' +
                ", WEEK='" + WEEK + '\'' +
                ", MONTH='" + MONTH + '\'' +
                ", YEAR='" + YEAR + '\'' +
                ", TIME=" + TIME +
                ", ID=" + ID +
                '}';
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public int getTIME() {
        return TIME;
    }

    public void setTIME(int TIME) {
        this.TIME = TIME;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
