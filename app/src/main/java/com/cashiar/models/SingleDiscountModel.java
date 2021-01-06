package com.cashiar.models;

import java.io.Serializable;

public class SingleDiscountModel implements Serializable {

    private int id;
    private String title;
    private String type;
    private double value;

    public SingleDiscountModel(String title) {
        this.title=title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }
}
