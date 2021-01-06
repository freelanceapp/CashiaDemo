package com.cashiar.mvp.activity_home_mvp;

import android.content.Context;
import android.util.Log;

import com.cashiar.R;
import com.cashiar.models.AllAccountsModel;
import com.cashiar.models.UserModel;
import com.cashiar.preferences.Preferences;
import com.cashiar.remote.Api;
import com.cashiar.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityHomePresenter {
    private Context context;
    private HomeActivityView view;
    private Preferences preference;
    private UserModel userModel;

    public ActivityHomePresenter(Context context, HomeActivityView view) {
        this.context = context;
        this.view = view;
        preference = Preferences.getInstance();
        userModel = preference.getUserData(context);

    }


    public void backPress() {
        if (userModel == null) {
            view.onNavigateToLoginActivity();
        } else {
            view.onFinished();
        }

    }

    public void addproducts() {
        view.onAddproducts();


    }
    public void addbillsell() {
        view.onAddbillSell();


    }
    public void addbillBuy() {
        view.onAddbillBuy();


    }
    public void Expenses() {
        view.onExpenses();


    }
    public void customers() {
        view.onCustomers();
    }
    public void suppliers() {
        view.onSuppliers();
    }
    public void updateTokenFireBase(UserModel userModel) {


        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();

                try {

                    Api.getService(Tags.base_url)
                            .updatePhoneToken( token, userModel.getId(), "android")
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Log.e("token", "updated successfully");
                                    } else {
                                        try {

                                            Log.e("error", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    try {

                                        if (t.getMessage() != null) {
                                            Log.e("error", t.getMessage());
                                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                //  Toast.makeText(FireBaseMessaging.this, R.string.something, Toast.LENGTH_SHORT).show();
                                            } else {
                                            //    Toast.makeText(FireBaseMessaging.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            });
                } catch (Exception e) {


                }

            }
        });
    }

    public void share() {
        view.share();
    }

    public void logout(UserModel userModel) {
        view.onLoad();

        Api.getService(Tags.base_url)
                .logout("Bearer " + userModel.getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            view.logout();
                        } else {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess", response.code() + response.errorBody().string());
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

    public void backsale() {
        view.onBacksale();}

    public void backbuy() {
        view.onBackbuy();}

    public void addcashier() {
        view.oncahier();
    }

    public void profile() {
        view.profile();
    }

    public void lang() {
        view.lang();
    }
    public void getprofile(UserModel userModel)
    {
        //   Log.e("tjtjtj",singleDoctorModel.getId()+"  "+user_id);
        view.onLoad();

        Api.getService(Tags.base_url)
                .getprofile("Bearer "+userModel.getToken())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();

                        if (response.isSuccessful() && response.body() != null) {
                            view.onprofileload(response.body());
                        } else {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_codess",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
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
