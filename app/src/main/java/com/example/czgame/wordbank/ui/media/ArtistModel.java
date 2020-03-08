package com.example.czgame.wordbank.ui.media;

public class ArtistModel {

    private long id;
    private String artistName;
    private String nr_of_songs;
    private String url;

    ArtistModel(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getID(){
        return id;
    }



    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


    public String getNr_of_songs() {
        return nr_of_songs;
    }

    public void setNr_of_songs(String  nr_of_songs) {
        this.nr_of_songs = nr_of_songs;
    }
}
