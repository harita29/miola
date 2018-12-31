package com.example.android.sfwhf1;


/**
 * Created by bedryabalema on 10/13/17.
 */

public class Post {
    private String uname, title, comments, image;

    public Post(){}

    public Post(String uname, String title, String comments, String image) {
        this.uname = uname;
        this.title = title;
        this.comments = comments;
        this.image = image;

    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUname() {
        return uname;
    }

    public String getTitle() {
        return title;
    }

    public String getComments() {return comments;}

    public String getImage() {
        return image;
    }
}

