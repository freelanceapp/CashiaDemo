package com.cashiar.mvp.activity_cart_bill_sell_mvp;

import com.cashiar.models.AllDiscountsModel;
import com.cashiar.models.UserModel;

public interface CartBillSellActivityView {
    void onFinished();
    void onopenpay();
    void onLoad();

    void onFinishload();
    void onFailed(String msg);



    void onprofileload(UserModel body);

}
