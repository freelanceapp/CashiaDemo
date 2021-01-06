package com.cashiar.models;

import java.io.Serializable;

public class SinglePermissionModel implements Serializable {
    private int id;
    private String name;
    private String ar_name;
    private String type;
    private String type_order;
    private String class_name;
    private String guard_name;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAr_name() {
        return ar_name;
    }

    public String getType() {
        return type;
    }

    public String getType_order() {
        return type_order;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getGuard_name() {
        return guard_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
