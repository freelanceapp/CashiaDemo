package com.cashiar.ui.activity_cart_sell;

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
import com.cashiar.databinding.ActivityCartSellBinding;
import com.cashiar.databinding.DialogInpiutBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllDiscountsModel;
import com.cashiar.models.CreateOrderModel;
import com.cashiar.models.ItemCartModel;
import com.cashiar.models.SingleDiscountModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_cart_sell__mvp.ActivityCartSellPresenter;
import com.cashiar.mvp.activity_cart_sell__mvp.CartSellActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_pay_sell.PaymentSellActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartSellActivity extends AppCompatActivity implements CartSellActivityView, Swipe.SwipeListener {
    private ActivityCartSellBinding binding;
    private ActivityCartSellPresenter presenter;
    private String lang;
    private CartSellBuyAdapter cartSellBuyAdapter;
    private List<ItemCartModel> itemCartModelList;
    private Preferences preferences;
    private float dX = 0, dY = 0;
    private float downRawX, downRawY;
    private CreateOrderModel createOrderModel;
    private double total;
    private ProgressDialog dialog;
    private SpinnerDiscountAdapter spinnerDiscountAdapter;
    private List<SingleDiscountModel> singleDiscountModels;
    private UserModel userModel;
    private int coupon_id;
    private double discount;
    private String type;
    private String currency="";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart_sell);
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
        presenter = new ActivityCartSellPresenter(this, this);
        presenter.getdiscount(userModel);
        cartSellBuyAdapter = new CartSellBuyAdapter(itemCartModelList, this,currency);
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
                    calculateTotal();

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


        preferences.create_update_cart(this, createOrderModel);
        Intent intent = new Intent(this, PaymentSellActivity.class);
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
        preferences.create_update_cart(this, createOrderModel);
        calculateTotal();
    }

    private void calculateTotal() {
        total = 0;
        for (ItemCartModel model : itemCartModelList) {

            total += model.getAmount() * model.getPrice_value();

        }
        if (coupon_id != 0) {

            if (type.equals("pre")) {
                createOrderModel.setDiscount_value(((total * discount) / 100));
                total = total - ((total * discount) / 100);
            } else {
                createOrderModel.setDiscount_value(discount);
                total = total - discount;
            }

        }

        binding.tvTotal.setText(String.format(Locale.ENGLISH, "%s %s", String.valueOf(total), getString(R.string.sar)));
    }

    public void deleteItem(int adapterPosition) {
        itemCartModelList.remove(adapterPosition);
        cartSellBuyAdapter.notifyItemRemoved(adapterPosition);
        createOrderModel.setOrder_details(itemCartModelList);
        preferences.create_update_cart(this, createOrderModel);
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
                                                         preferences.create_update_cart(CartSellActivity.this, createOrderModel);
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
        if(dialog==null){
            dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);}
        else {
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
            singleDiscountModels.add(new SingleDiscountModel("choose Discount"));
        } else {

            singleDiscountModels.add(new SingleDiscountModel("اختر الخصم"));
        }
        //Log.e("dlldldl",model.getData().size()+"");
        singleDiscountModels.addAll(model.getData());
        spinnerDiscountAdapter = new SpinnerDiscountAdapter(singleDiscountModels, this);
        binding.spcat.setAdapter(spinnerDiscountAdapter);

    }

    @Override
    public void onprofileload(UserModel body) {
        this.currency=body.getCurrency();
        cartSellBuyAdapter.currency=currency;
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
        if (preferences.getCartData(this) != null) {
            createOrderModel = preferences.getCartData(this);
            itemCartModelList .addAll(createOrderModel.getOrder_details());
            calculateTotal();
        }
        if (itemCartModelList == null || itemCartModelList.size() == 0) {
            binding.llEmptyCart.setVisibility(View.VISIBLE);
            binding.consTotal.setVisibility(View.GONE);
            binding.ll.setVisibility(View.GONE);
            preferences.clearCart(this);
        }
        if (cartSellBuyAdapter != null) {
            cartSellBuyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (preferences != null) {
            getdata();
            presenter.getprofile(userModel);
        }
    }
}