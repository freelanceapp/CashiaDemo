package com.cashiar.models;

import java.io.Serializable;

public class SingleAccountModel implements Serializable {
   private int id;
   private String display_title;
   private int model_id;
   private String model_type;
   private int balance;
   private String user_type;
   private int user_id;
   private int added_by_id;
   private String deleted_at;
   private String created_at;
   private String updated_at;

    public SingleAccountModel(String display_title) {
        this.display_title = display_title;
    }

    public int getId() {
        return id;
    }

    public String getDisplay_title() {
        return display_title;
    }

    public int getModel_id() {
        return model_id;
    }

    public String getModel_type() {
        return model_type;
    }

    public int getBalance() {
        return balance;
    }

    public String getUser_type() {
        return user_type;
    }

    public int getUser_id() {
        return user_id;
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
}
