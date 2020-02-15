package com.example.czgame.wordbank.ui.media;

public class AlbumModel {

    private long id;
    private String albumName;
    private String artistName;
    private String nr_of_songs;
    private String albumImg;

    AlbumModel(){

    }


    public void setId(long id) {
        this.id = id;
    }

    public long getID(){
        return id;
    }

    public String getAlbumName(){
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(String albumImg) {
        this.albumImg = albumImg;
    }

    public String getNr_of_songs() {
        return nr_of_songs;
    }

    public void setNr_of_songs(String  nr_of_songs) {
        this.nr_of_songs = nr_of_songs;
    }


}
