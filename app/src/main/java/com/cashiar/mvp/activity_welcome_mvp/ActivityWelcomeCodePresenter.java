package com.cashiar.mvp.activity_welcome_mvp;

import android.content.Context;
import android.util.Log;

import com.cashiar.models.UserModel;
import com.cashiar.remote.Api;
import com.cashiar.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityWelcomeCodePresenter {
    private Context context;
    private ActivityWelcomeCodeView view;


    public ActivityWelcomeCodePresenter(Context context, ActivityWelcomeCodeView view) {
        this.context = context;
        this.view = view;


    }


    public void getuser(String token) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .getuser("Bearer "+token)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getIs_confirmed().equals("accepted")) {
                                view.accepted(response.body());
                            }
                            //  Log.e("eeeeee", response.body().getUser().getName());

                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                view.onServer();
                            } else {
                                view.onFailed();
                                //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onnotconnect(t.getMessage().toLowerCase());
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed();
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

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
                                               // Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

}
