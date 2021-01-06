package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllAccountsModel implements Serializable {
private List<SingleAccountModel> data;

    public List<SingleAccountModel> getData() {
        return data;
    }
}
