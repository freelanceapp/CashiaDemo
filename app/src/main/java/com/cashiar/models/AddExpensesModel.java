package com.cashiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.cashiar.R;


public class AddExpensesModel extends BaseObservable {
    private String account="";
    private String price="";
    private String date="";

    public ObservableField<String> error_price = new ObservableField<>();
    public ObservableField<String> error_data = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!account.trim().isEmpty()&& !price.trim().isEmpty()&&!date.isEmpty()
               )
         {


            error_price.set(null);
            return true;
        } else {
            if (account.trim().isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_account), Toast.LENGTH_LONG).show();
            }

            if (date.trim().isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_date), Toast.LENGTH_LONG).show();
            }


            if (price.trim().isEmpty()) {
                error_price.set(context.getString(R.string.field_required));

            } else {
                error_price.set(null);

            }

            return false;
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
