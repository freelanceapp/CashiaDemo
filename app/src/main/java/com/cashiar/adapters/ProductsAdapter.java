package com.cashiar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cashiar.R;
import com.cashiar.databinding.ProductRowBinding;
import com.cashiar.models.SingleProductModel;
import com.cashiar.ui.activity_products.ProductsActivity;
import com.cashiar.ui.activity_products_buy.ProductsBuyActivity;
import com.cashiar.ui.activity_products_sell.ProductsSellActivity;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewholder> {

    public  String currency;
    private List<SingleProductModel> list;
    private Context context;

    public ProductsAdapter(Context context, List<SingleProductModel> list, String currency) {
        this.list = list;
        this.context = context;
        this.currency=currency;
        Log.e("dldldl",currency);
    }

    @NonNull
    @Override
    public ProductsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_row, parent, false);
        return new ProductsViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewholder holder, int position) {
        if(context instanceof  ProductsSellActivity){
            holder.binding.setCon(1);
        }
        holder.binding.setCurrency(currency);
        holder.binding.setModel(list.get(position));

        if (list.get(position).getDisplay_logo_type().equals("color")) {
            holder.binding.fr.setBackgroundColor(Color.parseColor(list.get(position).getColor().getColor_code()));
            holder.binding.image.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof ProductsSellActivity) {
                    ProductsSellActivity productsSellActivity = (ProductsSellActivity) context;
                    productsSellActivity.addproductstocart(list.get(position));
                } else if (context instanceof ProductsBuyActivity) {
                    ProductsBuyActivity productsBuyActivity = (ProductsBuyActivity) context;
                    productsBuyActivity.addproductstocart(list.get(position));
                }
                else if (context instanceof ProductsActivity) {
                    ProductsActivity productsActivity = (ProductsActivity) context;
                    productsActivity.update(list.get(position));
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
