package com.example.myapplication;

public class Items {
    private String name;
    private String description;
    private String photoURL;
    private int price;
    private String date;

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getPhotoURL(){
        return photoURL;
    }
    public int getPrice(){
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setPhotoURL(String photoURL){
        this.photoURL = photoURL;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setName(String name){
        this.name = name;
    }


}
