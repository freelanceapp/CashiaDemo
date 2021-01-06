package com.cashiar.mvp.activity_all_bill_buy_mvp;

import com.cashiar.models.AllBillOfSellModel;
import com.cashiar.models.UserModel;


public interface AllBillBuyActivityView {
    void onFinished();

    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);
    void onSuccess(AllBillOfSellModel allBillOfSellModel);
    void onLoad();

    void onFinishload();



    void onprofileload(UserModel body);
}
