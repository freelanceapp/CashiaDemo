package com.cashiar.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.cashiar.R;

public class EditProfileModel extends BaseObservable {

    private String image_url;
    private String name;
    private String phone_code;
    private String phone;
    private String address;
    private String currency;
    private String taxamount;
    private double longutide;
    private double latuide;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_currency = new ObservableField<>();
    public ObservableField<String> error_taxamount = new ObservableField<>();

    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public boolean isDataValid(Context context) {

        Log.e("name", name);
        Log.e("phone_code", phone_code);
        Log.e("phone", phone);


        if (!TextUtils.isEmpty(name) &&

                !TextUtils.isEmpty(phone_code) &&
                !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(currency) && !TextUtils.isEmpty(taxamount)
        ) {


            error_name.set(null);
            error_currency.set(null);
            error_phone_code.set(null);
            error_phone.set(null);
            error_taxamount.set(null);
            return true;
        } else {


            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);
            }

            if (TextUtils.isEmpty(currency)) {
                error_currency.set(context.getString(R.string.field_required));
            } else {
                error_currency.set(null);
            }
            if (TextUtils.isEmpty(taxamount)) {
                error_taxamount.set(context.getString(R.string.field_required));
            } else {
                error_taxamount.set(null);
            }
            if (phone_code.isEmpty()) {
                error_phone_code.set(context.getString(R.string.field_required));
            } else {
                error_phone_code.set(null);
            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));
            } else {
                error_phone.set(null);
            }


            return false;
        }
    }

    public EditProfileModel() {
        this.phone_code = "";
        notifyPropertyChanged(BR.phone_code);
        this.phone = "";
        notifyPropertyChanged(BR.phone);
        this.name = "";
        notifyPropertyChanged(BR.name);

        this.image_url = "";

        this.taxamount = "";
        this.currency = "";
        notifyPropertyChanged(BR.currency);

    }


    @Bindable
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }


    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTaxamount() {
        return taxamount;
    }
    @Bindable
    public void setTaxamount(String taxamount) {
        this.taxamount = taxamount;
    }

    public double getLongutide() {
        return longutide;
    }

    public void setLongutide(double longutide) {
        this.longutide = longutide;
    }

    public double getLatuide() {
        return latuide;
    }

    public void setLatuide(double latuide) {
        this.latuide = latuide;
    }
}
