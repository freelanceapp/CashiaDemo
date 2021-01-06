package com.cashiar.ui.activity_add_cashier;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.PermissionAdapter;
import com.cashiar.databinding.ActivityAddCashierBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AddCashierModel;
import com.cashiar.models.AllPermissionModel;
import com.cashiar.models.SinglePermissionModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_add_cashier_mvp.ActivityAddCashierPresenter;
import com.cashiar.mvp.activity_add_cashier_mvp.AddCashierActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.ui.activity_expenses.ExpensesActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AddCashierActivity extends AppCompatActivity implements AddCashierActivityView {
    private ActivityAddCashierBinding binding;


    private AddCashierModel model;
    private ActivityAddCashierPresenter presenter;
    private Preferences preferences;
    private UserModel userModel;
    private ProgressDialog dialog;
    private List<SinglePermissionModel> singlePermissionModelList;
    private PermissionAdapter permissionAdapter;
    private List<Integer> ids;
    private UserModel body;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_cashier);
        initView();

    }

    private void initView() {
        ids = new ArrayList<>();
        singlePermissionModelList = new ArrayList<>();

        permissionAdapter = new PermissionAdapter(this, singlePermissionModelList);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        model = new AddCashierModel();
        binding.setModel(model);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(permissionAdapter);
        presenter = new ActivityAddCashierPresenter(this, this);
        presenter.getprofile(userModel);
        presenter.getpermission();
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (body!=null&&body.getCurrency() != null && body.getTax_amount() != null) {
                    presenter.checkData(model, userModel);
                } else {
                    Common.CreateDialogAlertProfile(AddCashierActivity.this,getResources().getString(R.string.please_complete_profile_first));

                }
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
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
    public void onSuccess() {
        finish();
    }

    @Override
    public void onpermisionSuccess(AllPermissionModel body) {

        singlePermissionModelList.clear();
        singlePermissionModelList.addAll(body.getData());
        permissionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onprofileload(UserModel body) {
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

    public void addid(int id) {
        Log.e("id", id + "");
        ids.add(id);
        model.setIds(ids);
        binding.setModel(model);
    }

    public void removeid(int id) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == id) {
                ids.remove(i);
                break;
            }
        }
        model.setIds(ids);
        binding.setModel(model);
    }
}