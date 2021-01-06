package com.cashiar.ui.activity_pay_sell;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.adapters.SpinnerCustomerAdapter;
import com.cashiar.databinding.ActivityPaymentSellBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllCustomersModel;
import com.cashiar.models.BillModel;
import com.cashiar.models.CreateOrderModel;
import com.cashiar.models.PaymentModel;
import com.cashiar.models.SingleCustomerSuplliersModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_payment_sell_mvp.ActivityPAymentSellPresenter;
import com.cashiar.mvp.activity_payment_sell_mvp.PaymentSellActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_add_Customer.AddCustomerActivity;
import com.cashiar.ui.activity_bill_Sell.BillSellActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class PaymentSellActivity extends AppCompatActivity implements PaymentSellActivityView {
    private ActivityPaymentSellBinding binding;
    private ActivityPAymentSellPresenter presenter;
    private String lang;
    private PaymentModel paymentModel;
    private double total;
    private Preferences preferences;
    private UserModel userModel;
    private List<SingleCustomerSuplliersModel> singleCustomerSuplliersModels;
    private ProgressDialog dialog;
    private SpinnerCustomerAdapter spinnercustomerAdapter;
    private int client_id;
    private CreateOrderModel createOrderModel;
    private double paid;
    private String taxamount;
    private String name;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_sell);
        getdatafromintent();
        initView();
    }

    private void getdatafromintent() {
        Intent intent = getIntent();
        if (intent != null) {
            total = intent.getDoubleExtra("total", 0.0);
        }
    }


    private void initView() {
        singleCustomerSuplliersModels = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        createOrderModel = preferences.getCartData(this);
        binding.setDate(System.currentTimeMillis());
        binding.tvTotal.setText((total+createOrderModel.getDiscount_value()) + "");
        binding.tvstay.setText((total) + "");
binding.tvdiscount.setText(createOrderModel.getDiscount_value()+"");
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityPAymentSellPresenter(this, this);
        presenter.getcustomer(userModel);
        presenter.getprofile(userModel);
        paymentModel = new PaymentModel();

        binding.setModel(paymentModel);
        binding.edtpaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtpaid.setError(null);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtpaid.setError(null);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    paid = Double.parseDouble(editable.toString());

                    if(paid<=total){
                    binding.tvstay.setText((total - paid) + "");}
                    else {
                        binding.edtpaid.setError(getResources().getString(R.string.paid_must_small_or));
                    }
                } catch (Exception e) {
                    binding.tvstay.setText((total) + "");
                }
            }
        });
        binding.btnaddcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addCustomers();
            }
        });
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrderModel.setClient_id(client_id);
                createOrderModel.setName(name);
                String date[];
                date = binding.tvdate.getText().toString().split("/");

                createOrderModel.setDate(date[2] + "-" + date[1] + "-" + date[0]);
                createOrderModel.setTotal_price(Math.round(total));
                createOrderModel.setPaid_price(Math.round(paid));
                createOrderModel.setRemaining_price(Math.round(total - paid));
                createOrderModel.setDiscount_value(Math.round(createOrderModel.getDiscount_value()));

                //  Log.e("llll",createOrderModel.getDiscount_value()+" "+createOrderModel.getRemaining_price()+" "+total);

                presenter.checkData(paymentModel, createOrderModel, userModel);
            }
        });
        binding.spcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    client_id =0;
                    paymentModel.setId("");

                    binding.setModel(paymentModel);
                } else {
                    client_id = singleCustomerSuplliersModels.get(i).getId();
                    name=singleCustomerSuplliersModels.get(i).getName();
                    paymentModel.setId(client_id + "");

                    binding.setModel(paymentModel);
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
    public void ondcustomerSuccess(AllCustomersModel model) {
        singleCustomerSuplliersModels.clear();
        if (lang.equals("en")) {

            singleCustomerSuplliersModels.add(new SingleCustomerSuplliersModel("Choose Customer"));
        } else {

            singleCustomerSuplliersModels.add(new SingleCustomerSuplliersModel("اختر عميل"));
        }
        //Log.e("dlldldl",model.getData().size()+"");
        singleCustomerSuplliersModels.addAll(model.getData());
        spinnercustomerAdapter = new SpinnerCustomerAdapter(singleCustomerSuplliersModels, this);
        binding.spcat.setAdapter(spinnercustomerAdapter);

    }

    @Override
    public void onCustomers() {
        Intent intent = new Intent(this, AddCustomerActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onsucess(BillModel body) {
        preferences.clearCart(this);
        Intent intent = new Intent(this, BillSellActivity.class);
        intent.putExtra("data", createOrderModel);
        intent.putExtra("databill",body);
        startActivity(intent);
        finish();

    }

    @Override
    public void onprofileload(UserModel body) {
        this.taxamount = body.getTax_amount();
        binding.setTaxamount(taxamount);
        binding.tvtax.setText(taxamount);
        total = total + (Double.parseDouble(taxamount));
        createOrderModel.setTotal_price(total);
        binding.tvstay.setText((total-paid) + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            presenter.getcustomer(userModel);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
    }
}