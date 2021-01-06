package com.cashiar.mvp.activity_add_product_mvp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cashiar.R;
import com.cashiar.models.AddPRoductModel;
import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.AllColorsModel;
import com.cashiar.models.SingleProductModel;
import com.cashiar.models.UserModel;
import com.cashiar.preferences.Preferences;
import com.cashiar.remote.Api;
import com.cashiar.share.Common;
import com.cashiar.tags.Tags;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddProductPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private AddproductActivityView view;
    private Context context;

    public ActivityAddProductPresenter(AddproductActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }
    public void backPress() {

            view.onFinished();


    }
    public void checkData(AddPRoductModel addPRoductModel,UserModel userModel)
    {
        if (addPRoductModel.isDataValid(context)){
            if(addPRoductModel.getShowinsell().equals("color")){
addProduct(addPRoductModel,userModel);}

        else {
            addProductwithimage(addPRoductModel,userModel,context);
        }}
    }
    public void checkDataupdate(AddPRoductModel addPRoductModel, UserModel userModel, SingleProductModel singleProductModel)
    {
        if (addPRoductModel.isDataValid(context)){
            if(addPRoductModel.getShowinsell().equals("color")){
                updateProduct(addPRoductModel,userModel,singleProductModel);}

            else {
                updateProductwithimage(addPRoductModel,userModel,context,singleProductModel);
            }}
    }
    private void addProductwithimage(
            AddPRoductModel addPRoductModel, UserModel userModel,Context context) {
        Log.e("dlldldl","dlkkdkdk");
        view.onLoad();
        RequestBody name_part = Common.getRequestBodyText(addPRoductModel.getName());
        RequestBody sellby_part = Common.getRequestBodyText(addPRoductModel.getSellBy());
        RequestBody cost_part = Common.getRequestBodyText(addPRoductModel.getCost());
        RequestBody price_part = Common.getRequestBodyText(addPRoductModel.getPrice());

        RequestBody sku_part = Common.getRequestBodyText(addPRoductModel.getSku());
        RequestBody barcode_part = Common.getRequestBodyText(addPRoductModel.getBarcode());
        RequestBody stock_part = Common.getRequestBodyText(addPRoductModel.getStock());
        RequestBody stockamount_part = Common.getRequestBodyText(addPRoductModel.getStokamount());
        RequestBody showin_part = Common.getRequestBodyText(addPRoductModel.getShowinsell());
        RequestBody color_part = Common.getRequestBodyText(addPRoductModel.getColor());
        RequestBody depart_part = Common.getRequestBodyText(addPRoductModel.getDepartment());



        MultipartBody.Part image_form_part = Common.getMultiPart(context, Uri.parse(addPRoductModel.getImage_url()), "image");


        Api.getService(Tags.base_url).
                addproductwithimage("Bearer "+userModel.getToken(),name_part, sellby_part, cost_part, price_part, sku_part, barcode_part, stock_part, stockamount_part, showin_part, color_part,depart_part, image_form_part)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {

                            view.onSuccess();
                        } else {
                            try {
                                view.onFailed(response.message());
                                Log.e("jdjjdjjd", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                           if (response.code() == 422) {
                                //Toast.makeText(SignUpDriverActivity.this, R.string.user_found, Toast.LENGTH_SHORT).show();
                               view.onFailed(response.message());

                            } else {
                                try {
                                    view.onFailed(response.message());
                                    Log.e("jdjjdjjd", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                                    view.onFailed(t.getMessage());

                                    //  Toast.makeText(SignUpDriverActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed(t.getMessage());

                                    // Toast.makeText(SignUpDriverActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void addProduct(AddPRoductModel addPRoductModel,UserModel userModel) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .addproduct("Bearer "+userModel.getToken(), addPRoductModel.getName(),addPRoductModel.getSellBy(),addPRoductModel.getCost(),addPRoductModel.getPrice(),addPRoductModel.getSku(),addPRoductModel.getBarcode(),addPRoductModel.getStock(),addPRoductModel.getStokamount(),addPRoductModel.getShowinsell(),addPRoductModel.getColor(),addPRoductModel.getDepartment())
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
    private void updateProductwithimage(
            AddPRoductModel addPRoductModel, UserModel userModel,Context context,SingleProductModel singleProductModel) {
        Log.e("dlldldl","dlkkdkdk");
        view.onLoad();
        RequestBody name_part = Common.getRequestBodyText(addPRoductModel.getName());
        RequestBody sellby_part = Common.getRequestBodyText(addPRoductModel.getSellBy());
        RequestBody cost_part = Common.getRequestBodyText(addPRoductModel.getCost());
        RequestBody price_part = Common.getRequestBodyText(addPRoductModel.getPrice());

        RequestBody sku_part = Common.getRequestBodyText(addPRoductModel.getSku());
        RequestBody barcode_part = Common.getRequestBodyText(addPRoductModel.getBarcode());
        RequestBody stock_part = Common.getRequestBodyText(addPRoductModel.getStock());
        RequestBody stockamount_part = Common.getRequestBodyText(addPRoductModel.getStokamount());
        RequestBody showin_part = Common.getRequestBodyText(addPRoductModel.getShowinsell());
        RequestBody color_part = Common.getRequestBodyText(addPRoductModel.getColor());
        RequestBody depart_part = Common.getRequestBodyText(addPRoductModel.getDepartment());
        RequestBody product_part = Common.getRequestBodyText(singleProductModel.getId()+"");



        MultipartBody.Part image_form_part = Common.getMultiPart(context, Uri.parse(addPRoductModel.getImage_url()), "image");


        Api.getService(Tags.base_url).
                updtaeproductwithimage("Bearer "+userModel.getToken(),name_part, sellby_part, cost_part, price_part, sku_part, barcode_part, stock_part, stockamount_part, showin_part, color_part,depart_part,product_part, image_form_part)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {

                            view.onSuccess();
                        } else {
                            try {
                                view.onFailed(response.message());
                                Log.e("jdjjdjjd", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 422) {
                                //Toast.makeText(SignUpDriverActivity.this, R.string.user_found, Toast.LENGTH_SHORT).show();
                                view.onFailed(response.message());

                            } else {
                                try {
                                    view.onFailed(response.message());
                                    Log.e("jdjjdjjd", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                                    view.onFailed(t.getMessage());

                                    //  Toast.makeText(SignUpDriverActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    view.onFailed(t.getMessage());

                                    // Toast.makeText(SignUpDriverActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateProduct(AddPRoductModel addPRoductModel,UserModel userModel,SingleProductModel singleProductModel) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .updateproduct("Bearer "+userModel.getToken(), addPRoductModel.getName(),addPRoductModel.getSellBy(),addPRoductModel.getCost(),addPRoductModel.getPrice(),addPRoductModel.getSku(),addPRoductModel.getBarcode(),addPRoductModel.getStock(),addPRoductModel.getStokamount(),addPRoductModel.getShowinsell(),addPRoductModel.getColor(),addPRoductModel.getDepartment(),singleProductModel.getId()+"")
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
    public void getcategories(UserModel userModel)
    {
        // Log.e("tjtjtj",userModel.getIs_confirmed());
        view.onLoad();

        Api.getService(Tags.base_url)
                .getproductcategories("Bearer "+userModel.getToken())
                .enqueue(new Callback<AllCategoryModel>() {
                    @Override
                    public void onResponse(Call<AllCategoryModel> call, Response<AllCategoryModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            view.onSuccess(response.body());
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
                    public void onFailure(Call<AllCategoryModel> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getString(R.string.something));
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

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
}
