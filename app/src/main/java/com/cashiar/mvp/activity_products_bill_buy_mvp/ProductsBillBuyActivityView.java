package com.cashiar.mvp.activity_products_bill_buy_mvp;

import com.cashiar.models.UserModel;

public interface ProductsBillBuyActivityView {
    void onFinished();
    void onCart();
    void onLoad();
    void onFinishload();
    void onFailed(String msg);
    void onprofileload(UserModel body);

}
