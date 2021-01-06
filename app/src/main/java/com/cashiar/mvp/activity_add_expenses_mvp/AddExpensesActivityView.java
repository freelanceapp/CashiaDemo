package com.cashiar.mvp.activity_add_expenses_mvp;

import com.cashiar.models.AllAccountsModel;
import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.AllProductsModel;

public interface AddExpensesActivityView {
    void onFinished();

    void onFailed(String msg);

    void onSuccess(AllAccountsModel allAccountsModel);

    void onLoad();

    void onFinishload();

    void onSuccess();

    void onAccount();

    void onDateSelected(String date);

}
