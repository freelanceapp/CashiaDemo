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
import com.cashiar.databinding.ColorRowBinding;
import com.cashiar.models.SingleColorModel;
import com.cashiar.ui.activity_add_departmnet.AddDepartmnetActivity;
import com.cashiar.ui.activity_add_product.AddProductActivity;

import java.util.List;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorViewholder> {

    private List<SingleColorModel> list;
    private Context context;
    private int i=-1;

    public ColorsAdapter(Context context, List<SingleColorModel> list) {
        this.list = list;
        this.context = context;
    }

    public void setI(int i) {
        this.i = i;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ColorRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.color_row, parent, false);
        return new ColorViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewholder holder, int position) {
holder.binding.cardView.setCardBackgroundColor(Color.parseColor(list.get(position).getColor_code()));
holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        i=position;
        notifyDataSetChanged();
    }
});
if(i==position){

    holder.binding.image.setVisibility(View.VISIBLE);
    holder.binding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.gray2));
if(context instanceof AddProductActivity){
    AddProductActivity addProductActivity=(AddProductActivity)context;
    addProductActivity.setcolor(list.get(i).getId());
}
   else if(context instanceof AddDepartmnetActivity){
    AddDepartmnetActivity addDepartmnetActivity=(AddDepartmnetActivity)context;
        addDepartmnetActivity.setcolor(list.get(i).getId());
    }
}
else {
    holder.binding.image.setVisibility(View.GONE);
    holder.binding.cardView.setCardBackgroundColor(Color.parseColor(list.get(position).getColor_code()));


}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ColorViewholder extends RecyclerView.ViewHolder {
        ColorRowBinding binding;

        public ColorViewholder(@NonNull ColorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
