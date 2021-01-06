package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllCustomersModel implements Serializable {
private List<SingleCustomerSuplliersModel> data;

    public List<SingleCustomerSuplliersModel> getData() {
        return data;
    }
}
