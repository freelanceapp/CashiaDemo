package com.cashiar.mvp.activity_edit_cashier_profile;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cashiar.R;
import com.cashiar.models.EditProfileCahierModel;
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

public class ActivityEditCahierprofilePresenter {
    private UserModel userModel;
    private Preferences preferences;
    private EditprofileCahierActivityView view;
    private Context context;

    public ActivityEditCahierprofilePresenter(EditprofileCahierActivityView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void backPress() {

        view.onFinished();


    }

    public void checkData(EditProfileCahierModel editProfileModel, UserModel userModel) {
        if (editProfileModel.isDataValid(context)) {

            editprofile(editProfileModel, userModel);

        }
    }


    private void editprofile(EditProfileCahierModel editProfileModel, UserModel userModel) {
        view.onLoad();
        Api.getService(Tags.base_url)
                .Editprofilecahier("Bearer " + userModel.getToken(), editProfileModel.getName(), editProfileModel.getPhone_code(), editProfileModel.getPhone(), editProfileModel.getAddress(), editProfileModel.getLongutide() + "", editProfileModel.getLatuide() + "")
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


                                view.AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng(),response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", ""));
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

}
