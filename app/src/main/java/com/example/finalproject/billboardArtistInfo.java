package com.example.finalproject;

public class billboardArtistInfo {

    private String name;
    private String topTrack;
    private String streams;

    public billboardArtistInfo(String name, String topTrack, String streams){
        this.name=name;
        this.topTrack=topTrack;
        this.streams=streams;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopTrack() {
        return topTrack;
    }

    public void setTopTrack(String topTrack) {
        this.topTrack = topTrack;
    }

    public String getStreams() {
        return streams;
    }

    public void setStreams(String streams) {
        this.streams = streams;
    }
}
