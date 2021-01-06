package com.cashiar.mvp.activity_bill_sell_mvp;

import com.cashiar.models.AllCustomersModel;
import com.cashiar.models.UserModel;

public interface BillSellActivityView {
    void onFinished();

    void onFailed(String msg);
    void onLoad();
    void onFinishload();
    void onCustomers();
void onsucess();

    void onprofileload(UserModel body);
}
