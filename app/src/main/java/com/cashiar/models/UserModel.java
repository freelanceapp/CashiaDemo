package com.cashiar.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    private int id;
    private String user_type;
    private String name;
    private String code;
    private String email;
    private String phone_code;
    private String phone;
    private int trader_id;
    private String parent_id;
    private String logo;
    private String banner;
    private String address;
    private String notes;
    private String latitude;
    private String longitude;
    private String is_confirmed;
    private String is_block;
    private String is_login;
    private long logout_time;
    private String notification_status;
    private String email_verification_code;
    private String email_verified_at;
    private String software_type;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private String token;
    private String tax_amount;
    private String currency;
    private List<Permissions> permissions;

    public int getId() {
        return id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public int getTrader_id() {
        return trader_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getLogo() {
        return logo;
    }

    public String getBanner() {
        return banner;
    }

    public String getAddress() {
        return address;
    }

    public String getNotes() {
        return notes;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getIs_confirmed() {
        return is_confirmed;
    }

    public String getIs_block() {
        return is_block;
    }

    public String getIs_login() {
        return is_login;
    }

    public long getLogout_time() {
        return logout_time;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public String getEmail_verification_code() {
        return email_verification_code;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getSoftware_type() {
        return software_type;
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

    public String getToken() {
        return token;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public static class Permissions implements Serializable {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
