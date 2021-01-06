package com.cashiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.cashiar.R;


public class AddDepartmentModel extends BaseObservable {
    private String name="";

    private String color="";

    public ObservableField<String> error_name = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() && !color.trim().isEmpty() )
        {

            error_name.set(null);

            return true;
        } else {
            if (color.trim().isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.department_color), Toast.LENGTH_LONG).show();
            }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
