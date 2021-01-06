package com.cashiar.mvp.activity_products_mvp;

import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.AllProductsModel;
import com.cashiar.models.UserModel;

public interface ProductsActivityView {
    void onFinished();
    void onAddproducts();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);
    void onSuccess(AllCategoryModel allCategoryModel);
    void onproductSuccess(AllProductsModel allProductsModel);
    void onLoad();
    void onFinishload();

    void onSuccessDelete();

    void onprofileload(UserModel body);
}
