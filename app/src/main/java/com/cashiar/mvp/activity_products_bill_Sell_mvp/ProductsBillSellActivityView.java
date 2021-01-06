package com.cashiar.mvp.activity_products_bill_Sell_mvp;

import com.cashiar.models.UserModel;

public interface ProductsBillSellActivityView {
    void onFinished();
    void onCart();

    void onLoad();
    void onFinishload();

    void onFailed(String msg);

    void onprofileload(UserModel body);
}
