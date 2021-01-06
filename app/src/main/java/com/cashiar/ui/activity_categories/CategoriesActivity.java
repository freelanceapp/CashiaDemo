package com.cashiar.ui.activity_categories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.CategoriesAdapter;
import com.cashiar.adapters.DelteDepartmentSwipe;
import com.cashiar.adapters.DelteproductSwipe;
import com.cashiar.adapters.SliderAdapter;
import com.cashiar.databinding.ActivityCategoriesBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.models.Slider_Model;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_categories_mvp.ActivityCategoriesPresenter;
import com.cashiar.mvp.activity_categories_mvp.CategoriesActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_add_departmnet.AddDepartmnetActivity;
import com.cashiar.ui.activity_add_product.AddProductActivity;
import com.cashiar.ui.activity_products.ProductsActivity;
import com.cashiar.ui.activity_suppliers.SuppliersActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class CategoriesActivity extends AppCompatActivity implements CategoriesActivityView, DelteDepartmentSwipe.SwipeListener {
    private ActivityCategoriesBinding binding;
    private ActivityCategoriesPresenter presenter;
    private String lang;
    private float dX = 0, dY = 0;
    private float downRawX, downRawY;
    private List<SingleCategoryModel> singleCategoryModelList;
    private CategoriesAdapter categoriesAdapter;
    private Preferences preferences;
    private UserModel userModel;
    private String query;
    private int pos;
    private ProgressDialog dialog;
    private SliderAdapter sliderAdapter;
    private List<Slider_Model.Data> sliDataList;
    private Timer timer;
    private TimerTask timerTask;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_categories);
        initView();
    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        singleCategoryModelList = new ArrayList<>();
        sliDataList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityCategoriesPresenter(this, this);
        sliderAdapter = new SliderAdapter(sliDataList, this);

        categoriesAdapter = new CategoriesAdapter(this, singleCategoryModelList);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(categoriesAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new DelteDepartmentSwipe(this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(binding.recView);
        binding.llMap.performClick();
        presenter.getcategories(userModel, query);
        binding.editQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = binding.editQuery.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(CategoriesActivity.this, binding.editQuery);
                    presenter.getcategories(userModel, query);
                    return false;
                }
            }
            return false;
        });
        binding.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.adddepartment();

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
//                        presenter.adddepartment();
//                    }
//                    return false; // Consumed
//
//
//                } else {
//                    //return super.onTouchEvent(motionEvent);
//
//                    if (duration < 100) {
//                        presenter.adddepartment();
//                    }
//                }
//
//                return false;
//            }
//
//
//        });
        presenter.getSlider();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
    public void onAddDepartment() {
        Intent intent = new Intent(this, AddDepartmnetActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onSuccess(AllCategoryModel model) {

        //Log.e("dlldldl",model.getData().size()+"");
        if (model.getData() == null || model.getData().size() == 0) {
            binding.llNoNotification.setVisibility(View.VISIBLE);
        } else {
            binding.llNoNotification.setVisibility(View.GONE);

        }
        singleCategoryModelList.clear();
        singleCategoryModelList.addAll(model.getData());
        categoriesAdapter.notifyDataSetChanged();

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
    public void onProgressShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            singleCategoryModelList.clear();
            categoriesAdapter.notifyDataSetChanged();
            query = "";
            presenter.getcategories(userModel, query);
        }
    }

    @Override
    public void onSwipe(int pos, int dir) {
        this.pos = pos;
        presenter.deltedepartment(singleCategoryModelList.get(pos).getId(), userModel);
    }

    @Override
    public void onSuccessDelete() {
        singleCategoryModelList.remove(pos);
        categoriesAdapter.notifyItemRemoved(pos);
    }

    public void update(SingleCategoryModel singleCategoryModel) {
        Intent intent = new Intent(CategoriesActivity.this, AddDepartmnetActivity.class);
        intent.putExtra("data", singleCategoryModel);
        intent.putExtra("type", "update");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onProgressSliderShow() {
        binding.progBarSlider.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressSliderHide() {
        binding.progBarSlider.setVisibility(View.GONE);
    }

    @Override
    public void onSliderSuccess(List<Slider_Model.Data> sliderModelList) {

        for (int i = 0; i < sliderModelList.size(); i++) {
            if (sliderModelList.get(i).getType().equals("categories")) {
                sliDataList.add(sliderModelList.get(i));

            }
        }
        binding.tab.setupWithViewPager(binding.sliderview);
        binding.sliderview.setAdapter(sliderAdapter);
        if (sliDataList.size()>1)
        {
            timer = new Timer();
            timerTask = new MyTask();
            timer.scheduleAtFixedRate(timerTask,6000,6000);
        }
        if(sliDataList.size()==0){
            binding.fr.setVisibility(View.GONE);
        }
        sliderAdapter.notifyDataSetChanged();
    }
    public class MyTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                int current_page = binding.sliderview.getCurrentItem();
                if (current_page<sliderAdapter.getCount()-1){
                    binding.sliderview.setCurrentItem(binding.sliderview.getCurrentItem()+1);
                }else {
                    binding.sliderview.setCurrentItem(0);

                }
            });

        }
    }

}