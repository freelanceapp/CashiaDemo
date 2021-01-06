package com.cashiar.mvp.activity_splash_mvp;

import android.content.Context;
import android.os.Handler;

import com.cashiar.models.UserModel;
import com.cashiar.preferences.Preferences;


public class SplashPresenter {
    private Context context;
    private SplashView view;
    private Preferences preferences;
    private UserModel userModel;

    public SplashPresenter(Context context, SplashView view) {
        this.context = context;
        this.view = view;
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(context);
        delaySplash();
    }

    public void delaySplash(){
        new Handler().postDelayed(()->{

                view.onNavigateToLocationActivity();




        },2000);
    }
}
