package com.cashiar.mvp.activity_editprofile_mvp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cashiar.R;
import com.cashiar.models.EditProfileModel;
import com.cashiar.models.PlaceGeocodeData;
import com.cashiar.models.PlaceMapDetailsData;
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

public class ActivityEditprofilePresenter {
    private UserModel userModel;
    private Preferences preferences;
    private EditprofileActivityView view;
    private Context context;

    public ActivityEditprofilePresenter(EditprofileActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void backPress() {

        view.onFinished();


    }

    public void checkData(EditProfileModel editProfileModel, UserModel userModel) {
        if (editProfileModel.isDataValid(context)) {
            if (!editProfileModel.getImage_url().isEmpty()) {
                editprofileimage(editProfileModel, userModel, context);
            } else {
                editprofile(editProfileModel, userModel);
            }
        }
    }

    private void editprofileimage(
            EditProfileModel editProfileModel, UserModel userModel, Context context) {
       // Log.e("dlldldl", "dlkkdkdk");
        view.onLoad();
        RequestBody name_part = Common.getRequestBodyText(editProfileModel.getName());
        RequestBody phonecode_part = Common.getRequestBodyText(editProfileModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(editProfileModel.getPhone());
        RequestBody address_part = Common.getRequestBodyText(editProfileModel.getAddress());

        RequestBody long_part = Common.getRequestBodyText(editProfileModel.getLongutide() + "");
        RequestBody lat_part = Common.getRequestBodyText(editProfileModel.getLatuide() + "");
        RequestBody currency_part = Common.getRequestBodyText(editProfileModel.getCurrency());
        RequestBody taxamount_part = Common.getRequestBodyText(editProfileModel.getTaxamount());


        MultipartBody.Part image_form_part = Common.getMultiPart(context, Uri.parse(editProfileModel.getImage_url()), "logo");


        Api.getService(Tags.base_url).
                Editprofilewithimage("Bearer " + userModel.getToken(), name_part, phonecode_part, phone_part, address_part, long_part, lat_part, currency_part, taxamount_part, image_form_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {

                            view.onSuccess(response.body());
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
                    public void onFailure(Call<UserModel> call, Throwable t) {
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

    private void editprofile(EditProfileModel editProfileModel, UserModel userModel) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .Editprofile("Bearer " + userModel.getToken(), editProfileModel.getName(), editProfileModel.getPhone_code(), editProfileModel.getPhone(), editProfileModel.getAddress(), editProfileModel.getLongutide() + "", editProfileModel.getLatuide() + "", editProfileModel.getCurrency(), editProfileModel.getTaxamount())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {
                            //  Log.e("eeeeee", response.body().getUser().getName());
                            // view.onUserFound(response.body());
                            view.onSuccess(response.body());
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
                    public void onFailure(Call<UserModel> call, Throwable t) {
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

    public void Search(String query, String lang) {

        view.onLoad();
        String fields = "id,place_id,name,geometry,formatted_address";

        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, lang, context.getResources().getString(R.string.mapapikey))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getCandidates().size() > 0) {


                                view.AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng(), response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", ""));
                            }
                        } else {
                            view.onFinishload();
                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(context.getResources().getString(R.string.something));
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void getGeoData(final double lat, double lng, String lang) {
        view.onLoad();
        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, lang, context.getResources().getString(R.string.mapapikey))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        view.onFinishload();
                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getResults().size() > 0) {
                                view.onaddress(response.body());
                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {
                            view.onFinishload();
                            view.onFailed(t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
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
