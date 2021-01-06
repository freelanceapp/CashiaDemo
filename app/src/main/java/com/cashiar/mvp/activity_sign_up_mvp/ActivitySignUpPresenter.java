package com.cashiar.mvp.activity_sign_up_mvp;

import android.content.Context;
import android.util.Log;

import com.cashiar.models.SignUpModel;
import com.cashiar.models.UserModel;
import com.cashiar.remote.Api;
import com.cashiar.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignUpPresenter {
    private Context context;
    private ActivitySignUpView view;


    public ActivitySignUpPresenter(Context context, ActivitySignUpView view) {
        this.context = context;
        this.view = view;


    }


    public void checkData(SignUpModel signUpModel) {
        if (signUpModel.isDataValid(context)) {
            sign_up(signUpModel);

        }
    }


    private void sign_up(SignUpModel signUpModel) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .signup(signUpModel.getPhone_code().replace("+", "00"), signUpModel.getPhone(), signUpModel.getName(), signUpModel.getEmail(), "android")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            view.onSignupValid(response.body());
                        } else {
                            try {
                                Log.e("mmmmmmmmmmssss", response.errorBody().string());
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


}
