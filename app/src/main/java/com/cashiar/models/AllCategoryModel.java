package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllCategoryModel implements Serializable {
private List<SingleCategoryModel> data;

    public List<SingleCategoryModel> getData() {
        return data;
    }
}
