package com.cashiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cashiar.R;
import com.cashiar.databinding.BillSellRowBinding;
import com.cashiar.models.SingleBillOfSellModel;
import com.cashiar.ui.activity_all_bill_buy.AllBillBuyActivity;
import com.cashiar.ui.activity_all_bill_sell.AllBillSellActivity;

import java.util.List;

public class BillSellAdapter extends RecyclerView.Adapter<BillSellAdapter.ProductsViewholder> {

    private List<SingleBillOfSellModel> list;
    private Context context;
    public String currency;

    public BillSellAdapter(Context context, List<SingleBillOfSellModel> list, String currency) {
        this.list = list;
        this.context = context;
        this.currency = currency;
    }

    @NonNull
    @Override
    public ProductsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BillSellRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bill_sell_row, parent, false);
        return new ProductsViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewholder holder, int position) {
        holder.binding.setCurrency(currency);
        holder.binding.setModel(list.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof AllBillSellActivity) {
                    AllBillSellActivity productsSellActivity = (AllBillSellActivity) context;
                    productsSellActivity.openbillsell(list.get(position));
                } else if (context instanceof AllBillBuyActivity) {
                    AllBillBuyActivity productsBuyActivity = (AllBillBuyActivity) context;
                    productsBuyActivity.openbillBuy(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ProductsViewholder extends RecyclerView.ViewHolder {
        BillSellRowBinding binding;

        public ProductsViewholder(@NonNull BillSellRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
