package com.cashiar.mvp.activity_payment_sell_mvp;

import com.cashiar.models.AllCustomersModel;
import com.cashiar.models.BillModel;
import com.cashiar.models.UserModel;

public interface PaymentSellActivityView {
    void onFinished();

    void onFailed(String msg);
    void onLoad();
    void onFinishload();
    void ondcustomerSuccess(AllCustomersModel allCustomersModel);
    void onCustomers();
void onsucess(BillModel body);

    void onprofileload(UserModel body);
}
