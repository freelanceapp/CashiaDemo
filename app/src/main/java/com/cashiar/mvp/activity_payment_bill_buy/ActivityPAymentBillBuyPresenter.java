package com.cashiar.mvp.activity_payment_bill_buy;

import android.content.Context;
import android.util.Log;

import com.cashiar.R;
import com.cashiar.models.CreateBuyOrderModel;
import com.cashiar.models.CreateOrderModel;
import com.cashiar.models.PaymentModel;
import com.cashiar.models.UserModel;
import com.cashiar.preferences.Preferences;
import com.cashiar.remote.Api;
import com.cashiar.tags.Tags;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPAymentBillBuyPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private PaymentBillBuyActivityView view;
    private Context context;

    public ActivityPAymentBillBuyPresenter(PaymentBillBuyActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }
    public void backPress() {

            view.onFinished();


    }
    public void checkData(PaymentModel paymentModel, CreateBuyOrderModel createOrderModel, UserModel userModel)
    {
        if (paymentModel.isDataValid(context)){
            Create_order(userModel,createOrderModel);
        }
    }

    public void Create_order(UserModel userModel, CreateBuyOrderModel createOrderModel)
    {
        view.onLoad();

        Api.getService(Tags.base_url)
                .createBackbuy("Bearer "+userModel.getToken(),createOrderModel)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                          //  view.ondcustomerSuccess(response.body());
                            view.onsucess();
                        } else {
                            view.onFinishload();
                            view.onFailed(context.getResources().getString(R.string.something));
                            try {
                                Log.e("error_code",response.code()+  response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

}
