package com.cashiar.mvp.activity_products_buy_mvp;

import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.AllProductsModel;
import com.cashiar.models.SingleProductModel;
import com.cashiar.models.UserModel;

public interface ProductsBuyActivityView {
    void onFinished();
    void onCart();

    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);
    void onSuccess(AllCategoryModel allCategoryModel);
    void onproductSuccess(AllProductsModel allProductsModel);
    void onproductSuccess(SingleProductModel allProductsModel);

    void onLoad();
    void onFinishload();


    void onprofileload(UserModel body);
}
