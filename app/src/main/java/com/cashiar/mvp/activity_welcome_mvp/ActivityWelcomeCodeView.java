package com.cashiar.mvp.activity_welcome_mvp;


import com.cashiar.models.UserModel;

public interface ActivityWelcomeCodeView {

    void onFailed();
    void onServer();
    void onLoad();
    void onFinishload();
    void onnotconnect(String msg);
    void accepted(UserModel userModel);


}
