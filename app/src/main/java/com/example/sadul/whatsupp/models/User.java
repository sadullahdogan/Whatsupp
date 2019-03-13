package com.example.sadul.whatsupp.models;

public class User {
    private String about;
    private String birthDate;
    private String picture;
    private String username;

    public User() {
    }

    public User(String about, String birthDate, String picture, String username) {
        this.about = about;
        this.birthDate = birthDate;
        this.picture = picture;
        this.username = username;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "about='" + about + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", picture='" + picture + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
