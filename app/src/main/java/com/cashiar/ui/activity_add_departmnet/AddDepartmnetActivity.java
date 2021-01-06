package com.cashiar.ui.activity_add_departmnet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.ColorsAdapter;
import com.cashiar.databinding.ActivityAddDepartmentBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AddDepartmentModel;
import com.cashiar.models.AllColorsModel;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.models.SingleColorModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_add_department_mvp.ActivityAddDepartmentPresenter;
import com.cashiar.mvp.activity_add_department_mvp.AddDepartmentActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AddDepartmnetActivity extends AppCompatActivity implements AddDepartmentActivityView {
    private ActivityAddDepartmentBinding binding;
    private ActivityAddDepartmentPresenter presenter;
    private String lang;
    private AddDepartmentModel addDepartmentModel;
    private List<SingleColorModel> allColorsModels;
    private ColorsAdapter colorsAdapter;
    private UserModel userModel;
    private Preferences preferences;
    private ProgressDialog dialog;
    private SingleCategoryModel singleCategoryModel;
    private String type;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_department);
        getdatafromintent();
        initView();
    }

    private void getdatafromintent() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("type") != null) {
            Log.e("dlldll", "dldlldl");
            type = intent.getStringExtra("type");
            singleCategoryModel = (SingleCategoryModel) intent.getSerializableExtra("data");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        allColorsModels = new ArrayList<>();
        colorsAdapter = new ColorsAdapter(this, allColorsModels);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityAddDepartmentPresenter(this, this);

        addDepartmentModel = new AddDepartmentModel();

        binding.setModel(addDepartmentModel);
        if(type!=null&&type.equals("update")){
            binding.tv.setText(getResources().getString(R.string.udate_department));
            binding.btnConfirm.setText(getResources().getString(R.string.update));
        }
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type!=null&&type.equals("update")){
                    presenter.checkDataupdate(addDepartmentModel,userModel,singleCategoryModel);
                }
                else {
                presenter.checkData(addDepartmentModel, userModel);
            }}
        });
        binding.recView.setLayoutManager(new GridLayoutManager(this, 4));
        binding.recView.setAdapter(colorsAdapter);
        presenter.getcolors();
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
    public void oncolorSuccess(AllColorsModel allColorsModel) {
        allColorsModels.clear();
        allColorsModels.addAll(allColorsModel.getData());


        colorsAdapter.notifyDataSetChanged();
        if(singleCategoryModel!=null){
            update();
        }
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

    public void setcolor(int id) {
        Log.e("fkkfkfkkf", id + "");
        addDepartmentModel.setColor(id + "");
        binding.setModel(addDepartmentModel);

    }

    private void update() {
        addDepartmentModel.setName(singleCategoryModel.getTitle());
        addDepartmentModel.setColor(singleCategoryModel.getColor_id());
        binding.btnConfirm.setText(getResources().getString(R.string.update));
        setselectioncolor();
    }
    private void setselectioncolor() {
        int pos = -1;
        for (int i = 0; i < allColorsModels.size(); i++) {
            if (allColorsModels.get(i).getId() == Integer.parseInt(singleCategoryModel.getColor_id())) {
                pos = i;
                break;
            }
        }
        colorsAdapter.setI(pos);
    }

}