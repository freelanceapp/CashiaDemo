package com.cashiar.ui.activity_bill_Sell;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cashiar.R;
import com.cashiar.adapters.BluthoosAdapter;
import com.cashiar.adapters.ProductsSellAdapter;
import com.cashiar.adapters.SpinnerCustomerAdapter;
import com.cashiar.databinding.ActivityBillSellBinding;
import com.cashiar.databinding.ActivityPaymentSellBinding;
import com.cashiar.databinding.DialogBluthoosBinding;
import com.cashiar.databinding.DialogInpiutBinding;
import com.cashiar.language.Language;
import com.cashiar.models.AllCustomersModel;
import com.cashiar.models.BillModel;
import com.cashiar.models.CreateOrderModel;
import com.cashiar.models.ItemCartModel;
import com.cashiar.models.PaymentModel;
import com.cashiar.models.PdfDocumentAdpter;
import com.cashiar.models.SingleCustomerSuplliersModel;
import com.cashiar.models.UserModel;
import com.cashiar.mvp.activity_bill_sell_mvp.ActivitBillSellPresenter;
import com.cashiar.mvp.activity_bill_sell_mvp.BillSellActivityView;
import com.cashiar.mvp.activity_payment_sell_mvp.ActivityPAymentSellPresenter;
import com.cashiar.mvp.activity_payment_sell_mvp.PaymentSellActivityView;
import com.cashiar.preferences.Preferences;
import com.cashiar.share.Common;
import com.cashiar.tags.Tags;
import com.cashiar.ui.activity_add_Customer.AddCustomerActivity;
import com.cashiar.ui.activity_cart_buy.CartBuyActivity;
import com.chaos.view.BuildConfig;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.paperdb.Paper;

import static android.os.Build.VERSION_CODES.M;

public class BillSellActivity extends AppCompatActivity implements BillSellActivityView {
    private ActivityBillSellBinding binding;
    private ActivitBillSellPresenter presenter;
    private String lang;
    private UserModel userModel;
    private ProgressDialog dialog;
    private BillModel billModel;
    private CreateOrderModel createOrderModel;
    private double paid;
    private String taxamount = "0";
    private Preferences preferences;
    private List<ItemCartModel> itemCartModelList;
    private ProductsSellAdapter productsSellAdapter;
    private String currecny = "";
    private final String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int write_req = 100;
    //    private final String bluthoos_perm = Manifest.permission.BLUETOOTH;
//    private final String bluthoosadmin_perm = Manifest.permission.BLUETOOTH_ADMIN;
//
//    private final int bluthoos_req = 200;
//
    private boolean isPermissionGranted = false;
    private Image image;
//    WifiManager wifi;
//    List<ScanResult> results;
//    BluthoosAdapter bluthoosAdapter;
//    private AlertDialog dialog2;
//    private int size;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_sell);
        getdatafromintent();
        checkWritePermission();
        // checkBluthoosPermission();
        initView();

    }

//    private void checkBluthoosPermission() {
//
//        if (ContextCompat.checkSelfPermission(this, bluthoos_perm) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, bluthoosadmin_perm) != PackageManager.PERMISSION_GRANTED) {
//
//
//            isPermissionGranted = false;
//
//            ActivityCompat.requestPermissions(this, new String[]{bluthoos_perm, bluthoosadmin_perm}, bluthoos_req);
//
//
//        } else {
//            isPermissionGranted = true;
//        }
//    }

    private void checkWritePermission() {

        if (ContextCompat.checkSelfPermission(this, write_perm) != PackageManager.PERMISSION_GRANTED) {


            isPermissionGranted = false;

            ActivityCompat.requestPermissions(this, new String[]{write_perm}, write_req);


        } else {
            isPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == write_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true;
        }
    }

    private void getdatafromintent() {
        Intent intent = getIntent();
        if (intent != null) {
            createOrderModel = (CreateOrderModel) intent.getSerializableExtra("data");
            billModel = (BillModel) intent.getSerializableExtra("databill");
        }
    }


    private void initView() {
        itemCartModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setModel(createOrderModel);
        binding.setLogo("");
        binding.setCurrency("");
        binding.setAddress("");
        binding.setTax("");
        presenter = new ActivitBillSellPresenter(this, this);
        presenter.getprofile(userModel);
        binding.setBillmodel(billModel);

        binding.setTotal((createOrderModel.getTotal_price() - Double.parseDouble(taxamount) + createOrderModel.getDiscount_value()) + "");
        productsSellAdapter = new ProductsSellAdapter(this, itemCartModelList, currecny);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(productsSellAdapter);
        itemCartModelList.addAll(createOrderModel.getOrder_details());
        productsSellAdapter.notifyDataSetChanged();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(view -> {
            finish();
        });


        binding.btnsend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                takeScreenshot(1);
            }
        });
