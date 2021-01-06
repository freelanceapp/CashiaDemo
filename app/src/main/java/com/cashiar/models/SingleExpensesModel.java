package com.cashiar.models;

import java.io.Serializable;

public class SingleExpensesModel implements Serializable {
    private int id;
    private String type;
    private int user_id;
    private int creditor_id;
    private int debtor_id;
    private double total_price;
    private String date;
    private int added_by_id;
    private SingleAccountModel revenue_account;
    private SingleAccountModel expense_account;
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCreditor_id() {
        return creditor_id;
    }

    public int getDebtor_id() {
        return debtor_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public String getDate() {
        return date;
    }

    public int getAdded_by_id() {
        return added_by_id;
    }

    public SingleAccountModel getRevenue_account() {
        return revenue_account;
    }

    public SingleAccountModel getExpense_account() {
        return expense_account;
    }
}
