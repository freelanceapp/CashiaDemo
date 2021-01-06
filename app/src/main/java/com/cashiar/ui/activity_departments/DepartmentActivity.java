package com.cashiar.ui.activity_departments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cashiar.R;
import com.cashiar.databinding.ActivityDepartmentsBinding;
import com.cashiar.language.Language;
import com.cashiar.mvp.activity_department_mvp.ActivityDepartmentPresenter;
import com.cashiar.mvp.activity_department_mvp.DepartmentActivityView;
import com.cashiar.ui.activity_categories.CategoriesActivity;
import com.cashiar.ui.activity_disacount.DiscountActivity;
import com.cashiar.ui.activity_products.ProductsActivity;

import io.paperdb.Paper;

public class DepartmentActivity extends AppCompatActivity implements DepartmentActivityView {
    private ActivityDepartmentsBinding binding;
    private ActivityDepartmentPresenter presenter;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_departments);
        initView();
    }



    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityDepartmentPresenter(this, this);
        binding.consaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addproducts();
            }
        });
        binding.consadddepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.adddepartments();
            }
        });
        binding.consdiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.adddiscounts();
            }
        });

        binding.llBack.setOnClickListener(view -> {
            finish();
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
    public void onAddproducts() {
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAdddepartments() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAdddiscounts() {
        Intent intent = new Intent(this, DiscountActivity.class);
        startActivity(intent);
    }

}