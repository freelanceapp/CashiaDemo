package com.cashiar.ui.activity_products;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.DelteproductSwipe;
import com.cashiar.adapters.ProductsAdapter;
import com.cashiar.adapters.SpinnerCategoryAdapter;
import com.cashiar.adapters.Swipe;
import com.cashiar.databinding.ActivityProductsBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.AllProductsModel;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.models.SingleProductModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_products_mvp.ActivityProductsPresenter;
import com.cashiar.mvp.activity_products_mvp.ProductsActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_add_product.AddProductActivity;
import com.cashiar.ui.activity_expenses.ExpensesActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ProductsActivity extends AppCompatActivity implements ProductsActivityView, DelteproductSwipe.SwipeListener {
    private ActivityProductsBinding binding;
    private ActivityProductsPresenter presenter;
    private String lang;
    private ProductsAdapter productsAdapter;
    private SpinnerCategoryAdapter spinnerCategoryAdapter;
    private List<SingleCategoryModel> singleCategoryModelList;
    private List<SingleProductModel> singleProductModels;

    private float dX = 0, dY = 0;
    private float downRawX, downRawY;
    private UserModel userModel;
    private Preferences preferences;
    private ProgressDialog dialog;
    private String cat = "all", query = "";
    private int pos;
    private String currency = "";
    private int taderid;
    private UserModel body;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products);
        initView();
    }


    private void initView() {
        Paper.init(this);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", "ar");
        singleCategoryModelList = new ArrayList<>();
        singleProductModels = new ArrayList<>();
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });

        presenter = new ActivityProductsPresenter(this, this);
        presenter.getcategories(userModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        productsAdapter = new ProductsAdapter(this, singleProductModels, currency);
        presenter.getprofile(userModel);

        binding.recView.setAdapter(productsAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new DelteproductSwipe(this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(binding.recView);
        binding.llMap.performClick();
        // presenter.getproducts(userModel, cat, query);
        binding.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
                    presenter.addproducts();
                } else {
                    Common.CreateDialogAlertProfile(ProductsActivity.this, getResources().getString(R.string.please_complete_profile_first));

                }
            }
        });
//        binding.llMap.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                long duration = motionEvent.getEventTime() - motionEvent.getDownTime();
//
//
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//
//                int action = motionEvent.getAction();
//
//                if (action == MotionEvent.ACTION_DOWN) {
//
//                    downRawX = motionEvent.getRawX();
//                    downRawY = motionEvent.getRawY();
//                    dX = view.getX() - downRawX;
//                    dY = view.getY() - downRawY;
//
//                    return true; // Consumed
//
//                } else if (action == MotionEvent.ACTION_MOVE) {
//
//                    int viewWidth = view.getWidth();
//                    int viewHeight = view.getHeight();
//
//                    View viewParent = (View) view.getParent();
//                    int parentWidth = viewParent.getWidth();
//                    int parentHeight = viewParent.getHeight();
//
//                    float newX = motionEvent.getRawX() + dX;
//                    newX = Math.max(layoutParams.leftMargin, newX);
//                    newX = Math.min(parentWidth - viewWidth - layoutParams.rightMargin, newX); // Don't allow the FAB past the right hand side of the parent
//
//                    float newY = motionEvent.getRawY() + dY;
//                    newY = Math.max(layoutParams.topMargin, newY); // Don't allow the FAB past the top of the parent
//                    newY = Math.min(parentHeight - viewHeight - layoutParams.bottomMargin, newY); // Don't allow the FAB past the bottom of the parent
//
//                    view.animate()
//                            .x(newX)
//                            .y(newY)
//                            .setDuration(0)
//                            .start();
//
//                    return true; // Consumed
//
//                } else if (action == MotionEvent.ACTION_UP) {
//
//                    float upRawX = motionEvent.getRawX();
//                    float upRawY = motionEvent.getRawY();
//
//                    float upDX = upRawX - downRawX;
//                    float upDY = upRawY - downRawY;
//
//                    // A drag
//
//                    if (duration < 100) {
//
//                        if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
//                            presenter.addproducts();
//                        } else {
//                            Common.CreateDialogAlertProfile(ProductsActivity.this, getResources().getString(R.string.please_complete_profile_first));
//
//                        }
//                    }
//                    return false; // Consumed
//
//
//                } else {
//                    if (duration < 100) {
//                        if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
//                            presenter.addproducts();
//                        } else {
//                            Common.CreateDialogAlertProfile(ProductsActivity.this, getResources().getString(R.string.please_complete_profile_first));
//
//                        }
//                    }
//                    //return super.onTouchEvent(motionEvent);
//
//                }
//
//                return false;
//            }
//
//
//        });
        binding.spcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    cat = "all";
                } else {
                    cat = singleCategoryModelList.get(i).getId() + "";

                }
                presenter.getproducts(userModel, cat, query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.editQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = binding.editQuery.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(ProductsActivity.this, binding.editQuery);
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
    public void onAddproducts() {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivityForResult(intent, 1);
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
    public void onSuccessDelete() {
        singleProductModels.remove(pos);
        productsAdapter.notifyItemRemoved(pos);
    }

    @Override
    public void onprofileload(UserModel body) {
        this.currency = body.getCurrency();
        productsAdapter.currency=currency;
        productsAdapter.notifyDataSetChanged();
        this.taderid = body.getTrader_id();
        this.body = body;
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
        if (lang.equals("en")) {
            singleCategoryModelList.add(new SingleCategoryModel("choose"));
        } else {

            singleCategoryModelList.add(new SingleCategoryModel("اختر"));
        }
        //Log.e("dlldldl",model.getData().size()+"");
        singleCategoryModelList.addAll(model.getData());
        spinnerCategoryAdapter = new SpinnerCategoryAdapter(singleCategoryModelList, this);
        binding.spcat.setAdapter(spinnerCategoryAdapter);

    }

    @Override
    public void onproductSuccess(AllProductsModel allProductsModel) {
        if (allProductsModel.getData() == null || allProductsModel.getData().size() == 0) {
            binding.llNoNotification.setVisibility(View.VISIBLE);
        } else {
            binding.llNoNotification.setVisibility(View.GONE);

        }
        singleProductModels.clear();
        singleProductModels.addAll(allProductsModel.getData());


        productsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            cat = "all";
            query = "";
            presenter.getproducts(userModel, cat, query);
        }
    }

    @Override
    public void onSwipe(int pos, int dir) {
        this.pos = pos;
        presenter.delteproduct(singleProductModels.get(pos).getId(), userModel);
    }

    public void update(SingleProductModel singleProductModel) {
        Intent intent = new Intent(ProductsActivity.this, AddProductActivity.class);
        intent.putExtra("data", singleProductModel);
        intent.putExtra("type", "update");
        startActivity(intent);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
    }
}