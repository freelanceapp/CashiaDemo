package com.cashiar.mvp.activity_customers_mvp;

import com.cashiar.models.AllCustomersModel;
import com.cashiar.models.Slider_Model;
import com.cashiar.models.UserModel;

import java.util.List;

public interface CustomersActivityView {
    void onFinished();
    void onCustomers();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);
    void onSuccess(AllCustomersModel allCategoryModel);
    void onProgressSliderShow();
    void onProgressSliderHide();
    void onSliderSuccess(List<Slider_Model.Data> sliderModelList);
    void onLoad();

    void onFinishload();



    void onprofileload(UserModel body);

    void onSuccessDelete();
}
