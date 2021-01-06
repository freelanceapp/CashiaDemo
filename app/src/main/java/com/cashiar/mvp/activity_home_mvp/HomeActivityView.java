package com.cashiar.mvp.activity_home_mvp;

import com.cashiar.models.UserModel;

public interface HomeActivityView {
    void onNavigateToLoginActivity();
    void onFinished();
    void onAddproducts();
    void onCustomers();
    void onSuppliers();

    void onAddbillSell();
    void onAddbillBuy();
    void onExpenses();
    void share();
    void onLoad();

    void onFinishload();


    void logout();
    void onFailed(String msg);

    void onBacksale();

    void onBackbuy();

    void oncahier();

    void profile();

    void lang();

    void onprofileload(UserModel body);
}
