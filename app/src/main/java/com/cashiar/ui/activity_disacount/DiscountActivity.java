package com.cashiar.ui.activity_disacount;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.cashiar.adapters.DelteDepartmentSwipe;
import com.cashiar.adapters.DelteDiscountSwipe;
import com.cashiar.adapters.DiscountsAdapter;
import com.cashiar.databinding.ActivityDiscountsBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllDiscountsModel;
import com.cashiar.models.SingleDiscountModel;
import com.cashiar.models.SingleProductModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_discount_mvp.ActivityDiscountPresenter;
import com.cashiar.mvp.activity_discount_mvp.DiscountActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_add_discount.AddDiscountActivity;
import com.cashiar.ui.activity_add_product.AddProductActivity;
import com.cashiar.ui.activity_products.ProductsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class DiscountActivity extends AppCompatActivity implements DiscountActivityView , DelteDiscountSwipe.SwipeListener {
    private ActivityDiscountsBinding binding;
    private ActivityDiscountPresenter presenter;
    private String lang;
    private DiscountsAdapter discountsAdapter;
    private List<SingleDiscountModel> singleDiscountModelList;
    private Preferences preferences;
    private UserModel userModel;
    private float dX = 0, dY = 0;
    private float downRawX, downRawY;
    private ProgressDialog dialog2;
    private String currency = "";
    private UserModel body;
    private int pos=-1;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discounts);
        initView();
    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        singleDiscountModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityDiscountPresenter(this, this);
        discountsAdapter = new DiscountsAdapter(this, singleDiscountModelList, currency);
        presenter.getprofile(userModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(discountsAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new DelteDiscountSwipe(this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        ItemTouchHelper helper = new ItemTouchHelper(simpleCallback);
        helper.attachToRecyclerView(binding.recView);
        binding.llMap.performClick();
        presenter.getdiscount(userModel);

///        binding.llMap.setOnTouchListener(new View.OnTouchListener() {
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
//                            presenter.addDiscount();
//                        } else {
//Common.CreateDialogAlertProfile(DiscountActivity.this,getResources().getString(R.string.please_complete_profile_first));
//                        }
//                    }
//                    return false; // Consumed
//
//
//                } else {
//                    if (duration < 100) {
//                        if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
//
//                            presenter.addDiscount();
//                        } else {
//                            Common.CreateDialogAlertProfile(DiscountActivity.this,getResources().getString(R.string.please_complete_profile_first));
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
binding.llMap.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
                presenter.addDiscount();
            } else {
                Common.CreateDialogAlertProfile(DiscountActivity.this,getResources().getString(R.string.please_complete_profile_first));
            }
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
    public void onAddDiscount() {
        Intent intent = new Intent(this, AddDiscountActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void ondiscountSuccess(AllDiscountsModel allDiscountsModel) {
        singleDiscountModelList.clear();
        discountsAdapter.notifyDataSetChanged();
        Log.e("d.dldld",singleDiscountModelList.size()+"");
        if (allDiscountsModel.getData() == null || allDiscountsModel.getData().size() == 0) {
            binding.llNoNotification.setVisibility(View.VISIBLE);
        } else {
            binding.llNoNotification.setVisibility(View.GONE);

        }
        singleDiscountModelList.clear();
        singleDiscountModelList.addAll(allDiscountsModel.getData());
        discountsAdapter.notifyDataSetChanged();
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
        discountsAdapter.currency=currency;
        discountsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccessDelete() {
        singleDiscountModelList.remove(pos);
        discountsAdapter.notifyItemRemoved(pos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            presenter.getdiscount(userModel);

        }
        else {
            presenter.getprofile(userModel);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
        presenter.getdiscount(userModel);
    }

    @Override
    public void onSwipe(int pos, int dir) {
        this.pos=pos;
        presenter.deletedicount(singleDiscountModelList.get(pos).getId(), userModel);

    }
    public void update(SingleDiscountModel singleDiscountModel) {
        Intent intent = new Intent(DiscountActivity.this, AddDiscountActivity.class);
        intent.putExtra("data", singleDiscountModel);
        intent.putExtra("type", "update");
        startActivity(intent);
    }
}