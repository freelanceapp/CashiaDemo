package com.cashiar.ui.activity_cart_buy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.CartSellBuyAdapter;
import com.cashiar.adapters.SpinnerDiscountAdapter;
import com.cashiar.adapters.Swipe;
import com.cashiar.databinding.ActivityCartBuyBinding;
import com.cashiar.databinding.DialogInpiutBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllDiscountsModel;
import com.cashiar.models.CreateBuyOrderModel;
import com.cashiar.models.ItemCartModel;
import com.cashiar.models.SingleDiscountModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_cart_buy_mvp.ActivityCartBuyPresenter;
import com.cashiar.mvp.activity_cart_buy_mvp.CartBuyActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_pay_buy.PaymentBuyActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartBuyActivity extends AppCompatActivity implements CartBuyActivityView, Swipe.SwipeListener {
    private ActivityCartBuyBinding binding;
    private ActivityCartBuyPresenter presenter;
    private String lang;
    private CartSellBuyAdapter cartSellBuyAdapter;
    private List<ItemCartModel> itemCartModelList;
    private Preferences preferences;
    private float dX = 0, dY = 0;
    private float downRawX, downRawY;
    private CreateBuyOrderModel createOrderModel;
    private double total;
    private ProgressDialog dialog;
    private SpinnerDiscountAdapter spinnerDiscountAdapter;
    private List<SingleDiscountModel> singleDiscountModels;
    private UserModel userModel;
    private int coupon_id;
    private double discount;
    private String type;
    private String currency = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart_buy);
        initView();
    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        itemCartModelList = new ArrayList<>();
        singleDiscountModels = new ArrayList<>();
        getdata();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityCartBuyPresenter(this, this);
        presenter.getdiscount(userModel);
        cartSellBuyAdapter = new CartSellBuyAdapter(itemCartModelList, this, currency);
        presenter.getprofile(userModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(cartSellBuyAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new Swipe(this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(binding.recView);
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.checkdata();
            }
        });
        binding.spcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    coupon_id = 0;
                    discount=0;
                    type="";
                    calculateTotal();
                } else {
                    coupon_id = singleDiscountModels.get(i).getId();
                    discount = singleDiscountModels.get(i).getValue();
                    type = singleDiscountModels.get(i).getType();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        presenter.backPress();
    }


    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onopenpay() {

        createOrderModel.setCoupon_id(coupon_id);
        if (coupon_id != 0) {
            createOrderModel.setDiscount_value(discount);

            if (type.equals("pre")) {
                total = total - ((total * discount) / 100);
            } else {
                total = total - discount;
            }

        }
        preferences.create_update_cartbuy(this, createOrderModel);
        Intent intent = new Intent(this, PaymentBuyActivity.class);
        intent.putExtra("total", total);
        startActivityForResult(intent, 1);

    }


    @Override
    public void onSwipe(int pos, int dir) {
        deleteItem(pos);
    }


    public void increase_decrease(ItemCartModel model, int adapterPosition) {
        itemCartModelList.set(adapterPosition, model);
        cartSellBuyAdapter.notifyItemChanged(adapterPosition);
        createOrderModel.setOrder_details(itemCartModelList);
        preferences.create_update_cartbuy(this, createOrderModel);
        calculateTotal();
    }

    private void calculateTotal() {
        total = 0;
        for (ItemCartModel model : itemCartModelList) {

            total += model.getAmount() * model.getPrice_value();

        }

        binding.tvTotal.setText(String.format(Locale.ENGLISH, "%s %s", String.valueOf(total), getString(R.string.sar)));
    }

    public void deleteItem(int adapterPosition) {
        itemCartModelList.remove(adapterPosition);
        cartSellBuyAdapter.notifyItemRemoved(adapterPosition);
        createOrderModel.setOrder_details(itemCartModelList);
        preferences.create_update_cartbuy(this, createOrderModel);
        calculateTotal();
        if (itemCartModelList.size() == 0) {
            binding.llEmptyCart.setVisibility(View.VISIBLE);
            binding.consTotal.setVisibility(View.GONE);
            preferences.clearCart(this);
        }
    }

    public void CreateDialogAlert(Context context, int pos) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogInpiutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_inpiut, null, false);


        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     String amount = binding.edtwieght.getText().toString();
                                                     if (!amount.isEmpty()) {
                                                         itemCartModelList.get(pos).setAmount2(Double.parseDouble(amount));
                                                         itemCartModelList.get(pos).setAmount(Double.parseDouble(amount) * itemCartModelList.get(pos).getAmount());
                                                         createOrderModel.setOrder_details(itemCartModelList);
                                                         preferences.create_update_cartbuy(CartBuyActivity.this, createOrderModel);
                                                         cartSellBuyAdapter.notifyDataSetChanged();

                                                         dialog.dismiss();
                                                     } else {
                                                         binding.edtwieght.setError(context.getResources().getString(R.string.field_required));
                                                     }
                                                 }
                                             }

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_App;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    @Override
    public void onLoad() {
        if (dialog == null) {
            dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
        } else {
            dialog.dismiss();
        }
        dialog.show();
    }

    @Override
    public void onFinishload() {
        dialog.dismiss();
    }


    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onServer() {

    }

    @Override
    public void ondiscountSuccess(AllDiscountsModel model) {
        if (lang.equals("en")) {
            singleDiscountModels.add(new SingleDiscountModel("choose"));
        } else {

            singleDiscountModels.add(new SingleDiscountModel("اختر"));
        }
        //Log.e("dlldldl",model.getData().size()+"");
        singleDiscountModels.addAll(model.getData());
        spinnerDiscountAdapter = new SpinnerDiscountAdapter(singleDiscountModels, this);
        binding.spcat.setAdapter(spinnerDiscountAdapter);

    }

    @Override
    public void onprofileload(UserModel body) {
        this.currency = body.getCurrency();
        cartSellBuyAdapter.currency = currency;
        cartSellBuyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getdata();
        }
    }

    private void getdata() {
        if (itemCartModelList != null) {
            itemCartModelList.clear();
        }
        if (preferences.getCartDatabuy(this) != null) {
            createOrderModel = preferences.getCartDatabuy(this);
            itemCartModelList.addAll(createOrderModel.getOrder_details());
            if (cartSellBuyAdapter != null) {
                cartSellBuyAdapter.notifyDataSetChanged();
            }
            calculateTotal();
        }
        if (itemCartModelList == null || itemCartModelList.size() == 0) {
            binding.llEmptyCart.setVisibility(View.VISIBLE);
            binding.consTotal.setVisibility(View.GONE);
            binding.ll.setVisibility(View.GONE);
            preferences.clearCartbuy(this);
        }
        if (cartSellBuyAdapter != null) {
            cartSellBuyAdapter.notifyDataSetChanged();
        }
    }

    //    @Override
//    protected void onRestart() {
//        super.onRestart();
//        getdata();
//    }
    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
    }
}