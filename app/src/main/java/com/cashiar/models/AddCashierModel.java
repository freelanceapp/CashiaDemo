package com.cashiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.cashiar.BR;
import com.cashiar.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddCashierModel extends BaseObservable implements Serializable {
    private String name;
    private String email = "";

    private String address;
    private String phone;
    private List<Integer> ids;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public AddCashierModel() {
        this.address = "";
        this.phone = "";
        this.name = "";
        this.email = "";
ids=new ArrayList<>();
    }

    public boolean isDataValid(Context context) {
        if (!name.isEmpty() && !phone.isEmpty()&&ids.size()>0
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
            if(ids.size()==0){
                Toast.makeText(context,context.getResources().getString(R.string.ch_department),Toast.LENGTH_LONG).show();
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

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
