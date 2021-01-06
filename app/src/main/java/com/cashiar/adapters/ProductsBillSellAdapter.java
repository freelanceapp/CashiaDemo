package com.cashiar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cashiar.R;
import com.cashiar.databinding.ProductRowBinding;
import com.cashiar.models.SingleBillOfSellModel;
import com.cashiar.models.SingleProductModel;
import com.cashiar.ui.Activity_products_bill_sell.ProductsBillSellActivity;
import com.cashiar.ui.activity_products_bill_buy.ProductsBillBuyActivity;
import com.cashiar.ui.activity_products_buy.ProductsBuyActivity;
import com.cashiar.ui.activity_products_sell.ProductsSellActivity;

import java.util.List;

public class ProductsBillSellAdapter extends RecyclerView.Adapter<ProductsBillSellAdapter.ProductsViewholder> {

    private List<SingleBillOfSellModel.SaleDetials> list;
    private Context context;
public String currency;
    public ProductsBillSellAdapter(Context context, List<SingleBillOfSellModel.SaleDetials> list,String currency) {
        this.list = list;
        this.context = context;
        this.currency=currency;
    }

    @NonNull
    @Override
    public ProductsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_row, parent, false);
        return new ProductsViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewholder holder, int position) {
        if(context instanceof  ProductsBillSellActivity){
            holder.binding.setCon(1);
        }
        holder.binding.setCurrency(currency);
        holder.binding.setModel(list.get(position).getProduct());

        if (list.get(position).getProduct()!=null&&list.get(position).getProduct().getDisplay_logo_type().equals("color")) {
            holder.binding.fr.setBackgroundColor(Color.parseColor(list.get(position).getProduct().getColor().getColor_code()));
            holder.binding.image.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof ProductsBillSellActivity) {
                    ProductsBillSellActivity productsSellActivity = (ProductsBillSellActivity) context;
                    productsSellActivity.addproductstocart(list.get(position));
                } else if (context instanceof ProductsBillBuyActivity) {
                    ProductsBillBuyActivity productsSellActivity = (ProductsBillBuyActivity) context;
                    productsSellActivity.addproductstocart(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ProductsViewholder extends RecyclerView.ViewHolder {
        ProductRowBinding binding;

        public ProductsViewholder(@NonNull ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
