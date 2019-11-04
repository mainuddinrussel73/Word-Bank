package com.example.czgame.wordbank.ui.words.ui.main;

public class sentence {
    int ID;
    String WORD;
    String SENTENCE;
    public sentence(int id, String root, String sentence) {
        this.ID = id;
        this.WORD = root;
        this.SENTENCE = sentence;
    }
    public sentence() {

    }

    public String getSENTENCE() {
        return SENTENCE;
    }

    public void setSENTENCE(String SENTENCE) {
        this.SENTENCE = SENTENCE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getWORD() {
        return WORD;
    }

    public void setWORD(String WORD) {
        this.WORD = WORD;
    }

}
