package com.example.czgame.wordbank.ui.backup_scheudle;

public class Task_Detail {

    int ID;
    String TITLE;
    int NUM;
    int COMPLETED;
    int ISREPEAT;
    String DUE_DATE;
    String START_TIME;
    String END_TIME;

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    @Override
    public String toString() {
        return "Task_Detail{" +
                "ID=" + ID +
                ", TITLE='" + TITLE + '\'' +
                ", NUM=" + NUM +
                ", COMPLETED=" + COMPLETED +
                ", ISREPEAT=" + ISREPEAT +
                ", DUE_DATE='" + DUE_DATE + '\'' +
                ", START_TIME='" + START_TIME + '\'' +
                ", END_TIME='" + END_TIME + '\'' +
                '}';
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }


    public Task_Detail(int ID, String TITLE, int NUM, int COMPLETED, int ISREPEAT, String DUE_DATE) {
        this.ID = ID;
        this.TITLE = TITLE;
        this.NUM = NUM;
        this.COMPLETED = COMPLETED;
        this.ISREPEAT = ISREPEAT;
        this.DUE_DATE = DUE_DATE;
    }


    public Task_Detail() {

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public String getTitle() {
        return TITLE;
    }

    public void setTitle(String title) {
        this.TITLE = title;
    }


    public int getNum_of_promotodo() {
        return NUM;
    }

    public void setNum_of_promotodo(int num_of_promotodo) {
        this.NUM = num_of_promotodo;
    }

    public int getCompleted_promotodo() {
        return COMPLETED;
    }

    public void setCompleted_promotodo(int completed_promotodo) {
        this.COMPLETED = completed_promotodo;
    }

    public int isIsrepeat() {
        return ISREPEAT;
    }

    public void setIsrepeat(int isrepeat) {
        this.ISREPEAT = isrepeat;
    }


    public String getDue_date() {
        return DUE_DATE;
    }

    public void setDue_date(String due_date) {
        this.DUE_DATE = due_date;
    }



}
