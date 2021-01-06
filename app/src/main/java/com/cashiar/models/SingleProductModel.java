package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class SingleProductModel implements Serializable {
    private int id;
    private String title;
    private String product_type;
    private double product_cost;
    private double product_price;
    private String sku;
    private String barcode_code;
    private String barcode_image;
    private String stock_type;
    private int stock_amount;
    private String display_logo_type;
    private String image;
    private int color_id;
    private int user_id;
    private int added_by_id;
    private SingleColorModel color;
    private List<SingleCategoryModel> single_category;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getProduct_type() {
        return product_type;
    }

    public double getProduct_cost() {
        return product_cost;
    }

    public double getProduct_price() {
        return product_price;
    }

    public String getSku() {
        return sku;
    }

    public String getBarcode_code() {
        return barcode_code;
    }

    public String getBarcode_image() {
        return barcode_image;
    }

    public String getStock_type() {
        return stock_type;
    }

    public int getStock_amount() {
        return stock_amount;
    }

    public String getDisplay_logo_type() {
        return display_logo_type;
    }

    public String getImage() {
        return image;
    }

    public int getColor_id() {
        return color_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getAdded_by_id() {
        return added_by_id;
    }

    public SingleColorModel getColor() {
        return color;
    }

    public List<SingleCategoryModel> getSingle_category() {
        return single_category;
    }
}

