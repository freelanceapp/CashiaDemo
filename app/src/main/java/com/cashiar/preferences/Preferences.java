package com.cashiar.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.cashiar.models.CreateBuyOrderModel;
import com.cashiar.models.CreateOrderModel;
import com.cashiar.models.UserModel;
import com.cashiar.tags.Tags;
import com.google.gson.Gson;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }






   public void create_update_userdata(Context context, UserModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

  public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }
    public void create_update_session(Context context, String session) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", session);
        editor.apply();


    }


    public void create_room_id(Context context, String room_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("room_id", room_id);
        editor.apply();


    }

    public String getRoomId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        String room_id = preferences.getString("room_id", Tags.session_logout);
        return room_id;
    }

    public String getSession(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }

    public void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
        SharedPreferences preferences2 = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit2 = preferences2.edit();
        edit2.clear();
        edit2.apply();

        SharedPreferences preferences3 = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit3 = preferences3.edit();
        edit3.clear();
        edit3.apply();
        create_update_session(context, Tags.session_logout);
    }
    public void create_update_cart(Context context , CreateOrderModel model)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cart_data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_data", cart_data);
        editor.apply();

    }

    public CreateOrderModel getCartData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String json_data = sharedPreferences.getString("cart_data","");
        Gson gson = new Gson();
        CreateOrderModel model = gson.fromJson(json_data,CreateOrderModel.class);
        return model;
    }

    public void clearCartbuy(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cartbuy", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }
    public void create_update_cartbuy(Context context , CreateBuyOrderModel model)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cartbuy", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cart_data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_databuy", cart_data);
        editor.apply();

    }

    public CreateBuyOrderModel getCartDatabuy(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cartbuy", Context.MODE_PRIVATE);
        String json_data = sharedPreferences.getString("cart_databuy","");
        Gson gson = new Gson();
        CreateBuyOrderModel model = gson.fromJson(json_data,CreateBuyOrderModel.class);
        return model;
    }

    public void clearCart(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }
    public void clearCartbillsell(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cartbillsell", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }
    public void create_update_cartbillsell(Context context , CreateOrderModel model)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cartbillsell", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cart_data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_billsell", cart_data);
        editor.apply();

    }

    public CreateOrderModel getCartDatabillsell(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cartbillsell", Context.MODE_PRIVATE);
        String json_data = sharedPreferences.getString("cart_billsell","");
        Gson gson = new Gson();
        CreateOrderModel model = gson.fromJson(json_data,CreateOrderModel.class);
        return model;
    }
    public void clearCartbillbuy(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cartbillbuy", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.apply();
    }
    public void create_update_cartbillbuy(Context context , CreateBuyOrderModel model)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cartbillbuy", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cart_data = gson.toJson(model);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_billbuy", cart_data);
        editor.apply();

    }

    public CreateBuyOrderModel getCartDatabillbuy(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cartbillbuy", Context.MODE_PRIVATE);
        String json_data = sharedPreferences.getString("cart_billbuy","");
        Gson gson = new Gson();
        CreateBuyOrderModel model = gson.fromJson(json_data,CreateBuyOrderModel.class);
        return model;
    }
}
