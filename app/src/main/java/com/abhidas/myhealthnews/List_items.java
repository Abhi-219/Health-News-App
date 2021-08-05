package com.abhidas.myhealthnews;

public class List_items {
    String title, description;
    String image;

    public List_items(String title, String description,String image) {
        this.title = title;
        this.description = description;
        this.image=image;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() { return image; }
}
