package com.cashiar.ui.activity_welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivityWelcomeBinding;
import com.cashiar.language.Language;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_welcome_mvp.ActivityWelcomeCodePresenter;
import com.cashiar.mvp.activity_welcome_mvp.ActivityWelcomeCodeView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_home.HomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity implements ActivityWelcomeCodeView {
    private ActivityWelcomeBinding binding;

    private ActivityWelcomeCodePresenter presenter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        initView();
    }


    private void initView() {
        EventBus.getDefault().register(this);

        preferences = Preferences.getInstance();

        presenter = new ActivityWelcomeCodePresenter(this, this);

        presenter.getuser(preferences.getUserData(this).getToken());
        presenter.updateTokenFireBase(preferences.getUserData(this));
    }


    @Override
    public void onFailed() {
        Toast.makeText(WelcomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServer() {
        Toast.makeText(WelcomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(WelcomeActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void accepted(UserModel userModel) {
        preferences.create_update_userdata(WelcomeActivity.this, userModel);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNotifications(UserModel userModel) {
        presenter.getuser(preferences.getUserData(this).getToken());


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}