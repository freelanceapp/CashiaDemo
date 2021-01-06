package com.cashiar.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.cashiar.R;


public class AddPRoductModel extends BaseObservable {
    private String name="";
    private String department="";
    private String sellBy="";
    private String price="";
    private String cost="";
    private String sku="";
    private String barcode="";
    private String stock="";
    private String stokamount="0";
    private String showinsell="";
    private String color="";
    private String image_url="";
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_price = new ObservableField<>();
    public ObservableField<String> error_cost = new ObservableField<>();
    public ObservableField<String> error_stok = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() && !department.trim().isEmpty() && !sellBy.trim().isEmpty() && !price.trim().isEmpty()
                && !cost.trim().isEmpty() && !stock.trim().isEmpty() && !showinsell.trim().isEmpty() && ((stock.equals("out_stock")) || (stock.equals("in_stock") && !stokamount.trim().isEmpty()))
                && ((((showinsell.equals("color") && !color.trim().isEmpty()))) || (showinsell.equals("image") && !image_url.trim().isEmpty()))
        ) {

            error_name.set(null);
            error_cost.set(null);
            error_price.set(null);
            error_stok.set(null);
            return true;
        } else {
            if (department.trim().isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_department), Toast.LENGTH_LONG).show();
            }
            if (sellBy.trim().isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_sell), Toast.LENGTH_LONG).show();

            }
            if (stock.trim().isEmpty()) {
                Toast.makeText(context, context.getResources().getString(R.string.ch_stoke), Toast.LENGTH_LONG).show();

            } else {

                if (stock.equals("in_stock") && !stokamount.trim().isEmpty()) {
                    error_stok.set(context.getResources().getString(R.string.field_required));
                }
            }
            if (showinsell.trim().isEmpty()) {

            } else {
                if (showinsell.equals("color") && color.trim().isEmpty()) {
                    Toast.makeText(context, context.getResources().getString(R.string.ch_color), Toast.LENGTH_LONG).show();

                } else if (showinsell.equals("photo") && image_url.trim().isEmpty()) {
                    Toast.makeText(context, context.getResources().getString(R.string.choose_image), Toast.LENGTH_LONG).show();

                }
            }
            if (name.trim().isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }
            if (price.trim().isEmpty()) {
                error_price.set(context.getString(R.string.field_required));

            } else {
                error_price.set(null);

            }
            if (cost.trim().isEmpty()) {
                error_cost.set(context.getString(R.string.field_required));

            } else {
                error_cost.set(null);

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSellBy() {
        return sellBy;
    }

    public void setSellBy(String sellBy) {
        this.sellBy = sellBy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStokamount() {
        return stokamount;
    }

    public void setStokamount(String stokamount) {
        this.stokamount = stokamount;
    }

    public String getShowinsell() {
        return showinsell;
    }

    public void setShowinsell(String showinsell) {
        this.showinsell = showinsell;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
