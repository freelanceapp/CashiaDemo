package com.cashiar.ui.activity_all_bill_buy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.BillSellAdapter;
import com.cashiar.databinding.ActivityAllBillBuyBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllBillOfSellModel;
import com.cashiar.models.SingleBillOfSellModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_all_bill_buy_mvp.ActivityAllBillBuyPresenter;
import com.cashiar.mvp.activity_all_bill_buy_mvp.AllBillBuyActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.Activity_products_bill_sell.ProductsBillSellActivity;
import com.cashiar.ui.activity_products_bill_buy.ProductsBillBuyActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AllBillBuyActivity extends AppCompatActivity implements AllBillBuyActivityView {
    private ActivityAllBillBuyBinding binding;
    private ActivityAllBillBuyPresenter presenter;
    private String lang;
    private BillSellAdapter billSellAdapter;
    private List<SingleBillOfSellModel> singleBillOfSellModels;


    private UserModel userModel;
    private Preferences preferences;
    private ProgressDialog dialog;
    private String currency="";


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_bill_buy);
        initView();
    }


    private void initView() {
        Paper.init(this);
        singleBillOfSellModels = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", "ar");

        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });

        presenter = new ActivityAllBillBuyPresenter(this, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        billSellAdapter = new BillSellAdapter(this, singleBillOfSellModels,currency);
        binding.recView.setAdapter(billSellAdapter);
        presenter.getprofile(userModel);
        presenter.getbillSell(userModel);
    }

    @Override
    public void onBackPressed() {
        presenter.backPress();
    }


    @Override
    public void onFinished() {
        finish();
    }


    @Override
    public void onProgressShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccess(AllBillOfSellModel model) {
        singleBillOfSellModels.clear();
        singleBillOfSellModels.addAll(model.getData());
        billSellAdapter.notifyDataSetChanged();
        if(model.getData().size()==0){
            binding.llNoNotification.setVisibility(View.VISIBLE);
        }
        else {
            binding.llNoNotification.setVisibility(View.GONE);

        }

    }




    public void openbillBuy(SingleBillOfSellModel singleBillOfSellModel) {
        Intent intent = new Intent(this, ProductsBillBuyActivity.class);
        intent.putExtra("data", singleBillOfSellModel);
        startActivity(intent);
    }
    @Override
    public void onLoad() {
        if(dialog==null){
            dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);}
        else {
            dialog.dismiss();
        }
        dialog.show();
    }

    @Override
    public void onFinishload() {
        dialog.dismiss();
    }



    @Override
    public void onprofileload(UserModel body) {
        this.currency = body.getCurrency();
        billSellAdapter.currency=currency;
        billSellAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
    }
}