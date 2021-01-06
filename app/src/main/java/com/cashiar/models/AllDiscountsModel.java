package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllDiscountsModel implements Serializable {
private List<SingleDiscountModel> data;

    public List<SingleDiscountModel> getData() {
        return data;
    }
}
