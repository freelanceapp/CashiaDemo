package com.cashiar.mvp.activity_add_customer_mvp;

public interface AddCustomerActivityView {
    void onFinished();

    void onFailed(String msg);


    void onLoad();

    void onFinishload();

    void onSuccess();

}
