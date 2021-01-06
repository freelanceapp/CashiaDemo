package com.cashiar.models;

import java.io.Serializable;

public class SingleCustomerSuplliersModel implements Serializable {
    private int id;
    private String name;
    private String phone_code;
    private String phone;
    private String email;
    private String address;

    public SingleCustomerSuplliersModel(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}
