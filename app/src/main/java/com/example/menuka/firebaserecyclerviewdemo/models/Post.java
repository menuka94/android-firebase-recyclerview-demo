package com.example.menuka.firebaserecyclerviewdemo.models;

/**
 * Created by menuka on 2/16/17.
 */

public class Post {
    private String imageUrl;
    private long numLikes;
    private String UID;

    public Post(){

    }

    public Post(String imageUrl, long numLikes, String UID) {
        this.imageUrl = imageUrl;
        this.numLikes = numLikes;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }

}
