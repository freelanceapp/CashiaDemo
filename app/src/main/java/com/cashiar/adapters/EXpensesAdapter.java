package com.cashiar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cashiar.R;
import com.cashiar.databinding.ExpensesRowBinding;
import com.cashiar.models.SingleExpensesModel;
import com.cashiar.ui.activity_expenses.ExpensesActivity;

import java.util.List;

public class EXpensesAdapter extends RecyclerView.Adapter<EXpensesAdapter.ExpensesViewholder> {

    public  String currency;
    private List<SingleExpensesModel> list;
    private Context context;

    public EXpensesAdapter(Context context, List<SingleExpensesModel> list, String currency) {
        this.list = list;
        this.context = context;
        this.currency=currency;
    }

    @NonNull
    @Override
    public ExpensesViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExpensesRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.expenses_row, parent, false);
        return new ExpensesViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewholder holder, int position) {
        holder.binding.setCurrency(currency);

        holder.binding.setModel(list.get(position));
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof ExpensesActivity){
            ExpensesActivity expensesActivity=(ExpensesActivity)context;
            expensesActivity.update(list.get(position));
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ExpensesViewholder extends RecyclerView.ViewHolder {
        ExpensesRowBinding binding;

        public ExpensesViewholder(@NonNull ExpensesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
