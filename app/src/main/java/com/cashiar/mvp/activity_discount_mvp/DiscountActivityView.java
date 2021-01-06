package com.cashiar.mvp.activity_discount_mvp;

import com.cashiar.models.AllDiscountsModel;
import com.cashiar.models.UserModel;

public interface DiscountActivityView {
    void onFinished();
    void onAddDiscount();
    void onFailed(String msg);

    void onProgressShow();
    void onProgressHide();
    void ondiscountSuccess(AllDiscountsModel allDiscountsModel);
    void onLoad();

    void onFinishload();



    void onprofileload(UserModel body);

    void onSuccessDelete();
}
