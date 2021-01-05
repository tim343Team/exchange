package com.bibi.ui.recharge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gyf.barlibrary.ImmersionBar;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import butterknife.OnClick;
import com.bibi.R;
import com.bibi.app.GlobalConstant;
import com.bibi.base.BaseActivity;
import com.bibi.entity.Coin;
import com.bibi.app.UrlFactory;
import com.bibi.entity.CoinBean;
import com.bibi.entity.ContractCoin;
import com.bibi.ui.common.ChooseCoinActivity;
import com.bibi.ui.my_promotion.PromotionInfoActivity;
import com.bibi.ui.wallet.ChongBiJLActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulBitmapUtils;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulPermissionUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.Request;

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvAddressText)
    TextView tvAddressText;
    @BindView(R.id.ivAddress)
    ImageView ivAddress;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvAddressTag)
    TextView tvAddressTag;
    @BindView(R.id.llAlbum)
    LinearLayout llAlbum;
    @BindView(R.id.llCopy)
    TextView llCopy;
    @BindView(R.id.llCopyTag)
    TextView llCopyTag;
    @BindView(R.id.tvTip)
    TextView tvTip;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.llAddressTag)
    LinearLayout llAddressTag;
    private String coinName;
    private Bitmap saveBitmap;
    @BindView(R.id.view_back)
    View view_back;

    @OnClick(R.id.ibRegist)
    public void startChargeRecord() {
        ChongBiJLActivity.actionStart(this, null);
    }

    private String address;
    private String tag;
    private String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ATC/";

    public static void actionStart(Context context, String coinName) {
        Intent intent = new Intent(context, RechargeActivity.class);
        intent.putExtra("coinName", coinName);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy();
            }
        });
        llCopyTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyTag();
            }
        });
        llAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!WonderfulPermissionUtils.isCanUseStorage(RechargeActivity.this))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else try {
                    save(getBitmap(ivAddress));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void obtainData() {
        coinName = getIntent().getStringExtra("coinName");
        if(coinName.equals("XRP")){
            llAddressTag.setVisibility(View.VISIBLE);
        }else {
            llAddressTag.setVisibility(View.GONE);
        }
        tvTitle.setText(coinName + getResources().getString(R.string.chargeMoney));
        tvAddressText.setText(coinName + getResources().getString(R.string.chargeMoneyAddress));
        String sInfoFormat = getResources().getString(R.string.chargeTip);
        String sFinalInfo = String.format(sInfoFormat, coinName);
        tvTip.setText(sFinalInfo);
        huoqu();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        obtainData();
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(RechargeActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    try {
                        save(getBitmap(ivAddress));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    WonderfulToastUtils.showToast(getResources().getString(R.string.storage_permission));
                    break;
            }
        }
    };

        /**
     * @param view 需要截取图片的view
     * @return 截图
     */
    private Bitmap getBitmap(View view) throws Exception {
        if (view == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-view.getScrollX(), -view.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        view.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }

    private void save(Bitmap saveBitmap) {
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = simpleDate.format(now.getTime());
        File folderFile = new File(dir);
        if (!folderFile.exists()) {
            // mkdir() 不会创建多级目录，所以导致后面的代码报错 提示文件或目录不存在
            // folderFile.mkdir();
            // mkdirs() 则会创建多级目录
            folderFile.mkdirs();
        }
        File file = new File(dir + fileName + ".jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建文件输出流对象用来向文件中写入数据
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //将bitmap存储为jpg格式的图片
        saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        //刷新文件流
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(file);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        WonderfulToastUtils.showToast(getResources().getString(R.string.savesuccess));
    }

    private void huoqu() {
        WonderfulOkhttpUtils.post().url(UrlFactory.getRechargeUrl()).addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                .addParams("coinName", coinName)
                .build()
                .execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                hideLoadingPopup();
            }

            @Override
            public void onResponse(String response) {
                hideLoadingPopup();
                WonderfulLogUtils.logi("充值地址记录回执：", "充值地址记录回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<ContractCoin> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ContractCoin>>() {
                        }.getType());
                        for (int i = 0; i < objs.size(); i++) {
                            ContractCoin coin1 = objs.get(i);
                            if (coinName.equals(coin1.getCoin().getUnit())) {
                                address = (coin1.getAddress());
                                tag = (coin1.getTag());
                                break;
                            }
                        }
                        if (TextUtils.isEmpty(address)) {
                            WonderfulToastUtils.showToast(getResources().getString(R.string.creating_address));
                        } else {
                            erciLoad();
                        }
                        tvAddressTag.setText(tag);
                    } else {
                        WonderfulCodeUtils.checkedErrorCode(RechargeActivity.this, object.optInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void copy() {
        WonderfulCommonUtils.copyText(this, tvAddress.getText().toString());
        WonderfulToastUtils.showToast(R.string.copy_success);
    }

    private void copyTag() {
        WonderfulCommonUtils.copyText(this, tvAddressTag.getText().toString());
        WonderfulToastUtils.showToast(R.string.copy_success);
    }

    @Override
    protected void fillWidget() {

    }

    private void erciLoad() {
        if (tvTitle == null) {
            return;
        }

        if (TextUtils.isEmpty(address)) {
            tvAddress.setText(getResources().getString(R.string.unChargeMoneyTip1));
        } else {
            tvAddress.setText(address);
            ivAddress.post(new Runnable() {
                @Override
                public void run() {
                    saveBitmap = createQRCode(address, Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
                    ivAddress.setImageBitmap(saveBitmap);
                }
            });
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 2);   //设置白边大小 取值为 0- 4 越大白边越大
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

//    @OnClick(R.id.ll_choose_coin)
//    public void startChooseCoin(){
//        ChooseCoinActivity.actionStart(this, ChooseCoinActivity.TYPE_CHARGE);
//
//    }


}
