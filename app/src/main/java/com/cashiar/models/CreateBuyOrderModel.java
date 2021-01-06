package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class CreateBuyOrderModel implements Serializable {
private int supplier_id;
        private String coupon_id;
        private double total_price;
        private double paid_price;
        private double remaining_price;
        private double discount_value;
        private String date;
        private List<ItemCartModel> order_details;

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id+"";
        if(coupon_id==0){
            this.coupon_id=null;
        }
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getPaid_price() {
        return paid_price;
    }

    public void setPaid_price(double paid_price) {
        this.paid_price = paid_price;
    }

    public double getRemaining_price() {
        return remaining_price;
    }

    public void setRemaining_price(double remaining_price) {
        this.remaining_price = remaining_price;
    }

    public double getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(double discount_value) {
        this.discount_value = discount_value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ItemCartModel> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<ItemCartModel> order_details) {
        this.order_details = order_details;
    }
}
