package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class AllPermissionModel implements Serializable {
private List<SinglePermissionModel> data;

    public List<SinglePermissionModel> getData() {
        return data;
    }
}
