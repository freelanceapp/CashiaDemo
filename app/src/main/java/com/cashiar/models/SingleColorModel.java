package com.cashiar.models;

import java.io.Serializable;

public class SingleColorModel implements Serializable {
    private int id;
    private String color_code;

    public int getId() {
        return id;
    }

    public String getColor_code() {
        return color_code;
    }
}
