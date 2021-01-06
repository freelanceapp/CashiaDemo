package com.cashiar.ui.activity_expenses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.DelteExpensesSwipe;
import com.cashiar.adapters.DelteproductSwipe;
import com.cashiar.adapters.EXpensesAdapter;
import com.cashiar.adapters.SliderAdapter;
import com.cashiar.databinding.ActivityExpensesBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllExpensesModel;
import com.cashiar.models.SingleDiscountModel;
import com.cashiar.models.SingleExpensesModel;
import com.cashiar.models.Slider_Model;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_expenses_mvp.ActivityExpensesPresenter;
import com.cashiar.mvp.activity_expenses_mvp.ExpensesActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_add_discount.AddDiscountActivity;
import com.cashiar.ui.activity_add_expenses.AddExpensesActivity;
import com.cashiar.ui.activity_customers.CustomersActivity;
import com.cashiar.ui.activity_disacount.DiscountActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class ExpensesActivity extends AppCompatActivity implements ExpensesActivityView, DelteExpensesSwipe.SwipeListener {
    private ActivityExpensesBinding binding;
    private ActivityExpensesPresenter presenter;
    private String lang;
    private float dX = 0, dY = 0;
    private float downRawX, downRawY;
    private EXpensesAdapter expenseadapter;
    private List<SingleExpensesModel> singleExpensesModelList;
    private Preferences preferences;
    private UserModel userModel;
    private SliderAdapter sliderAdapter;
    private List<Slider_Model.Data> sliDataList;
    private Timer timer;
    private TimerTask timerTask;
    private ProgressDialog dialog2;
    private UserModel body;
    private String currency = "";
    private int pos;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expenses);
        initView();
    }


    private void initView() {
        sliDataList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        singleExpensesModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        presenter = new ActivityExpensesPresenter(this, this);
        expenseadapter = new EXpensesAdapter(this, singleExpensesModelList, currency);
        presenter.getprofile(userModel);
        sliderAdapter = new SliderAdapter(sliDataList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(expenseadapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new DelteExpensesSwipe(this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(binding.recView);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.llMap.performClick();
        presenter.getexpenses(userModel);
        binding.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (body != null && body.getCurrency() != null && body.getTax_amount() != null) {

                    presenter.addExpenses();
                } else {
                    Common.CreateDialogAlertProfile(ExpensesActivity.this, getResources().getString(R.string.please_complete_profile_first));

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
//                        if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
//                            presenter.addExpenses();
//                        } else {
//                            Common.CreateDialogAlertProfile(ExpensesActivity.this,getResources().getString(R.string.please_complete_profile_first));
//
//                        }
//                    }
//                    return false; // Consumed
//
//
//                } else {
//                    //return super.onTouchEvent(motionEvent);
//
//                    if (duration < 100) {
//                        if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
//                            presenter.addExpenses();
//                        }  else {
//                            Common.CreateDialogAlertProfile(ExpensesActivity.this,getResources().getString(R.string.please_complete_profile_first));
//                        }
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
        this.currency = body.getCurrency();
        this.body = body;
        expenseadapter.currency = currency;
        expenseadapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccessDelete() {
        singleExpensesModelList.remove(pos);
        expenseadapter.notifyItemRemoved(pos);
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
    public void onExpenses() {
        Intent intent = new Intent(this, AddExpensesActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onexpensesSuccess(AllExpensesModel allExpensesModel) {
        if (allExpensesModel.getData() == null || allExpensesModel.getData().size() == 0) {
            binding.llNoNotification.setVisibility(View.VISIBLE);
        } else {
            binding.llNoNotification.setVisibility(View.GONE);

        }
        singleExpensesModelList.clear();
        singleExpensesModelList.addAll(allExpensesModel.getData());
        expenseadapter.notifyDataSetChanged();
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
            presenter.getexpenses(userModel);
        }
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
            if (sliderModelList.get(i).getType().equals("expenses")) {
                sliDataList.add(sliderModelList.get(i));

            }
        }
        binding.tab.setupWithViewPager(binding.sliderview);
        binding.sliderview.setAdapter(sliderAdapter);
        if (sliDataList.size() > 1) {
            timer = new Timer();
            timerTask = new MyTask();
            timer.scheduleAtFixedRate(timerTask, 6000, 6000);
        }
        if (sliDataList.size() == 0) {
            binding.fr.setVisibility(View.GONE);
        }
        sliderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSwipe(int pos, int dir) {
        this.pos = pos;
        presenter.deleteexpense(singleExpensesModelList.get(pos).getId(), userModel);

    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                int current_page = binding.sliderview.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.sliderview.setCurrentItem(binding.sliderview.getCurrentItem() + 1);
                } else {
                    binding.sliderview.setCurrentItem(0);

                }
            });

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
        presenter.getexpenses(userModel);
    }
    public void update(SingleExpensesModel singleExpensesModel) {
        Intent intent = new Intent(ExpensesActivity.this, AddExpensesActivity.class);
        intent.putExtra("data", singleExpensesModel);
        intent.putExtra("type", "update");
        startActivity(intent);
    }
}