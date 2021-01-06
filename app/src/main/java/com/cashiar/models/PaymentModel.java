package com.cashiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;

import com.cashiar.R;


public class PaymentModel extends BaseObservable {
    private String id="";




    public boolean isDataValid(Context context) {
        if (!id.trim().isEmpty()  )
        {


            return true;
        } else {

            if (id.trim().isEmpty()) {
               Toast.makeText(context,context.getResources().getString(R.string.ch_customer),Toast.LENGTH_LONG).show();

            }

            return false;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
