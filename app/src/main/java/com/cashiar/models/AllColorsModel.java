package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllColorsModel implements Serializable {
private List<SingleColorModel> data;

    public List<SingleColorModel> getData() {
        return data;
    }
}
