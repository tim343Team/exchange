package com.bibi.ui.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tifezh.kchartlib.utils.ViewUtil;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.HoldEntity;
import com.bibi.entity.User;
import com.bibi.ui.setting.ContractActivity;
import com.bibi.utils.WonderfulPermissionUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/10
 */
public class ShareActivity extends BaseActivity {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvEarnings)
    TextView tvEarnings;
    @BindView(R.id.tvSmobl)
    TextView tvSmobl;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvOriginPrice)
    TextView tvOriginPrice;
    @BindView(R.id.tvStopPrice)
    TextView tvStopPrice;

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
    }

    @BindView(R.id.iv_share_image)
    ImageView iv_share_image;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;

    private HoldEntity item;
    private String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ATC/";

    @OnClick(R.id.tv_save)
    public void saveImage() {
        if (!WonderfulPermissionUtils.isCanUseStorage(this)) {
            checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
        } else {
            try {
                save(getBitmap(ll_share));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void actionStart(Context context, HoldEntity holdEntity) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra("holdEntity", holdEntity);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        User user = MyApplication.getApp().getCurrentUser();
        if (WonderfulStringUtils.isEmpty(user.getPromotionPrefix(), user.getPromotionCode())) {
            return;
        }
        Bitmap saveBitmap = createQRCode(user.getPromotionPrefix() + user.getPromotionCode(), ViewUtil.Dp2Px(this, 70));
        iv_share_image.setImageBitmap(saveBitmap);
    }

    @Override
    protected void obtainData() {
        item = (HoldEntity) getIntent().getSerializableExtra("holdEntity");
        if (item != null) {
            double earning = (item.getProfitLost() / item.getMarginMoney()) * 100;
            if (earning >= 0) {
                tvEarnings.setTextColor(getResources().getColor(R.color.color_00b274));
            } else {
                tvEarnings.setTextColor(getResources().getColor(R.color.typeRed));
            }
            tvEarnings.setText(new DecimalFormat("#0.00").format(earning) + "%");
            tvSmobl.setText(item.getSymbol());
            if (item.getDirection().equals("BUY")) {
                tvType.setTextColor(getResources().getColor(R.color.color_00b274));
            } else {
                tvType.setTextColor(getResources().getColor(R.color.typeRed));
            }
            tvType.setText(item.getDirection().equals("BUY") ? getResources().getString(R.string.text_duo):getResources().getString(R.string.text_kong)) ;
            tvOriginPrice.setText(item.getPrice() + "");
            tvStopPrice.setText(item.getCurrentPrice() + "");
        }
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    try {
                        save(getBitmap(ll_share));
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

    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 0);   //设置白边大小 取值为 0- 4 越大白边越大
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

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }
}
