package com.cashiar.mvp.activity_add_product_mvp;

import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.AllColorsModel;

public interface AddproductActivityView {
    void onFinished();

    void onLoad();

    void onFinishload();

    void onFailed(String msg);

    void onSuccess(AllCategoryModel allCategoryModel);

    void oncolorSuccess(AllColorsModel allColorsModel);
    void onSuccess();

}
