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
import com.cashiar.models.SingleCustomerSuplliersModel;

import java.util.List;

public class SpinnerCustomerAdapter extends BaseAdapter {
    private List<SingleCustomerSuplliersModel> list;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerCustomerAdapter(List<SingleCustomerSuplliersModel> list, Context context) {
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
        String title = list.get(position).getName();
        binding.setTitle(title);
        return binding.getRoot();
    }
}
