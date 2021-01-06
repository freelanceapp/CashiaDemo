package com.cashiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.cashiar.BR;
import com.cashiar.R;

import java.io.Serializable;

public class SignUpModel extends BaseObservable implements Serializable {
    private String name;
    private String email = "";

    private String phone_code;
    private String phone;
    private int accepted;
    public ObservableField<String> error_name = new ObservableField<>();


    public SignUpModel(String phone_code, String phone) {
        this.phone_code = phone_code;
        this.phone = phone;
        this.name = "";
        this.email = "";
        this.accepted = 0;
    }

    public boolean isDataValid(Context context) {
        if (!name.isEmpty() && accepted == 1
        ) {
            return true;
        } else {
            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);
            }

            if (accepted == 0) {
                Toast.makeText(context, context.getResources().getString(R.string.accept_terms_and_conditions), Toast.LENGTH_LONG).show();
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

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
}
