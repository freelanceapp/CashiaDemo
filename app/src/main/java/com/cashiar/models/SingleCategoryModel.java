package com.cashiar.models;

import java.io.Serializable;

public class SingleCategoryModel implements Serializable {

    private int id;
    private String title;
    private String display_logo_type;
    private String image;
    private String color_id;
    private String user_id;
    private String added_by_id;
    private String deleted_at;
    private int products_count;
    private SingleColorModel color;

    public SingleCategoryModel(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDisplay_logo_type() {
        return display_logo_type;
    }

    public String getImage() {
        return image;
    }

    public String getColor_id() {
        return color_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAdded_by_id() {
        return added_by_id;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public int getProducts_count() {
        return products_count;
    }

    public SingleColorModel getColor() {
        return color;
    }
}
