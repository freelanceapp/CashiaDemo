package com.cashiar.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.cashiar.BR;
import com.cashiar.R;

import java.io.Serializable;

public class AddCustomerModel extends BaseObservable implements Serializable {
    private String name;
    private String email = "";

    private String address;
    private String phone;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public AddCustomerModel() {
        this.address = "";
        this.phone = "";
        this.name = "";
        this.email = "";

    }

    public boolean isDataValid(Context context) {
        if (!name.isEmpty()&&!phone.isEmpty()
        ) {
            error_name.set(null);
            error_phone.set(null);
            return true;
        } else {
            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);
            }
            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));
            } else {
                error_phone.set(null);
            }

        }
        return false;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
