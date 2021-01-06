package com.cashiar.mvp.activity_edit_cashier_profile;

import com.cashiar.models.PlaceGeocodeData;

public interface EditprofileCahierActivityView {
    void onFinished();

    void onFailed(String msg);


    void onLoad();

    void onFinishload();

    void onSuccess();

    void AddMarker(double lat, double lng, String replace);

    void onaddress(PlaceGeocodeData body);
}
