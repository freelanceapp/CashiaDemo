package com.cashiar.mvp.activity_add_department_mvp;

import com.cashiar.models.AllColorsModel;

public interface AddDepartmentActivityView {
    void onFinished();

    void onFailed(String msg);


    void onLoad();

    void onFinishload();



    void oncolorSuccess(AllColorsModel allColorsModel);
    void onSuccess();

}
