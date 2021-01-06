package com.cashiar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.SpinnerCategoryRowBinding;
import com.cashiar.models.SingleAccountModel;
import com.cashiar.models.SingleDiscountModel;

import java.util.List;

public class SpinnerAccountsAdapter extends BaseAdapter {
    private List<SingleAccountModel> list;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerAccountsAdapter(List<SingleAccountModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") SpinnerCategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_category_row,parent,false);
        String title = list.get(position).getDisplay_title();
        binding.setTitle(title);
        return binding.getRoot();
    }
}
