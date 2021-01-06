package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllBillOfSellModel implements Serializable {
private List<SingleBillOfSellModel> data;

    public List<SingleBillOfSellModel> getData() {
        return data;
    }
}
