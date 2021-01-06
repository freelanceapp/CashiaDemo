package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllProductsModel implements Serializable {
private List<SingleProductModel> data;

    public List<SingleProductModel> getData() {
        return data;
    }
}
