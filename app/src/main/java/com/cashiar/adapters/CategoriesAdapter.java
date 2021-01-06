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
import com.cashiar.databinding.CategoryRowBinding;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.ui.activity_categories.CategoriesActivity;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewholder> {

    private List<SingleCategoryModel> list;
    private Context context;

    public CategoriesAdapter(Context context, List<SingleCategoryModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.category_row, parent, false);
        return new CategoryViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, int position) {
holder.binding.setModel(list.get(position));
if(list.get(position).getDisplay_logo_type().equals("color")){
    holder.binding.fr.setBackgroundColor(Color.parseColor(list.get(position).getColor().getColor_code()));
    holder.binding.image.setVisibility(View.GONE);
}
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof CategoriesActivity){
            CategoriesActivity categoriesActivity=(CategoriesActivity)context;
            categoriesActivity.update(list.get(position));
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CategoryViewholder extends RecyclerView.ViewHolder {
        CategoryRowBinding binding;

        public CategoryViewholder(@NonNull CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
