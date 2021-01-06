package com.cashiar.ui.activity_home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivityHomeBinding;
import com.cashiar.language.Language;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_home_mvp.ActivityHomePresenter;
import com.cashiar.mvp.activity_home_mvp.HomeActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.Activity_add_accout.AddAccountActivity;
import com.cashiar.ui.activity_add_cashier.AddCashierActivity;
import com.cashiar.ui.activity_all_bill_buy.AllBillBuyActivity;
import com.cashiar.ui.activity_all_bill_sell.AllBillSellActivity;
import com.cashiar.ui.activity_customers.CustomersActivity;
import com.cashiar.ui.activity_departments.DepartmentActivity;
import com.cashiar.ui.activity_expenses.ExpensesActivity;
import com.cashiar.ui.activity_login.LoginActivity;
import com.cashiar.ui.activity_products.ProductsActivity;
import com.cashiar.ui.activity_products_buy.ProductsBuyActivity;
import com.cashiar.ui.activity_products_sell.ProductsSellActivity;
import com.cashiar.ui.activity_profile.ProfileActivity;
import com.cashiar.ui.activity_suppliers.SuppliersActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements HomeActivityView {
    private ActivityHomeBinding binding;
    private ActivityHomePresenter presenter;
    private ActionBarDrawerToggle toggle;
    private Preferences preference;
    private UserModel userModel;
    private List<Integer> persearch;
    private ProgressDialog dialog;
    private Preferences preferences;
    private String current_language;
    private String[] language_array;
    private String currency;
    private UserModel body;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
    }


    private void initView() {
        //    EventBus.getDefault().register(this);
        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        language_array = new String[]{"English", "العربية"};

        persearch = new ArrayList<>();
        toggle = new ActionBarDrawerToggle(this, binding.drawar, binding.toolbar, R.string.open, R.string.close);
        toggle.syncState();
        presenter = new ActivityHomePresenter(this, this);
        preference = Preferences.getInstance();
        userModel = preference.getUserData(this);
        presenter.updateTokenFireBase(userModel);
        presenter.getprofile(userModel);
        List<UserModel.Permissions> permissions = userModel.getPermissions();
        for (int i = 0; i < permissions.size(); i++) {
            persearch.add(permissions.get(i).getId());
        }
//        binding.fraddproduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (persearch.contains(2)) {
//                    presenter.addproducts();
//                }
//                {
//                    Toast.makeText(HomeActivity.this,getResources().getString(R.string.dont_have_permission),Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
        binding.llCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(5)) {
                    presenter.customers();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llAddcashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(1)) {
                    presenter.addcashier();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llSupliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(6)) {
                    presenter.suppliers();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llBackesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(8)) {
                    if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
                        presenter.backsale();
                    } else {
                        Common.CreateDialogAlertProfile(HomeActivity.this, getResources().getString(R.string.please_complete_profile_first));

                    }
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llBackebuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(9)) {
                    if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {

                        presenter.backbuy();
                    } else {
                        Common.CreateDialogAlertProfile(HomeActivity.this, getResources().getString(R.string.please_complete_profile_first));

                    }
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (persearch!=null&&persearch.contains(3)) {
                    if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {

                        presenter.addbillsell();
                    } else {
                        Common.CreateDialogAlertProfile(HomeActivity.this, getResources().getString(R.string.please_complete_profile_first));

                    }

                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(4)) {
                    if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {

                        presenter.addbillBuy();
                    } else {
                        Common.CreateDialogAlertProfile(HomeActivity.this, getResources().getString(R.string.please_complete_profile_first));

                    }
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(10)) {
                    presenter.Expenses();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.fraddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(2)) {
                    presenter.addproducts();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }

            }
        });
        binding.fraCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(5)) {
                    presenter.customers();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.fraSuppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(6)) {
                    presenter.suppliers();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.fraddbillsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (persearch!=null&&persearch.contains(3)) {
                    if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {

                        presenter.addbillsell();
                    } else {
                        Common.CreateDialogAlertProfile(HomeActivity.this, getResources().getString(R.string.please_complete_profile_first));

                    }
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.fraddbillBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(4)) {
                    if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {

                        presenter.addbillBuy();
                    } else {

                        Common.CreateDialogAlertProfile(HomeActivity.this, getResources().getString(R.string.please_complete_profile_first));

                    }
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.profile();

            }
        });
        binding.frmony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (persearch!=null&&persearch.contains(10)) {
                    presenter.Expenses();
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.dont_have_permission), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.share();
            }
        });
        binding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.logout(userModel);
            }
        });
        binding.llLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.lang();
            }
        });
    }


    @Override
    public void onBackPressed() {
        presenter.backPress();
    }


    @Override
    public void onNavigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onAddproducts() {
        Intent intent = new Intent(this, DepartmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCustomers() {
        Intent intent = new Intent(this, CustomersActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuppliers() {
        Intent intent = new Intent(this, SuppliersActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAddbillSell() {
        Intent intent = new Intent(this, ProductsSellActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAddbillBuy() {
        Intent intent = new Intent(this, ProductsBuyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onExpenses() {
        Intent intent = new Intent(this, ExpensesActivity.class);
        startActivity(intent);
    }

    @Override
    public void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(intent);
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
    public void onprofileload(UserModel body) {
        this.currency = body.getCurrency();
        this.body = body;

    }

    @Override
    public void logout() {
        preference.clear(this);
        navigateToSignInActivity();
    }


    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBacksale() {
        Intent intent = new Intent(this, AllBillSellActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackbuy() {
        Intent intent = new Intent(this, AllBillBuyActivity.class);
        startActivity(intent);
    }

    @Override
    public void oncahier() {
        Intent intent = new Intent(this, AddCashierActivity.class);
        startActivity(intent);
    }

    @Override
    public void profile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void lang() {
        CreateLanguageDialog();
    }

    private void navigateToSignInActivity() {

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public void RefreshActivity(String selected_language) {
        //preferences.create_update_language(this, selected_language);
        Paper.book().write("lang", selected_language);
        Language.updateResources(this, selected_language);

        Intent intent = getIntent();
        finish();

        startActivity(intent);


    }

    private void CreateLanguageDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_language, null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(language_array.length - 1);
        numberPicker.setDisplayedValues(language_array);
        numberPicker.setWrapSelectorWheel(false);
        if (current_language.equals("ar")) {
            numberPicker.setValue(2);

        } else if (current_language.equals("en")) {
            numberPicker.setValue(0);

        } else {
            numberPicker.setValue(1);
        }
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int pos = numberPicker.getValue();
                if (pos == 0) {
                    RefreshActivity("en");
                } else if (pos == 1) {
                    RefreshActivity("ar");

                }


            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //  dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
    }
}