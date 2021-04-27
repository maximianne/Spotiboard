package com.example.finalproject;

public class History {
    private String searched;
    private long id;

    public History(String searched){
        this.searched=searched;
    }

    public String getSearched() {
        return searched;
    }

    public void setSearched(String searched) {
        this.searched = searched;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
