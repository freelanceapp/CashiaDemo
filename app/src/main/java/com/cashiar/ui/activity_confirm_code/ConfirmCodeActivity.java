package com.cashiar.ui.activity_confirm_code;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivityVerficationcodeBinding;
import com.cashiar.language.Language;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_confirm_code_mvp.ActivityConfirmCodePresenter;
import com.cashiar.mvp.activity_confirm_code_mvp.ActivityConfirmCodeView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_home.HomeActivity;
import com.cashiar.ui.activity_login.LoginActivity;
import com.cashiar.ui.activity_sign_up.SignUpActivity;
import com.cashiar.ui.activity_welcome.WelcomeActivity;

import io.paperdb.Paper;

public class ConfirmCodeActivity extends AppCompatActivity implements ActivityConfirmCodeView {
    private ActivityVerficationcodeBinding binding;
    private String phone_code = "";
    private String phone = "";
    private boolean canSend = false;
    private ActivityConfirmCodePresenter presenter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verficationcode);
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
        String mPhone = phone_code + phone;
        binding.setPhone(mPhone);
        presenter = new ActivityConfirmCodePresenter(this, this, phone, phone_code);


        binding.btnConfirm.setOnClickListener(v -> {
            String sms = binding.edtCode.getText().toString().trim();
            if (!sms.isEmpty()) {
                presenter.checkValidCode(sms);
            } else {
                binding.edtCode.setError(getString(R.string.inv_code));
            }
        });
        binding.tvResendCode.setOnClickListener(view -> {
            if (canSend) {
                canSend = false;
                presenter.resendCode();
            }
        });
    }


    @Override
    public void onCounterStarted(String time) {
        binding.tvResendCode.setText(time + "");
        binding.tvResendCode.setTextColor(ContextCompat.getColor(ConfirmCodeActivity.this, R.color.colorPrimary));
        binding.tvResendCode.setBackgroundResource(R.color.transparent);
    }

    @Override
    public void onCounterFinished() {
        canSend = true;
        binding.tvResendCode.setText(R.string.resend);
        binding.tvResendCode.setTextColor(ContextCompat.getColor(ConfirmCodeActivity.this, R.color.gray4));
        binding.tvResendCode.setBackgroundResource(R.color.white);
    }

    @Override
    public void onCodeFailed(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCodeSent(String msg) {
        binding.edtCode.setText(msg);
    }

    @Override
    public void onUserFound(UserModel userModel) {
        preferences.create_update_userdata(ConfirmCodeActivity.this, userModel);
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
    public void onUserNoFound() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone_code", phone_code);
        intent.putExtra("phone", phone);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailed() {
        Toast.makeText(ConfirmCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServer() {
        Toast.makeText(ConfirmCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(ConfirmCodeActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopTimer();
    }
}