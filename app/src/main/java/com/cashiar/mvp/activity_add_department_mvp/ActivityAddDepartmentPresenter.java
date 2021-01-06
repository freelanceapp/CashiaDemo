package com.cashiar.mvp.activity_add_department_mvp;

import android.content.Context;
import android.util.Log;

import com.cashiar.R;
import com.cashiar.models.AddDepartmentModel;
import com.cashiar.models.AllColorsModel;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.models.UserModel;
import com.cashiar.preferences.Preferences;
import com.cashiar.remote.Api;
import com.cashiar.tags.Tags;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddDepartmentPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private AddDepartmentActivityView view;
    private Context context;

    public ActivityAddDepartmentPresenter(AddDepartmentActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }
    public void backPress() {

            view.onFinished();


    }
    public void checkData(AddDepartmentModel addDepartmentModel, UserModel userModel)
    {
        if (addDepartmentModel.isDataValid(context)){
adddepartment(addDepartmentModel,userModel);
        }
    }
    public void checkDataupdate(AddDepartmentModel addDepartmentModel, UserModel userModel, SingleCategoryModel singleCategoryModel)
    {
        if (addDepartmentModel.isDataValid(context)){
            updateepartment(addDepartmentModel,userModel,singleCategoryModel);
        }
    }

    private void updateepartment(AddDepartmentModel addDepartmentModel, UserModel userModel, SingleCategoryModel singleCategoryModel) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .updatedepartment("Bearer "+userModel.getToken(), addDepartmentModel.getName(),"color",addDepartmentModel.getColor(),singleCategoryModel.getId()+"")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            // view.onUserFound(response.body());
                            view.onSuccess();
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                // view.onServer();
                            } else {
                                view.onFailed(response.message());
                                //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //   view.onnotconnect(t.getMessage().toLowerCase());
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed(t.getMessage());
                                    // Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    public void getcolors()
    {

        Api.getService(Tags.base_url)
                .getcolors()
                .enqueue(new Callback<AllColorsModel>() {
                    @Override
                    public void onResponse(Call<AllColorsModel> call, Response<AllColorsModel> response) {
                        // view.onProgressHide();
                        if (response.isSuccessful() && response.body() != null) {
                            view.oncolorSuccess(response.body());
                        } else {
                            // view.onProgressHide();
                            view.onFailed(context.getString(R.string.something));
                            try {
                                Log.e("error_code",response.code()+  response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<AllColorsModel> call, Throwable t) {
                        try {
                            //view.onProgressHide();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }
    private void adddepartment(AddDepartmentModel addDepartmentModel, UserModel userModel) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .adddepartment("Bearer "+userModel.getToken(), addDepartmentModel.getName(),"color",addDepartmentModel.getColor())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            // view.onUserFound(response.body());
                            view.onSuccess();
                        } else {
                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                // view.onServer();
                            } else {
                                view.onFailed(response.message());
                                //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            view.onFinishload();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //   view.onnotconnect(t.getMessage().toLowerCase());
                                    //  Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed(t.getMessage());
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
