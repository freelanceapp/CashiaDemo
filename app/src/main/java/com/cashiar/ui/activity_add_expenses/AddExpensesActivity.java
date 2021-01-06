package com.cashiar.ui.activity_add_expenses;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.adapters.SpinnerAccountsAdapter;
import com.cashiar.adapters.SpinnerCategoryAdapter;
import com.cashiar.databinding.ActivityAddExpensesBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AddExpensesModel;
import com.cashiar.models.AllAccountsModel;
import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.SingleAccountModel;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.models.SingleDiscountModel;
import com.cashiar.models.SingleExpensesModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_add_expenses_mvp.ActivityAddExpensesPresenter;
import com.cashiar.mvp.activity_add_expenses_mvp.AddExpensesActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.Activity_add_accout.AddAccountActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AddExpensesActivity extends AppCompatActivity implements AddExpensesActivityView {
    private ActivityAddExpensesBinding binding;
    private ActivityAddExpensesPresenter presenter;
    private String lang;
    private AddExpensesModel addExpensesModel;
    private UserModel userModel;
    private Preferences preferences;
    private ProgressDialog dialog;
    private List<SingleAccountModel> singleaccountModelList;
    private SpinnerAccountsAdapter spinneraccountAdapter;
    private String type;
    private SingleExpensesModel singleexpenseModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_expenses);
        getdatafromintent();
        initView();
    }

    private void getdatafromintent() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("type") != null) {
            Log.e("dlldll", "dldlldl");
            type = intent.getStringExtra("type");
            singleexpenseModel = (SingleExpensesModel) intent.getSerializableExtra("data");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        singleaccountModelList = new ArrayList<>();
        addExpensesModel = new AddExpensesModel();
        binding.setModel(addExpensesModel);
        presenter = new ActivityAddExpensesPresenter(this, this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.spcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    addExpensesModel.setAccount("");

                } else {
                    addExpensesModel.setAccount(singleaccountModelList.get(i).getId() + "");

                }
                binding.setModel(addExpensesModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnaddacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addacount();
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type!=null&&type.equals("update")) {
                    presenter.checkupdateData(addExpensesModel,userModel,singleexpenseModel);

                }
                else {
                    presenter.checkData(addExpensesModel,userModel);
            }}
        });
        binding.tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showDateDialog(getFragmentManager());}
        });
        presenter.getAccount(userModel);
        if(type!=null&&type.equals("update")){
            binding.tv.setText(getResources().getString(R.string.update_expense));
            binding.btnConfirm.setText(getResources().getString(R.string.update));

            updatediscount();
        }
    }

    private void updatediscount() {
        addExpensesModel.setAccount(singleexpenseModel.getExpense_account().getId()+"");
        addExpensesModel.setDate(singleexpenseModel.getDate());
        addExpensesModel.setPrice(singleexpenseModel.getTotal_price()+"");
        binding.tvdate.setText(singleexpenseModel.getDate());
        binding.setModel(addExpensesModel);

        for(int i=0;i<singleaccountModelList.size();i++){
            Log.e("llllsll",singleaccountModelList.get(i).getId()+" "+singleexpenseModel.getExpense_account().getId()+" "+singleexpenseModel.getDate());
            if(singleaccountModelList.get(i).getId()==singleexpenseModel.getExpense_account().getId()){
                Log.e("dlffl",i+"");
                binding.spcat.setSelection(i);
                break;
            }
        }

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
    public void onLoad() {
        dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onFinishload() {
        dialog.dismiss();
    }

    @Override
    public void onSuccess() {
        finish();
    }

    @Override
    public void onAccount() {
        Intent intent = new Intent(this, AddAccountActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            presenter.getAccount(userModel);
        }
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccess(AllAccountsModel model) {
        singleaccountModelList.clear();
        if (lang.equals("en")) {
            singleaccountModelList.add(new SingleAccountModel("Choose Account"));
        } else {

            singleaccountModelList.add(new SingleAccountModel("اختر حساب"));
        }
        //Log.e("dlldldl",model.getData().size()+"");
        singleaccountModelList.addAll(model.getData());
        spinneraccountAdapter = new SpinnerAccountsAdapter(singleaccountModelList, this);
        binding.spcat.setAdapter(spinneraccountAdapter);
        if(type!=null&&type.equals("update")){
            binding.tv.setText(getResources().getString(R.string.update_expense));
            binding.btnConfirm.setText(getResources().getString(R.string.update));

            updatediscount();
        }

    }
    @Override
    public void onDateSelected(String date)
    {
        binding.tvdate.setText(date);
        addExpensesModel.setDate(date);
        binding.setModel(addExpensesModel);
    }
}