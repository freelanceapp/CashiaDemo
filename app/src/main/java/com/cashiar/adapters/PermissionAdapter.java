package com.cashiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cashiar.R;
import com.cashiar.databinding.PermissionRowBinding;
import com.cashiar.models.SinglePermissionModel;
import com.cashiar.ui.activity_add_cashier.AddCashierActivity;
import com.cashiar.ui.activity_all_bill_buy.AllBillBuyActivity;
import com.cashiar.ui.activity_all_bill_sell.AllBillSellActivity;

import java.util.List;

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.PermessionViewholder> {

    private List<SinglePermissionModel> list;
    private Context context;
    private AddCashierActivity addCashierActivity;

    public PermissionAdapter(Context context, List<SinglePermissionModel> list) {
        this.list = list;
        this.context = context;
        addCashierActivity = (AddCashierActivity) context;
    }

    @NonNull
    @Override
    public PermessionViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PermissionRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.permission_row, parent, false);
        return new PermessionViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PermessionViewholder holder, int position) {

        holder.binding.setModel(list.get(position));
        holder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    addCashierActivity.addid(list.get(position).getId());
                } else {
                    addCashierActivity.removeid(list.get(position).getId());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class PermessionViewholder extends RecyclerView.ViewHolder {
        PermissionRowBinding binding;

        public PermessionViewholder(@NonNull PermissionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
