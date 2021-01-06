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
import com.cashiar.databinding.MainCategoryRowBinding;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.tags.Tags;
import com.cashiar.ui.activity_products_buy.ProductsBuyActivity;
import com.cashiar.ui.activity_products_sell.ProductsSellActivity;
import com.esotericsoftware.minlog.Log;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesSaleBuydapter extends RecyclerView.Adapter<CategoriesSaleBuydapter.CategoryViewholder> {

    private List<SingleCategoryModel> list;
    private Context context;

    public CategoriesSaleBuydapter(Context context, List<SingleCategoryModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.main_category_row, parent, false);
        return new CategoryViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {
        holder.binding.setModel(list.get(position));

        if (list.get(position).getDisplay_logo_type().equals("color")) {
            holder.binding.image.setBackgroundColor(Color.parseColor(list.get(position).getColor().getColor_code()));
        } else {
            Picasso.get().load(Tags.IMAGE_URL + list.get(position).getImage()).into(holder.binding.image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof ProductsSellActivity) {
                    ProductsSellActivity productsSellActivity = (ProductsSellActivity) context;
                    productsSellActivity.getproducts(list.get(position).getId());
                } else if (context instanceof ProductsBuyActivity) {
                    ProductsBuyActivity productsBuyActivity = (ProductsBuyActivity) context;
                    productsBuyActivity.getproducts(list.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CategoryViewholder extends RecyclerView.ViewHolder {
        MainCategoryRowBinding binding;

        public CategoryViewholder(@NonNull MainCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
