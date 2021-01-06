package com.cashiar.ui.activity_payment_bill_Sell;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivityPaymentBillSellBinding;
import com.cashiar.language.Language;
import com.cashiar.models.CreateOrderModel;
import com.cashiar.models.PaymentModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_payment_bill_sell_mvp.ActivityPAymentBillSellPresenter;
import com.cashiar.mvp.activity_payment_bill_sell_mvp.PaymentBillSellActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_bill_Sell.BillSellActivity;

import io.paperdb.Paper;

public class PaymentBillSellActivity extends AppCompatActivity implements PaymentBillSellActivityView {
    private ActivityPaymentBillSellBinding binding;
    private ActivityPAymentBillSellPresenter presenter;
    private String lang;
    private PaymentModel paymentModel;
    private double total;
    private Preferences preferences;
    private UserModel userModel;

    private ProgressDialog dialog;
    private CreateOrderModel createOrderModel;
    private double paid;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_bill_sell);
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
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        createOrderModel = preferences.getCartDatabillsell(this);

        binding.setDate(System.currentTimeMillis());
        binding.tvTotal.setText(total + "");
        binding.tvstay.setText((total) + "");

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityPAymentBillSellPresenter(this, this);
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
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrderModel.setClient_id(createOrderModel.getClient_id());
                paymentModel.setId(createOrderModel.getClient_id() + "");
                String date[];
                date = binding.tvdate.getText().toString().split("/");

                createOrderModel.setDate(date[2] + "-" + date[1] + "-" + date[0]);
                createOrderModel.setTotal_price(Math.round(total));
                createOrderModel.setPaid_price(Math.round(paid));
                createOrderModel.setRemaining_price(Math.round(total - paid));
                presenter.checkData(paymentModel, createOrderModel, userModel);
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
        dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
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
    public void onsucess() {
        preferences.clearCartbillsell(this);

        finish();
    }


}