package com.cashiar.models;

import java.io.Serializable;

public class BillModel implements Serializable {
    private String sale_type;
    private int user_id;
    private int client_id;
    private int creditor_id;
    private int debtor_id;
    private String coupon_id;
    private int total_price;
    private int paid_price;
    private int remaining_price;
    private int discount_value;
    private String date;
    private int added_by_id;
    private String updated_at;
    private String created_at;
    private int id;

    public String getSale_type() {
        return sale_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public int getCreditor_id() {
        return creditor_id;
    }

    public int getDebtor_id() {
        return debtor_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public int getTotal_price() {
        return total_price;
    }

    public int getPaid_price() {
        return paid_price;
    }

    public int getRemaining_price() {
        return remaining_price;
    }

    public int getDiscount_value() {
        return discount_value;
    }

    public String getDate() {
        return date;
    }

    public int getAdded_by_id() {
        return added_by_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getId() {
        return id;
    }
}
