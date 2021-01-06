package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllExpensesModel implements Serializable {
private List<SingleExpensesModel> data;

    public List<SingleExpensesModel> getData() {
        return data;
    }
}
