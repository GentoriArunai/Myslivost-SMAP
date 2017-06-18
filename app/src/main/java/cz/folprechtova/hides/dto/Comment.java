package cz.folprechtova.hides.dto;

import java.io.Serializable;


public class Comment implements Serializable {

    private String comment;
    private String date;
    private String user;

    public Comment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }
}
