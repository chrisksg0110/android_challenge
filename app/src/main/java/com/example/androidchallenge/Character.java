package com.example.androidchallenge;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Character {
    private int id;
    private String name;
    private String nickname;
    private String status;
    private String portrayed;
    private String imgUrl;
    private Bitmap imgBitmap;
    private ArrayList<String> occupations;
    private boolean isFavorite;

    public Character(int id, String name, String nickname, String status, String portrayed, String imgUrl, ArrayList<String> occupations) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.status = status;
        this.portrayed = portrayed;
        this.imgUrl = imgUrl;
        this.occupations = occupations;
        this.isFavorite = isFavorite;
        this.imgBitmap = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<String> getOccupations() {
        return occupations;
    }

    public void setOccupations(ArrayList<String> occupations) {
        this.occupations = occupations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPortrayed() {
        return portrayed;
    }

    public void setPortrayed(String portrayed) {
        this.portrayed = portrayed;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
