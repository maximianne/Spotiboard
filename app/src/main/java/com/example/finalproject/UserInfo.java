package com.example.finalproject;

import java.util.ArrayList;

public class UserInfo {

   private String username;
   private String password;
   private String email;
   private ArrayList<String> artist;

    public UserInfo(String username, String password, String email, ArrayList<String> artist){
        this.username=username;
        this.email=email;
        this.password=password;
        this.artist=artist;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getArtist() {
        return artist;
    }

    public void setArtist(ArrayList<String> artist) {
        this.artist = artist;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
