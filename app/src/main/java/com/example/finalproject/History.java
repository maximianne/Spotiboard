package com.example.finalproject;

public class History {

    private String searchText;
    private String date;
    private String time;

    private long id;
    public History(String searchText, String date, String time){
        this.searchText=searchText;
        this.date=date;
        this.time = time;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
