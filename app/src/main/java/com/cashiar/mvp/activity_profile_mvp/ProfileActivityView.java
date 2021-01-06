package com.cashiar.mvp.activity_profile_mvp;

import com.cashiar.models.UserModel;

public interface ProfileActivityView {
    void onFinished();

    void onLoad();

    void onFinishload();

    void onFailed(String msg);


    void onprofileload(UserModel body);


    void update();
}
