package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class SingleBillOfSellModel implements Serializable {
    private int id;
    private String sale_type;
    private int user_id;
    private int client_id;
    private int supplier_id;
    private int creditor_id;
    private int debtor_id;
    private int coupon_id;
    private int total_price;
    private int paid_price;
    private int remaining_price;
    private int discount_value;
    private String date;
    private int added_by_id;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private SingleCustomerSuplliersModel client;
    private SingleCustomerSuplliersModel supplier;

    private List<SaleDetials> sale_details;
    private List<SaleDetials> purchase_details;

    public int getId() {
        return id;
    }

    public String getSale_type() {
        return sale_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public int getCreditor_id() {
        return creditor_id;
    }

    public int getDebtor_id() {
        return debtor_id;
    }

    public int getCoupon_id() {
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public SingleCustomerSuplliersModel getClient() {
        return client;
    }

    public SingleCustomerSuplliersModel getSupplier() {
        return supplier;
    }

    public List<SaleDetials> getSale_details() {
        return sale_details;
    }

    public List<SaleDetials> getPurchase_details() {
        return purchase_details;
    }

    public class SaleDetials implements Serializable {
        public int id;
        public int sale_id;
        public int product_id;
        public int price_value;
        public double amount;
        public String created_at;
        public String updated_at;
        private SingleProductModel product;

        public int getId() {
            return id;
        }

        public int getSale_id() {
            return sale_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public int getPrice_value() {
            return price_value;
        }

        public double getAmount() {
            return amount;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public SingleProductModel getProduct() {
            return product;
        }
    }
}
