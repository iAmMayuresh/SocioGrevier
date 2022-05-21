package com.mayuresh08.sociogrevier;

public class model_p {
    String police_posts,fullname,location,time,date,description;

    public model_p() {

    }

    public model_p(String police_posts, String fullname, String location, String time, String date, String description, String uid) {
        this.police_posts = police_posts;
        this.fullname = fullname;
        this.location = location;
        this.time = time;
        this.date = date;
        this.description = description;
    }

    public String getPolice_posts() {
        return police_posts;
    }

    public void setPolice_posts(String police_posts) {
        this.police_posts = police_posts;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
