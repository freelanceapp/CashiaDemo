package com.cashiar.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cashiar.R;
import com.cashiar.databinding.CartRowBinding;
import com.cashiar.models.ItemCartModel;
import com.cashiar.ui.activity_cart_bill_buy.CartBillBuyActivity;
import com.cashiar.ui.activity_cart_bill_sell.CartBillSellActivity;
import com.cashiar.ui.activity_cart_buy.CartBuyActivity;
import com.cashiar.ui.activity_cart_sell.CartSellActivity;

import java.util.List;

public class CartSellBuyAdapter extends RecyclerView.Adapter<CartSellBuyAdapter.Cart_Holder> {
    public  String currency;
    private List<ItemCartModel> itemCartModelList;
    public Context context;


    public CartSellBuyAdapter(List<ItemCartModel> itemCartModelList, Context context, String currency) {
        this.itemCartModelList = itemCartModelList;
        this.context = context;
        this.currency = currency;
    }

    @Override
    public Cart_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cart_row, parent, false);
        return new Cart_Holder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final Cart_Holder holder, final int i) {
        holder.binding.setCurrency(currency);
        holder.binding.setModel(itemCartModelList.get(i));
        if (itemCartModelList.get(i).getType().equals("weight")) {
            holder.binding.tvweight.setVisibility(View.VISIBLE);
        }
        holder.binding.imgIncrease.setOnClickListener(v -> {
                    ItemCartModel model2 = itemCartModelList.get(holder.getAdapterPosition());
                    double count = 0;
                    count = model2.getAmount() + model2.getAmount2();
                    if (context instanceof CartBillSellActivity) {

                        if (count <= model2.getStock()) {
                            holder.binding.tvAmount.setText(String.valueOf(count));
                            holder.binding.tvCost.setText((model2.getPrice_value()*count)+currency);
                            model2.setAmount(count);
                            itemCartModelList.set(holder.getAdapterPosition(), model2);
                        }

                    } else if (context instanceof CartBillBuyActivity) {
                        if (count <= model2.getStock()) {
                            holder.binding.tvAmount.setText(String.valueOf(count));
                            holder.binding.tvCost.setText((model2.getPrice_value()*count)+currency);

                            model2.setAmount(count);
                            itemCartModelList.set(holder.getAdapterPosition(), model2);
                        }

                    } else {
                        if (model2.getType().equals("in_stock") && count <= model2.getStock()) {
                            holder.binding.tvAmount.setText(String.valueOf(count));
                            model2.setAmount(count);
                            holder.binding.tvCost.setText((model2.getPrice_value()*count)+currency);

                            itemCartModelList.set(holder.getAdapterPosition(), model2);
                        } else {
                            holder.binding.tvAmount.setText(String.valueOf(count));
                            model2.setAmount(count);
                            holder.binding.tvCost.setText((model2.getPrice_value()*count)+currency);

                            itemCartModelList.set(holder.getAdapterPosition(), model2);
                        }
                    }

                    if (context instanceof CartSellActivity) {
                        CartSellActivity cartSellActivity = (CartSellActivity) context;
                        cartSellActivity.increase_decrease(model2, holder.getAdapterPosition());

                    } else if (context instanceof CartBuyActivity) {
                        CartBuyActivity cartBuyActivity = (CartBuyActivity) context;
                        cartBuyActivity.increase_decrease(model2, holder.getAdapterPosition());

                    } else if (context instanceof CartBillSellActivity) {

                        CartBillSellActivity cartBillSellActivity = (CartBillSellActivity) context;
                        cartBillSellActivity.increase_decrease(model2, holder.getAdapterPosition());

                    } else if (context instanceof CartBillBuyActivity) {
                        CartBillBuyActivity cartBillSellActivity = (CartBillBuyActivity) context;
                        cartBillSellActivity.increase_decrease(model2, holder.getAdapterPosition());

                    }
                    notifyItemChanged(holder.getAdapterPosition());
                }

        );
        holder.binding.imgDecrease.setOnClickListener(v -> {
                    ItemCartModel model2 = itemCartModelList.get(holder.getAdapterPosition());
                    double count = model2.getAmount() - model2.getAmount2();
                    if (count > 0) {
                        model2.setAmount(count);
                        holder.binding.tvCost.setText((model2.getPrice_value()*count)+currency);

                        holder.binding.tvAmount.setText(String.valueOf(count));
                        itemCartModelList.set(holder.getAdapterPosition(), model2);
                        if (context instanceof CartSellActivity) {
                            CartSellActivity cartSellActivity = (CartSellActivity) context;
                            cartSellActivity.increase_decrease(model2, holder.getAdapterPosition());

                        } else if (context instanceof CartBuyActivity) {
                            CartBuyActivity cartBuyActivity = (CartBuyActivity) context;
                            cartBuyActivity.increase_decrease(model2, holder.getAdapterPosition());

                        } else if (context instanceof CartBillSellActivity) {
                            CartBillSellActivity cartBillSellActivity = (CartBillSellActivity) context;
                            cartBillSellActivity.increase_decrease(model2, holder.getAdapterPosition());

                        } else if (context instanceof CartBillBuyActivity) {
                            CartBillBuyActivity cartBillSellActivity = (CartBillBuyActivity) context;
                            cartBillSellActivity.increase_decrease(model2, holder.getAdapterPosition());

                        }
                        notifyItemChanged(holder.getAdapterPosition());
                    }

                }

        );
        holder.binding.tvweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof CartSellActivity) {
                    CartSellActivity cartSellActivity = (CartSellActivity) context;
                    cartSellActivity.CreateDialogAlert(context, holder.getAdapterPosition());

                } else if (context instanceof CartBuyActivity) {
                    CartBuyActivity cartBuyActivity = (CartBuyActivity) context;
                    cartBuyActivity.CreateDialogAlert(context, holder.getAdapterPosition());

                } else if (context instanceof CartBillSellActivity) {
                    CartBillSellActivity cartBillSellActivity = (CartBillSellActivity) context;
                    cartBillSellActivity.CreateDialogAlert(context, holder.getAdapterPosition());

                } else if (context instanceof CartBillBuyActivity) {
                    CartBillBuyActivity cartBillSellActivity = (CartBillBuyActivity) context;
                    cartBillSellActivity.CreateDialogAlert(context, holder.getAdapterPosition());

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemCartModelList.size();
    }

    public class Cart_Holder extends RecyclerView.ViewHolder {
        CartRowBinding binding;

        public Cart_Holder(@NonNull CartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }


}
