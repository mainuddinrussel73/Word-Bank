package com.example.czgame.wordbank.ui.media;

import java.util.Arrays;

public class ImageHelper {

    private String imageId;
    private byte[] imageByteArray;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "ImageHelper{" +
                "imageId='" + imageId + '\'' +
                ", imageByteArray=" + Arrays.toString(imageByteArray) +
                '}';
    }

    public byte[] getImageByteArray() {
        return imageByteArray;
    }
    public void setImageByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
    }

}