//        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse response) {
//
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse response) {
//
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//            }
//        });
//        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if (wifi.isWifiEnabled() == false) {
//            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
//            wifi.setWifiEnabled(true);
//        }
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context c, Intent intent) {
//                results = wifi.getScanResults();
//                size = results.size();
//               // Log.e("lxllx",size+"");
//            }
//        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                takeScreenshot(2);


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
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onCustomers() {
        Intent intent = new Intent(this, AddCustomerActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onsucess() {
        preferences.clearCart(this);
        finish();

    }

    @Override
    public void onprofileload(UserModel body) {
        this.taxamount = body.getTax_amount();
        currecny = body.getCurrency();
        binding.setCurrency(currecny);
        binding.setAddress(body.getAddress());
        binding.setTax(taxamount);
        binding.setTotal((createOrderModel.getTotal_price() - Double.parseDouble(taxamount) + createOrderModel.getDiscount_value()) + "");
        if (body.getLogo() != null) {
            binding.setLogo(body.getLogo());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void takeScreenshot(int mode) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now.toString().replaceAll(" ", "") + ".jpeg";

            // create bitmap screen capture
            ScrollView v1 = (ScrollView) getWindow().getDecorView().findViewById(R.id.scrollView);
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(v1, v1.getChildAt(0).getHeight(), v1.getChildAt(0).getWidth());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //setting screenshot in imageview
            String filePath = imageFile.getPath();
            Log.e("ddlldld", filePath);
            if (mode == 1) {
                shareImage(new File(filePath));
            } else {
                convertPDF(filePath);
                //sendData(filePath);
                //printPhoto(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",new File(filePath)));
            }
//   Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        } catch (Exception e) {
            // Several error may come out with file handling or DOM
            Log.e("ddlldld", e.toString());
        }
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.getprofile(userModel);
    }

    private void shareImage(File file) {
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setPackage("com.whatsapp");

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    // this will find a bluetooth printer device
    void findBT() {

        //try {
//            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//
//            if (!mBluetoothAdapter.isEnabled()) {
//                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBluetooth, 0);
//            }
//
//            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//            CreateDialogAlert(this, pairedDevices);
//
//            //    Toast.makeText(this, "Bluetooth device found." + mmDevice.getName() + pairedDevices.size(), Toast.LENGTH_LONG).show();
//            //     myLabel.setText("Bluetooth device found.");
//
//        } catch (Exception e) {
//            Log.e("ldkkd", e.toString());
//        }
//        if (size > 0) {
//CreateDialogAlert(this,results);
//        }
    }

//    public void CreateDialogAlert(Context context, List<ScanResult> bluetoothDeviceList) {
//        List<ScanResult> bluetoothDeviceList1 = new ArrayList<>();
//        bluetoothDeviceList1.addAll(bluetoothDeviceList);
//        dialog2 = new AlertDialog.Builder(context)
//                .create();
//
//        DialogBluthoosBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_bluthoos, null, false);
//        BluthoosAdapter bluetoothAdapter = new BluthoosAdapter(context, bluetoothDeviceList1);
//        binding.bluthoos.setLayoutManager(new LinearLayoutManager(context));
//        binding.bluthoos.setAdapter(bluetoothAdapter);
//
//        dialog2.getWindow().getAttributes().windowAnimations = R.style.Theme_App;
//        dialog2.setCanceledOnTouchOutside(false);
//        dialog2.setView(binding.getRoot());
//        dialog2.show();
//    }

    //    public void openBT(ScanResult scanResult) throws IOException {
//        try {
//            dialog2.dismiss();
//
//
//            // Standard SerialPortService ID
//            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//            mmSocket.connect();
//            mmOutputStream = mmSocket.getOutputStream();
//            inputStream = mmSocket.getInputStream();
//
//            beginListenForData();
//
//             myLabel.setText("Bluetooth Opened");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * after opening a connection to bluetooth printer device,
//     * we have to listen and check if a data were sent to be printed.
//     */
//    void sendData(String strPath) throws IOException {
//
//
//        Bitmap imageBit = BitmapFactory.decodeFile(strPath);
//
//        ByteArrayOutputStream blob = new ByteArrayOutputStream();
//        imageBit.compress(Bitmap.CompressFormat.PNG, 0, blob);
//        byte[] bitmapdata = blob.toByteArray();
//
//     //   binding.image.setImageBitmap(imageBit);
//
//        findBT();
//
//     //   mmOutputStream.write(bitmapdata);
//        // tell the user data were sent
//        //  myLabel.setText("Data Sent");
//
//    }
//
//    void beginListenForData() {
////        try {
////            final Handler handler = new Handler();
////
////            // this is the ASCII code for a newline character
////            final byte delimiter = 10;
////
////            stopWorker = false;
////            readBufferPosition = 0;
////            readBuffer = new byte[1024];
////
////            workerThread = new Thread(new Runnable() {
////                public void run() {
////
////                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
////
////                        try {
////
////                            int bytesAvailable = inputStream.available();
////
////                            if (bytesAvailable > 0) {
////
////                                byte[] packetBytes = new byte[bytesAvailable];
////                                inputStream.read(packetBytes);
////
////                                for (int i = 0; i < bytesAvailable; i++) {
////
////                                    byte b = packetBytes[i];
////                                    if (b == delimiter) {
////
////                                        byte[] encodedBytes = new byte[readBufferPosition];
////                                        System.arraycopy(
////                                                readBuffer, 0,
////                                                encodedBytes, 0,
////                                                encodedBytes.length
////                                        );
////
////                                        // specify US-ASCII encoding
////                                        final String data = new String(encodedBytes, "US-ASCII");
////                                        readBufferPosition = 0;
////
////                                        // tell the user data were sent to bluetooth printer device
////                                        handler.post(new Runnable() {
////                                            public void run() {
////                                                // myLabel.setText(data);
////                                            }
////                                        });
////
////                                    } else {
////                                        readBuffer[readBufferPosition++] = b;
////                                    }
////                                }
////                            }
////
////                        } catch (IOException ex) {
////                            stopWorker = true;
////                        }
////
////                    }
////                }
////            });
////
////            workerThread.start();
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }
////    void closeBT() throws IOException {
////        try {
////            stopWorker = true;
////            mmOutputStream.close();
////            mmInputStream.close();
////            mmSocket.close();
////            myLabel.setText("Bluetooth Closed");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//private void checkWifi(){
//    IntentFilter filter = new IntentFilter();
//    filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//    final WifiManager wifiManager =
//            (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);;
//    registerReceiver(new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context arg0, Intent arg1) {
//            // TODO Auto-generated method stub
//            Log.d("wifi","Open Wifimanager");
//
//            String scanList = wifiManager.getScanResults().toString();
//            Log.d("wifi","Scan:"+scanList);
//        }
//    },filter);
//    wifiManager.startScan();
//}
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void convertPDF(String path) {
        String FILE = Environment.getExternalStorageDirectory().toString() + "/FirstPdf.pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            try {
                image = Image.getInstance(path);
                // image.getHeight();
                //    document.setPageSize(new Rectangle(image.getAbsoluteX(),image.getAbsoluteY()));
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / image.getWidth()) * 100;
                float scaler1 = ((document.getPageSize().getHeight() - document.bottom()
                        - document.topMargin() - 0) / image.getHeight());// 0 means you have no indentation. If you have any, change it.
                image.scalePercent(scaler);
//                image.setAbsolutePosition(
//                        (document.getPageSize().getWidth() - image.getScaledWidth()) / 2,
//                        (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                image.scaleAbsoluteHeight(scaler1);
                image.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight() - 80);
                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                //  document.setPageSize(new com.itextpdf.text.Rectangle(image.getWidth(), image.getScaledHeight() * 200));

                document.add(image);
                document.close();
                //  document.add(new Paragraph("My Heading"));
printpdf();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            Log.e("message1", e.toString());
        } catch (FileNotFoundException e) {
            Log.e("message2", e.toString());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printpdf(){
        PrintManager printManager=(PrintManager) getSystemService(Context.PRINT_SERVICE);
try {
    PrintDocumentAdapter printDocumentAdapter=new PdfDocumentAdpter(BillSellActivity.this,Environment.getExternalStorageDirectory().toString() + "/FirstPdf.pdf");
    printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
}
catch (Exception e){

}
    }
}