package com.cashiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.cashiar.R;


public class AddAccountModel extends BaseObservable {
    private String name="";



    public ObservableField<String> error_name = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty()  )
        {

            error_name.set(null);

            return true;
        } else {


            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }

            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
