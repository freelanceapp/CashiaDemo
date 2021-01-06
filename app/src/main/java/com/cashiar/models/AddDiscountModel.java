package com.cashiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.cashiar.R;


public class AddDiscountModel extends BaseObservable {
    private String name="";

    private String value="";
private String type="";
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_type = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() && !value.trim().isEmpty()&&!type.trim().isEmpty() )
        {

            error_name.set(null);

            return true;
        } else {

            if (type.trim().isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_type), Toast.LENGTH_LONG).show();
            }
            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }
            if (value.trim().isEmpty()) {
                error_type.set(context.getString(R.string.field_required));

            } else {
                error_type.set(null);

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
