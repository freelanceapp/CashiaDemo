package com.cashiar.mvp.activity_add_discount_mvp;

public interface AddDiscountActivityView {
    void onFinished();

    void onLoad();

    void onFinishload();


    void onFailed(String msg);

    void onSuccess();
}
