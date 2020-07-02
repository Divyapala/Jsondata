package com.example.app;


import com.google.gson.annotations.SerializedName;

public class Network{


    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;


    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }


}