package com.cashiar.ui.activity_products_sell;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashiar.R;
import com.cashiar.adapters.CategoriesSaleBuydapter;
import com.cashiar.adapters.ProductsAdapter;
import com.cashiar.databinding.ActivityProductsSellBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.AllProductsModel;
import com.cashiar.models.CreateOrderModel;
import com.cashiar.models.ItemCartModel;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.models.SingleProductModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_products_sell_mvp.ActivityProductsSellPresenter;
import com.cashiar.mvp.activity_products_sell_mvp.ProductsSellActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_cart_sell.CartSellActivity;
import com.cashiar.ui.scanner.ActivityScanner;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ProductsSellActivity extends AppCompatActivity implements ProductsSellActivityView {
    private ActivityProductsSellBinding binding;
    private ActivityProductsSellPresenter presenter;
    private String lang;
    private ProductsAdapter productsAdapter;
    private CategoriesSaleBuydapter categoriesSaleBuydapter;
    private List<SingleCategoryModel> singleCategoryModelList;
    private List<SingleProductModel> singleProductModelList;
    private Preferences preferences;
    private UserModel userModel;
    private String cat = "all", query;
    private CreateOrderModel createOrderModel;
    private List<ItemCartModel> itemCartModels;
    private String currency="";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products_sell);
        initView();
    }

    private void initView() {

        singleProductModelList = new ArrayList<>();
        singleCategoryModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        if (preferences.getCartData(this) != null) {
            binding.setCartcount(preferences.getCartData(this).getOrder_details().size());
        }
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityProductsSellPresenter(this, this);
        productsAdapter = new ProductsAdapter(this, singleProductModelList, currency);
        presenter.getprofile(userModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(productsAdapter);
        categoriesSaleBuydapter = new CategoriesSaleBuydapter(this, singleCategoryModelList);
        binding.recViewcategories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewcategories.setAdapter(categoriesSaleBuydapter);
        binding.frbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsSellActivity.this, ActivityScanner.class);
                startActivityForResult(intent, 100);
            }
        });
        binding.frcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.Cart();
            }
        });
        presenter.getcategories(userModel);
        presenter.getproducts(userModel, cat, query);
        binding.editQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = binding.editQuery.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(ProductsSellActivity.this, binding.editQuery);
                    presenter.getproducts(userModel, cat, query);
                    return false;
                }
            }
            return false;
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
    public void onCart() {
        Intent intent = new Intent(this, CartSellActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            presenter.getsingleproduct(userModel, data.getStringExtra("code"));

        }

    }

    @Override
    public void onLoad() {
        binding.progBarcategories.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishload() {
        binding.progBarcategories.setVisibility(View.GONE);

    }

    @Override
    public void onprofileload(UserModel body) {
        this.currency=body.getCurrency();
        productsAdapter.currency=currency;
        productsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProgressShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccess(AllCategoryModel model) {

        //Log.e("dlldldl",model.getData().size()+"");
        singleCategoryModelList.clear();
        singleCategoryModelList.addAll(model.getData());
        categoriesSaleBuydapter.notifyDataSetChanged();

    }

    @Override
    public void onproductSuccess(AllProductsModel allProductsModel) {
        if (allProductsModel.getData() == null || allProductsModel.getData().size() == 0) {
            binding.llNoNotification.setVisibility(View.VISIBLE);
        } else {
            binding.llNoNotification.setVisibility(View.GONE);

        }
        singleProductModelList.clear();
        singleProductModelList.addAll(allProductsModel.getData());


        productsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onproductSuccess(SingleProductModel allProductsModel) {
        addproductstocart(allProductsModel);
    }

    public void getproducts(int id) {
        cat = id + "";
        presenter.getproducts(userModel, cat, query);
    }

    public void addproductstocart(SingleProductModel singleProductModel) {
        if (preferences.getCartData(this) != null) {
            int index = -1;
            createOrderModel = preferences.getCartData(this);
            itemCartModels = preferences.getCartData(this).getOrder_details();
            for (int i = 0; i < itemCartModels.size(); i++) {
                if (singleProductModel.getId() == itemCartModels.get(i).getProduct_id()) {
                    index = i;
                    break;
                }}
                if (index != -1) {
                    ItemCartModel itemCartModel = itemCartModels.get(index);
                    itemCartModel.setAmount((1 * itemCartModel.getAmount2()) + itemCartModel.getAmount());
                    itemCartModels.set(index, itemCartModel);
                    createOrderModel.setOrder_details(itemCartModels);
                } else {
                    ItemCartModel itemCartModel = new ItemCartModel();
                    itemCartModel.setAmount(1);
                    itemCartModel.setAmount2(1);
                    itemCartModel.setImage(singleProductModel.getImage());
                    itemCartModel.setPrice_value(singleProductModel.getProduct_price());
                    itemCartModel.setProduct_id(singleProductModel.getId());
                    itemCartModel.setTitle(singleProductModel.getTitle());
                    itemCartModel.setType(singleProductModel.getProduct_type());
                    itemCartModel.setStock(singleProductModel.getStock_amount());

                    itemCartModels.add(itemCartModel);
                    createOrderModel.setOrder_details(itemCartModels);
                }

        } else {
            itemCartModels = new ArrayList<>();
            createOrderModel = new CreateOrderModel();
            ItemCartModel itemCartModel = new ItemCartModel();
            itemCartModel.setAmount(1);
            itemCartModel.setAmount2(1);
            itemCartModel.setImage(singleProductModel.getImage());
            itemCartModel.setPrice_value(singleProductModel.getProduct_price());
            itemCartModel.setProduct_id(singleProductModel.getId());
            itemCartModel.setTitle(singleProductModel.getTitle());
            itemCartModel.setType(singleProductModel.getProduct_type());
            itemCartModel.setStock(singleProductModel.getStock_amount());

            itemCartModels.add(itemCartModel);
            createOrderModel.setOrder_details(itemCartModels);
        }
        preferences.create_update_cart(this, createOrderModel);
        binding.setCartcount(createOrderModel.getOrder_details().size());
        Toast.makeText(this,getResources().getString(R.string.addtocartsucess),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        presenter.getprofile(userModel);
        if (preferences != null && preferences.getCartData(this) == null) {
            binding.setCartcount(0);
        }
        else {
            binding.setCartcount(preferences.getCartData(this).getOrder_details().size());
        }
    }

}