package com.cashiar.ui.activity_add_Customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivityAddCustomerBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AddCustomerModel;
import com.cashiar.models.SingleCustomerSuplliersModel;
import com.cashiar.models.SingleExpensesModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_add_customer_mvp.ActivityAddCustomerPresenter;
import com.cashiar.mvp.activity_add_customer_mvp.AddCustomerActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;

import io.paperdb.Paper;

public class AddCustomerActivity extends AppCompatActivity implements AddCustomerActivityView {
    private ActivityAddCustomerBinding binding;


    private AddCustomerModel model;
    private ActivityAddCustomerPresenter presenter;
    private Preferences preferences;
    private UserModel userModel;
    private ProgressDialog dialog;
    private String type;
    private SingleCustomerSuplliersModel singlecustomerModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_customer);
        getdatafromintent();
        initView();

    }

    private void getdatafromintent() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("type") != null) {
            Log.e("dlldll", "dldlldl");
            type = intent.getStringExtra("type");
            singlecustomerModel = (SingleCustomerSuplliersModel) intent.getSerializableExtra("data");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        model = new AddCustomerModel();
        binding.setModel(model);
        presenter = new ActivityAddCustomerPresenter(this, this);


        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type!=null&&type.equals("update")) {
                    presenter.checkupdateData(model, userModel,singlecustomerModel);
                }
                else {
                        presenter.checkData(model, userModel);}

                }
        });
        binding.llBack.setOnClickListener(view -> {
            finish();
        });


        if(type!=null&&type.equals("update")){
            binding.tv.setText(getResources().getString(R.string.update_customer));
            binding.btnConfirm.setText(getResources().getString(R.string.update));

            updatediscount();
        }
    }

    private void updatediscount() {
        model.setName(singlecustomerModel.getName());
        model.setAddress(singlecustomerModel.getAddress());
        model.setEmail(singlecustomerModel.getEmail()+"");
        model.setPhone(singlecustomerModel.getPhone());
        binding.setModel(model);

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
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
}