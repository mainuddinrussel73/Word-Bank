package com.example.mainuddin.myapplication34.ui.words;

public class word implements Comparable< word >{
    int ID;

    public String getSENTENCE() {
        return SENTENCE;
    }

    public void setSENTENCE(String SENTENCE) {
        this.SENTENCE = SENTENCE;
    }

    String WORD;
    String MEANING;
    String SENTENCE;
    public word(int id, String root, String meaning,String sentence){
        this.ID = id;
        this.WORD = root;
        this.MEANING = meaning;
        this.SENTENCE = sentence;
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

    public String getMEANING() {
        return MEANING;
    }

    public void setMEANING(String MEANING) {
        this.MEANING = MEANING;
    }

    public word(){

    }
    @Override
    public int compareTo(word o) {
        return this.getID() - (o.getID());
    }
}
