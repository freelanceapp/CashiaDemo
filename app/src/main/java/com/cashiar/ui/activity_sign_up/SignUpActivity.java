package com.cashiar.ui.activity_sign_up;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivitySignupBinding;
import com.cashiar.language.Language;
import com.cashiar.models.SignUpModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_sign_up_mvp.ActivitySignUpPresenter;
import com.cashiar.mvp.activity_sign_up_mvp.ActivitySignUpView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_home.HomeActivity;
import com.cashiar.ui.activity_welcome.WelcomeActivity;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity implements ActivitySignUpView {
    private ActivitySignupBinding binding;

    private String phone = "", phone_code = "";
    private SignUpModel model;
    private ActivitySignUpPresenter presenter;
    private ProgressDialog dialog;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            phone_code = intent.getStringExtra("phone_code");
            phone = intent.getStringExtra("phone");


        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        model = new SignUpModel(phone_code, phone);
        binding.setModel(model);
        presenter = new ActivitySignUpPresenter(this, this);

        binding.checkbox.setOnClickListener(view -> {
            if (binding.checkbox.isChecked()) {
                model.setAccepted(1);
            } else {
                model.setAccepted(0);
            }
            binding.setModel(model);
        });
        binding.btsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.checkData(model);
            }
        });


    }


    @Override
    public void onSignupValid(UserModel userModel) {
        preferences.create_update_userdata(SignUpActivity.this, userModel);

        if (userModel.getIs_confirmed().equals("accepted")) {

            Intent intent = new Intent(this, HomeActivity.class);

            startActivity(intent);
            finish();
        } else {

            Intent intent = new Intent(this, WelcomeActivity.class);

            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailed() {
        Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServer() {
        Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
    public void onnotconnect(String msg) {
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();

    }
}