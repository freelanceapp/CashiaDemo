package com.cashiar.ui.Activity_add_accout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivityAddAccountBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AddAccountModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_add_account_mvp.ActivityAddAccountPresenter;
import com.cashiar.mvp.activity_add_account_mvp.AddAccountActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;

import io.paperdb.Paper;

public class AddAccountActivity extends AppCompatActivity implements AddAccountActivityView {
    private ActivityAddAccountBinding binding;
    private ActivityAddAccountPresenter presenter;
    private String lang;
    private AddAccountModel addAccountModel;
    private Preferences preferences;
    private UserModel userModel;
    private ProgressDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_account);
        initView();
    }


    private void initView() {
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityAddAccountPresenter(this, this);

        addAccountModel = new AddAccountModel();

        binding.setModel(addAccountModel);

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.checkData(addAccountModel,userModel);
            }
        });

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
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}