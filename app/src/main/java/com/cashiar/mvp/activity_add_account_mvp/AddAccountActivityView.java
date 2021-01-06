package com.cashiar.mvp.activity_add_account_mvp;

public interface AddAccountActivityView {
    void onFinished();

    void onLoad();

    void onFinishload();


    void onFailed(String msg);

    void onSuccess();


}
