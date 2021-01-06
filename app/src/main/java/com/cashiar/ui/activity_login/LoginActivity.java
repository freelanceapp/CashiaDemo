package com.cashiar.ui.activity_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivityLoginBinding;
import com.cashiar.language.Language;
import com.cashiar.models.LoginModel;
import com.cashiar.mvp.activity_login_presenter.ActivityLoginPresenter;
import com.cashiar.mvp.activity_login_presenter.ActivityLoginView;
import com.cashiar.ui.activity_confirm_code.ConfirmCodeActivity;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements ActivityLoginView {
    private ActivityLoginBinding binding;
    private LoginModel model;
    private ActivityLoginPresenter presenter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase,Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }



    private void initView() {
        model = new LoginModel();

        binding.setModel(model);
        presenter = new ActivityLoginPresenter(this,this);
        binding.btlogin.setOnClickListener(view -> {
            presenter.checkData(model);
        });


    }

    @Override
    public void onLoginValid() {
        Intent intent = new Intent(this, ConfirmCodeActivity.class);
        intent.putExtra("phone_code",model.getPhone_code());
        intent.putExtra("phone",model.getPhone());

        startActivity(intent);
        finish();

    }
}