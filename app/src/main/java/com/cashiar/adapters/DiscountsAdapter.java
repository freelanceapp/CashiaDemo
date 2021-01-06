package com.cashiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cashiar.R;
import com.cashiar.databinding.DiscountRowBinding;
import com.cashiar.models.SingleDiscountModel;
import com.cashiar.ui.activity_disacount.DiscountActivity;

import java.util.List;

public class DiscountsAdapter extends RecyclerView.Adapter<DiscountsAdapter.DiscountViewholder> {

    public  String currency;
    private List<SingleDiscountModel> list;
    private Context context;

    public DiscountsAdapter(Context context, List<SingleDiscountModel> list, String currency) {
        this.list = list;
        this.context = context;
        this.currency = currency;
    }

    @NonNull
    @Override
    public DiscountViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiscountRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.discount_row, parent, false);
        return new DiscountViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewholder holder, int position) {
        holder.binding.setCurrency(currency);
        holder.binding.setModel(list.get(position));
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof DiscountActivity){
            DiscountActivity discountActivity=(DiscountActivity)context;
            discountActivity.update(list.get(position));
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class DiscountViewholder extends RecyclerView.ViewHolder {
        DiscountRowBinding binding;

        public DiscountViewholder(@NonNull DiscountRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
