package com.bibi.ui.my_promotion;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gyf.barlibrary.ImmersionBar;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.PagerAdapter;
import com.bibi.app.GlobalConstant;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.base.BaseFragment;
import com.bibi.entity.SummaryEntity;
import com.bibi.entity.User;
import com.bibi.ui.address.AddressPresenter;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulPermissionUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionActivity extends BaseActivity implements PromotionContract.View {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.ibShare)
    ImageView ibShare;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.view_back)
    View view_back;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.tvTotalCount)
    TextView tvTotalCount;
    @BindView(R.id.tvTotalExchangeAmount)
    TextView tvTotalExchangeAmount;
    @BindView(R.id.tvPromotionExchangeAmount)
    TextView tvPromotionExchangeAmount;
    @BindView(R.id.tvLastweekPromotionExchangeAmount)
    TextView tvLastweekPromotionExchangeAmount;
    @BindView(R.id.tvAmount)
    TextView tvAmount;

    private User user;
    //    private List<String> tabs = new ArrayList<>();
//    private List<BaseFragment> fragments = new ArrayList<>();
    private PopupWindow popWnd;
    private String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ATC/";
    private Bitmap saveBitmap;
    private LinearLayout llPromotion;
    private PromotionContract.Presenter presenter;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PromotionActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.tvPromotion)
    public void startPromotionInfo() {
        PromotionInfoActivity.actionStart(this);
    }

    @OnClick(R.id.llRecord)
    public void startPromotionRecord() {
        PromotionRecordActivity.actionStart(this);
    }

    @OnClick(R.id.llReward)
    public void startPromotionReward() {
        PromotionRewardActivity.actionStart(this);
    }

    @OnClick(R.id.llOut)
    public void startPromotionOut() {
        PromotionOutActivity.actionStart(this);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_promotion_new;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new PromotionPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        user = MyApplication.getApp().getCurrentUser();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoadingPopup();
                finish();
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoadingPopup();
                finish();
            }
        });
        ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
    }

    private void showPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PromotionActivity.this, R.style.dialog_a);
        final Dialog dialog = builder.create();
        dialog.show();
        LayoutInflater inflater = LayoutInflater.from(PromotionActivity.this);
        View v = inflater.inflate(R.layout.dialog_erweima, null);
        dialog.getWindow().setContentView(v);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.7);
        dialog.getWindow().setAttributes(layoutParams);
//        ImageView cha=v.findViewById(R.id.cha);
//        cha.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        ImageView cha = v.findViewById(R.id.cha);
        cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ImageView ivPromotion = v.findViewById(R.id.ivPromotion);
        ivPromotion.post(new Runnable() {
            @Override
            public void run() {
                if (WonderfulStringUtils.isEmpty(user.getPromotionPrefix(), user.getPromotionCode()))
                    return;
                saveBitmap = createQRCode(user.getPromotionPrefix() + user.getPromotionCode(), Math.min(ivPromotion.getWidth(), ivPromotion.getHeight()));
                ivPromotion.setImageBitmap(saveBitmap);
            }
        });
        ivPromotion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!WonderfulPermissionUtils.isCanUseStorage(PromotionActivity.this))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else try {
                    save(getBitmap(ivPromotion));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(PromotionActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    try {
                        save(getBitmap(llPromotion));
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

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        presenter.summary(this.getToken());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(PromotionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getSummarySuccess(SummaryEntity obj) {
        tvCount.setText(getResources().getString(R.string.direct_push) + "：" + obj.getInviteCount());
        tvTotalCount.setText(getResources().getString(R.string.number_of_team) +"：" + obj.getTotalInviteCount());
        tvTotalExchangeAmount.setText(getResources().getString(R.string.total_transaction_amount) +"（USDT）：" + obj.getTotalExchangeAmount());
        tvAmount.setText(getResources().getString(R.string.class_and_commission) +"（USDT）：" + obj.getPromotionAwardAmount());
        String promotionExchangeAmount = obj.getPromotionExchangeAmount() == null ? "0.0" : obj.getPromotionExchangeAmount();
        String lastweekPromotionExchangeAmount = obj.getLastweekPromotionExchangeAmount() == null ? "0.0" : obj.getLastweekPromotionExchangeAmount();
        tvPromotionExchangeAmount.setText(getResources().getString(R.string.my_promotion_transaction) +"：" + promotionExchangeAmount);
        tvLastweekPromotionExchangeAmount.setText(getResources().getString(R.string.my_promotional_last) +"：" + lastweekPromotionExchangeAmount);
    }
}
