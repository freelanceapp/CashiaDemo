package com.cashiar.ui.activity_add_product;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.ColorsAdapter;
import com.cashiar.adapters.SpinnerCategoryAdapter;
import com.cashiar.databinding.ActivityAddProductBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AddPRoductModel;
import com.cashiar.models.AllCategoryModel;
import com.cashiar.models.AllColorsModel;
import com.cashiar.models.SingleCategoryModel;
import com.cashiar.models.SingleColorModel;
import com.cashiar.models.SingleProductModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_add_product_mvp.ActivityAddProductPresenter;
import com.cashiar.mvp.activity_add_product_mvp.AddproductActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.tags.Tags;
import com.cashiar.ui.scanner.ActivityScanner;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class AddProductActivity extends AppCompatActivity implements AddproductActivityView {
    private ActivityAddProductBinding binding;
    private ActivityAddProductPresenter presenter;
    private String lang;
    private AddPRoductModel addPRoductModel;
    private AlertDialog dialog;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri = null;
    private UserModel userModel;
    private Preferences preferences;
    private List<SingleCategoryModel> singleCategoryModelList;
    private SpinnerCategoryAdapter spinnerCategoryAdapter;
    private List<SingleColorModel> allColorsModels;
    private ColorsAdapter colorsAdapter;
    private String type;
    private SingleProductModel singleProductModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);
        getdatafromintent();
        initView();
    }

    private void getdatafromintent() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("type") != null) {
            Log.e("dlldll", "dldlldl");
            type = intent.getStringExtra("type");
            singleProductModel = (SingleProductModel) intent.getSerializableExtra("data");

        }
    }


    private void initView() {
        singleCategoryModelList = new ArrayList<>();
        allColorsModels = new ArrayList<>();
        colorsAdapter = new ColorsAdapter(this, allColorsModels);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        presenter = new ActivityAddProductPresenter(this, this);
        binding.rdIn.setChecked(true);
        binding.rdcolor.setChecked(true);
        binding.rdunit.setChecked(true);
        addPRoductModel = new AddPRoductModel();
        addPRoductModel.setSellBy("unit");
        addPRoductModel.setStock("in_stock");
        addPRoductModel.setShowinsell("color");
        binding.setModel(addPRoductModel);
        binding.rdunit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //  binding.edtin.setVisibility(View.VISIBLE);
                    //binding.view.setVisibility(View.VISIBLE);
                    binding.rdweight.setChecked(false);
                    binding.rdunit.setSelected(true);
                    addPRoductModel.setSellBy("unit");
                    binding.setModel(addPRoductModel);

                }
            }
        });

        binding.rdweight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Log.e(";lll", b + "");
                if (b) {
                    binding.rdweight.setSelected(true);
                    // binding.edtin.setVisibility(View.GONE);
                    // binding.view.setVisibility(View.GONE);
                    binding.rdunit.setChecked(false);
                    addPRoductModel.setSellBy("weight");
                    binding.setModel(addPRoductModel);

                }
            }
        });
        binding.spcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    addPRoductModel.setDepartment("");
                } else {
                    addPRoductModel.setDepartment(singleCategoryModelList.get(i).getId() + "");

                }
                binding.setModel(addPRoductModel);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.rdIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.edtin.setVisibility(View.VISIBLE);
                    binding.view.setVisibility(View.VISIBLE);
                    binding.rdOut.setChecked(false);
                    binding.rdIn.setSelected(true);
                    addPRoductModel.setStock("in_stock");
                    binding.setModel(addPRoductModel);

                }
            }
        });

        binding.rdOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Log.e(";lll", b + "");
                if (b) {
                    binding.rdOut.setSelected(true);
                    binding.edtin.setVisibility(View.GONE);
                    binding.view.setVisibility(View.GONE);
                    binding.rdIn.setChecked(false);
                    addPRoductModel.setStock("out_stock");
                    binding.setModel(addPRoductModel);

                }
            }
        });
        binding.rdcolor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.recView.setVisibility(View.VISIBLE);
                    binding.consimage.setVisibility(View.GONE);

                    addPRoductModel.setShowinsell("color");
                    binding.setModel(addPRoductModel);

                }
            }
        });
        binding.rdimage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.consimage.setVisibility(View.VISIBLE);
                    binding.recView.setVisibility(View.GONE);

                    addPRoductModel.setShowinsell("image");
                    binding.setModel(addPRoductModel);

                }
            }
        });
        if(type!=null&&type.equals("update")){
            binding.tv.setText(getResources().getString(R.string.update_product));
            binding.btnConfirm.setText(getResources().getString(R.string.update));
        }
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type != null && type.equals("update")) {
                    presenter.checkDataupdate(addPRoductModel, userModel, singleProductModel);
                } else {
                    presenter.checkData(addPRoductModel, userModel);
                }
            }
        });
        binding.chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkReadPermission();
            }
        });
        binding.tackpicature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });
        binding.barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, ActivityScanner.class);
                startActivityForResult(intent, 100);
            }
        });
        binding.recView.setLayoutManager(new GridLayoutManager(this, 4));
        binding.recView.setAdapter(colorsAdapter);
        presenter.getcategories(userModel);

    }

    private void update() {
        Log.e("lll", "llll");
        if (singleProductModel.getBarcode_code() != null) {
            addPRoductModel.setBarcode(singleProductModel.getBarcode_code());
        }
        addPRoductModel.setCost(singleProductModel.getProduct_cost() + "");
        addPRoductModel.setDepartment(singleProductModel.getSingle_category().get(0).getId() + "");
        addPRoductModel.setImage_url(Tags.IMAGE_URL + singleProductModel.getImage());
        addPRoductModel.setName(singleProductModel.getTitle());
        addPRoductModel.setPrice(singleProductModel.getProduct_price() + "");
        addPRoductModel.setSellBy(singleProductModel.getProduct_type());
        addPRoductModel.setStock(singleProductModel.getStock_type());
        Log.e("ellekek",singleProductModel.getSku()+"");
        addPRoductModel.setStokamount(singleProductModel.getStock_amount()+"");
        if (singleProductModel.getSku() != null) {
            addPRoductModel.setSku(singleProductModel.getSku());
        }
        addPRoductModel.setShowinsell(singleProductModel.getDisplay_logo_type());
        Log.e("dlldldl", singleProductModel.getDisplay_logo_type());

        if (singleProductModel.getDisplay_logo_type().equals("color")) {
            addPRoductModel.setColor(singleProductModel.getColor_id() + "");
            addPRoductModel.setImage_url("");
            setselectioncolor();
            binding.rdcolor.setChecked(true);
            binding.rdimage.setChecked(false);

        } else {
            binding.rdcolor.setChecked(false);
            binding.rdimage.setChecked(true);
            binding.icon.setVisibility(View.GONE);

            Picasso.get().load(Tags.IMAGE_URL + singleProductModel.getImage()).into(binding.image);
        }
        if (singleProductModel.getProduct_type().equals("unit")) {
            binding.rdunit.setChecked(true);
            binding.rdweight.setChecked(false);
        } else {
            binding.rdunit.setChecked(false);
            binding.rdweight.setChecked(true);
        }
        if (singleProductModel.getStock_type().equals("in_stock")) {
            binding.rdIn.setChecked(true);
            binding.rdOut.setChecked(false);
        } else {
            binding.rdIn.setChecked(false);
            binding.rdOut.setChecked(true);
        }
        setselectiondepartment();
        binding.btnConfirm.setText(getResources().getString(R.string.update));
        binding.setModel(addPRoductModel);
    }

    private void setselectioncolor() {
        int pos = -1;
        for (int i = 0; i < allColorsModels.size(); i++) {
            if (allColorsModels.get(i).getId() == singleProductModel.getColor_id()) {
                pos = i;
                break;
            }
        }
        colorsAdapter.setI(pos);
    }

    private void setselectiondepartment() {
        int pos = -1;
        Log.e("ssss", singleCategoryModelList.size() + " " + singleProductModel.getSingle_category().get(0).getId());

        for (int i = 1; i < singleCategoryModelList.size(); i++) {
            Log.e("ssss", singleCategoryModelList.get(i).getId() + " " + singleProductModel.getSingle_category().get(0).getId());
            if (singleCategoryModelList.get(i).getId() == singleProductModel.getSingle_category().get(0).getId()) {
                pos = i;
                break;
            }
        }
        binding.spcat.setSelection(pos);

        //   spinnerCategoryAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        presenter.backPress();
    }


    @Override
    public void onFinished() {
        finish();
    }


    public void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {


        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }


    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            uri = data.getData();
            File file = new File(Common.getImagePath(this, uri));
            Picasso.get().load(file).fit().into(binding.image);
            binding.icon.setVisibility(View.GONE);
            addPRoductModel.setImage_url(uri.toString());
            binding.setModel(addPRoductModel);

        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = getUriFromBitmap(bitmap);
            binding.icon.setVisibility(View.GONE);

            if (uri != null) {
                addPRoductModel.setImage_url(uri.toString());
                binding.setModel(addPRoductModel);
                String path = Common.getImagePath(this, uri);
                if (path != null) {
                    Picasso.get().load(new File(path)).fit().into(binding.image);

                } else {
                    Picasso.get().load(uri).fit().into(binding.image);

                }
            }


        } else if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            binding.tvnum.setText(data.getStringExtra("code"));
            addPRoductModel.setBarcode(binding.tvnum.getText().toString());
            binding.setModel(addPRoductModel);
        }

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
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
        presenter.getcolors();

    }

    @Override
    public void oncolorSuccess(AllColorsModel allColorsModel) {
        allColorsModels.clear();
        allColorsModels.addAll(allColorsModel.getData());


        colorsAdapter.notifyDataSetChanged();
        if (singleProductModel != null) {
            update();
        }
    }

    @Override
    public void onSuccess() {
        finish();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void setcolor(int id) {
        Log.e("fkkfkfkkf", id + "");
        addPRoductModel.setColor(id + "");
        binding.setModel(addPRoductModel);

    }
}