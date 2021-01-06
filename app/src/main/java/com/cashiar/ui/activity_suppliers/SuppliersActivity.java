package com.cashiar.ui.activity_suppliers;

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
import com.cashiar.adapters.CustomerAdapter;
import com.cashiar.adapters.DelteCustomerSwipe;
import com.cashiar.adapters.SliderAdapter;
import com.cashiar.databinding.ActivitySuppliersBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllCustomersModel;
import com.cashiar.models.SingleCustomerSuplliersModel;
import com.cashiar.models.SingleDiscountModel;
import com.cashiar.models.Slider_Model;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_suppliers_mvp.ActivitySuppliersPresenter;
import com.cashiar.mvp.activity_suppliers_mvp.SuppliersActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_add_Customer.AddCustomerActivity;
import com.cashiar.ui.activity_add_subliers.AddSubliersActivity;
import com.cashiar.ui.activity_customers.CustomersActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class SuppliersActivity extends AppCompatActivity implements SuppliersActivityView,DelteCustomerSwipe.SwipeListener {
    private ActivitySuppliersBinding binding;
    private ActivitySuppliersPresenter presenter;
    private String lang;
    private float dX = 0, dY = 0;
    private float downRawX, downRawY;
    private String query;
    private Preferences preferences;
    private UserModel userModel;
    private List<SingleCustomerSuplliersModel> allCustomersModels;
    private CustomerAdapter customerAdapter;
    private SliderAdapter sliderAdapter;
    private List<Slider_Model.Data> sliDataList;
    private Timer timer;
    private TimerTask timerTask;
    private UserModel body;
    private ProgressDialog dialog2;
    private int pos;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_suppliers);
        initView();
    }


    private void initView() {
        sliDataList = new ArrayList<>();

        allCustomersModels = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        presenter = new ActivitySuppliersPresenter(this, this);
        presenter.getprofile(userModel);
        customerAdapter = new CustomerAdapter(this, allCustomersModels);
        sliderAdapter = new SliderAdapter(sliDataList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(customerAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new DelteCustomerSwipe(this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(binding.recView);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.llMap.performClick();
        binding.editQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = binding.editQuery.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(SuppliersActivity.this, binding.editQuery);
                    presenter.getCustomers(userModel, query);
                    return false;
                }
            }
            return false;
        });
        binding.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.suppliers();

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
//                    presenter.suppliers();
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
//                     //   if(body.getCurrency()!=null&&body.getTax_amount()!=null){
//
//                            presenter.suppliers();
//
//                    //}
//                       //
//                    }
//                    return false; // Consumed
//
//
//                } else {
//                    //return super.onTouchEvent(motionEvent);
//
//                    if (duration < 100) {
//                       // if(body.getCurrency()!=null&&body.getTax_amount()!=null){
//
//                            presenter.suppliers();
////                    }
////                        else {
////
////                        }
//                    }
//                }
//
//                return false;
//            }
//
//
//        });
        presenter.getCustomers(userModel, query);
        presenter.getSlider();
    }


    @Override
    public void onSuppliers() {
        Intent intent = new Intent(this, AddSubliersActivity.class);
        startActivityForResult(intent, 1);
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
    public void onSuccess(AllCustomersModel model) {

        //Log.e("dlldldl",model.getData().size()+"");
        if (model.getData() == null || model.getData().size() == 0) {
            binding.llNoNotification.setVisibility(View.VISIBLE);
        } else {
            binding.llNoNotification.setVisibility(View.GONE);

        }
        allCustomersModels.clear();
        allCustomersModels.addAll(model.getData());
        customerAdapter.notifyDataSetChanged();

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
            if (sliderModelList.get(i).getType().equals("suppliers")) {
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

    @Override
    public void onLoad() {
        if (dialog2 == null) {
            dialog2 = Common.createProgressDialog(this, getString(R.string.wait));
            dialog2.setCancelable(false);
        } else {
            dialog2.dismiss();
        }
        dialog2.show();
    }

    @Override
    public void onFinishload() {
        dialog2.dismiss();
    }


    @Override
    public void onprofileload(UserModel body) {
this.body=body;
    }

    @Override
    public void onSuccessDelete() {
        allCustomersModels.remove(pos);
        customerAdapter.notifyDataSetChanged();
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
            query = "";
            presenter.getCustomers(userModel, query);
        }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
        presenter.getCustomers(userModel,query);
    }
    @Override
    public void onSwipe(int pos, int dir) {
        this.pos=pos;
        presenter.deletesupplier(allCustomersModels.get(pos).getId(), userModel);

    }
    public void update(SingleCustomerSuplliersModel singleCustomerSuplliersModel) {
        Intent intent = new Intent(SuppliersActivity.this, AddSubliersActivity.class);
        intent.putExtra("data", singleCustomerSuplliersModel);
        intent.putExtra("type", "update");
        startActivity(intent);
    }
}