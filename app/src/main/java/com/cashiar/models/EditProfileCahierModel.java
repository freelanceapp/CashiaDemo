package com.cashiar.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.cashiar.R;

public class EditProfileCahierModel extends BaseObservable {

    private String name;
    private String phone_code;
    private String phone;
    private String address;
    private double longutide;
    private double latuide;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_currency = new ObservableField<>();
    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public boolean isDataValid(Context context) {

        Log.e("name", name);
        Log.e("phone_code", phone_code);
        Log.e("phone", phone);


        if (!TextUtils.isEmpty(name) &&

                !TextUtils.isEmpty(phone_code) &&
                !TextUtils.isEmpty(phone)
        ) {


            error_name.set(null);
            error_currency.set(null);
            error_phone_code.set(null);
            error_phone.set(null);

            return true;
        } else {


            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);
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

    public EditProfileCahierModel() {
        this.phone_code = "";
        notifyPropertyChanged(BR.phone_code);
        this.phone = "";
        notifyPropertyChanged(BR.phone);
        this.name = "";
        notifyPropertyChanged(BR.name);



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
