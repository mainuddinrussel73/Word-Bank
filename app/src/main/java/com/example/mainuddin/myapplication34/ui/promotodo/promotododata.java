package com.example.mainuddin.myapplication34.ui.promotodo;

public class promotododata {


    int ID;
    String TITLE;
    int NUM;
    int  COMPLETED;
    int ISREPEAT;
    String DUE_DATE;



    public promotododata(int ID, String TITLE, int NUM, int COMPLETED, int ISREPEAT, String DUE_DATE) {
        this.ID = ID;
        this.TITLE = TITLE;
        this.NUM = NUM;
        this.COMPLETED = COMPLETED;
        this.ISREPEAT = ISREPEAT;
        this.DUE_DATE = DUE_DATE;
    }


    public promotododata() {

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
