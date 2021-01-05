package com.bibi.ui.main.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m7.imkfsdk.KfStartHelper;
import com.m7.imkfsdk.utils.PermissionUtils;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bibi.ui.main.asset.AssetActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import com.bibi.R;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.app.UrlFactory;
import com.bibi.base.BaseTransFragment;
import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.entity.Message;
import com.bibi.entity.SafeSetting;
import com.bibi.entity.User;
import com.bibi.ui.aboutus.AboutUsActivity;
import com.bibi.ui.address.AddressActivity;
import com.bibi.ui.bind_user_name.BindUserNameActivity;
import com.bibi.ui.credit.CreditActivity;
import com.bibi.ui.credit.CreditInfoActivity;
import com.bibi.ui.dialog.HeaderSelectDialogFragment;
import com.bibi.ui.login.LoginActivity;
import com.bibi.ui.main.MainContract;
import com.bibi.ui.main.asset.ReportActivity;
import com.bibi.ui.message_detail.MessageDetailActivity;
import com.bibi.ui.my_promotion.PromotionActivity;
import com.bibi.ui.myinfo.MyInfoActivity;
import com.bibi.ui.recharge.ReportDialogFragment;
import com.bibi.ui.setting.SettingActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulBitmapUtils;
import com.bibi.utils.WonderfulFileUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulPermissionUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.WonderfulUriUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;
import zendesk.core.AnonymousIdentity;
import zendesk.core.Identity;
import zendesk.core.Zendesk;
import zendesk.support.Support;
import zendesk.support.request.RequestActivity;
import zendesk.support.requestlist.RequestListActivity;

import static android.app.Activity.RESULT_OK;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/13
 */
public class MineFragment extends BaseTransFragment implements MainContract.MineView {
    public static final String TAG = MineFragment.class.getSimpleName();
    public static Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    private SafeSetting safeSetting;

    @BindView(R.id.ll_credit)
    LinearLayout ll_credit;
    @BindView(R.id.llMemberGrade)
    LinearLayout llMemberGrade;
    @BindView(R.id.ivMemberGrade)
    ImageView ivMemberGrade;
    @BindView(R.id.tvMemberGrade)
    TextView tvMemberGrade;
    @BindView(R.id.tv_user_name)
    TextView tvNickName;
    @BindView(R.id.tv_user_id)
    TextView tv_user_id;
    @BindView(R.id.img_credit)
    ImageView imgCredit;
    @BindView(R.id.ivHeader)
    ImageView ivHeader;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;

    private File imageFile;
    private String filename = "userAvart.jpg";
    private Uri imageUri;
    private HeaderSelectDialogFragment headerSelectDialogFragment;
    private MainContract.MinePresenter presenter;
    private List<Message> messageList = new ArrayList<>();
    private List<String> info = new ArrayList<>();
    private List<Integer> infoss = new ArrayList<>();

    @OnClick(R.id.tv_user_name)
    public void modifyUsername() {
        BindUserNameActivity.actionStart(getmActivity(), tvNickName.getText().toString());
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    showHeaderSelectDialog();
                    break;
                case GlobalConstant.PERMISSION_STORAGE:
                    chooseFromAlbum();
                    break;
                default:
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_CAMERA:
                    WonderfulToastUtils.showToast(getResources().getString(R.string.camera_permission));
                    break;
                case GlobalConstant.PERMISSION_STORAGE:
                    WonderfulToastUtils.showToast(getResources().getString(R.string.storage_permission));
                    break;
                default:
            }
        }
    };

    @OnClick(R.id.ivHeader)
    public void startHeader() {
        toCamera(GlobalConstant.PERMISSION_CAMERA, Permission.CAMERA);
    }

    @OnClick(R.id.ivSetting)
    public void startSetting() {
        SettingActivity.actionStart(getActivity());
    }

    @OnClick(R.id.ll_about_us)
    public void startAbout() {
        AboutUsActivity.actionStart(getActivity());
    }

    @OnClick(R.id.llAddress)
    public void startAddress() {
        AddressActivity.actionStart(getActivity());
    }

    @OnClick(R.id.llCustomer)
    public void startCustomer() {
        handlePermission();
    }

    @OnClick(R.id.llPromotion)
    public void startPromotion() {
        if (MyApplication.getApp().isLogin()) {
            PromotionActivity.actionStart(getActivity());
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @OnClick(R.id.ll_credit)
    public void startCredit() {
        if (MyApplication.getApp().isLogin()) {
            if (safeSetting.getRealAuditing() == 1) {//审核中
                WonderfulToastUtils.showToast(getResources().getString(R.string.creditting));
            } else {
                if (safeSetting.getRealNameRejectReason() != null) {//失败
                    CreditInfoActivity.actionStart(getActivity(), CreditInfoActivity.AUDITING_FILED, safeSetting.getRealNameRejectReason());
                } else {//未认证
                    CreditInfoActivity.actionStart(getActivity(), CreditInfoActivity.UNAUDITING, safeSetting.getRealNameRejectReason());
                }
            }
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
//        CreditInfoActivity.actionStart(getActivity(), CreditInfoActivity.UNAUDITING, safeSetting.getRealNameRejectReason());
    }

    @OnClick(R.id.ll_security_center)
    public void startSecurityCenter() {
        if (MyApplication.getApp().isLogin()) {
            MyInfoActivity.actionStart(getActivity());
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @OnClick(R.id.ll_asset)
    public void startAssetView() {
        if (MyApplication.getApp().isLogin()) {
            AssetActivity.actionStart(getActivity());
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        imageFile = WonderfulFileUtils.getCacheSaveFile(getmActivity(), filename);
    }

    @Override
    protected void obtainData() {
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        getMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLoginStatus();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (marqueeView != null) {
                marqueeView.stopFlipping();
            }
        } else {
            if (marqueeView != null) {
                if (!marqueeView.isFlipping()) {
                    marqueeView.startFlipping();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginActivity.RETURN_LOGIN:
                if (resultCode == RESULT_OK && getUserVisibleHint() && MyApplication.getApp().isLogin()) {
                    loginingViewText();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    notLoginViewText();
                }
                break;
            case GlobalConstant.TAKE_PHOTO:
                try {
                    Glide.with(this).load(imageFile).centerCrop().skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(ivHeader);

                    //上传
                    Bitmap bitmap = WonderfulBitmapUtils.loadBitmap(new FileInputStream(imageFile), ivHeader.getWidth(), ivHeader.getHeight());
                    if(bitmap!=null){
                        WonderfulBitmapUtils.saveBitmapToFile(bitmap, imageFile, 100);
                        String base64Data = WonderfulBitmapUtils.imgToBase64(bitmap);
                        bitmap.recycle();
                        presenter.uploadBase64Pic(SharedPreferenceInstance.getInstance().getTOKEN(), "data:image/jpeg;base64," + base64Data, 1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case GlobalConstant.CHOOSE_ALBUM:
                if (resultCode != RESULT_OK) {
                    return;
                }
                imageUri = data.getData();
                if (Build.VERSION.SDK_INT >= 19) {
                    imageFile = WonderfulUriUtils.getUriFromKitKat(getmActivity(), imageUri);
                } else {
                    imageFile = WonderfulUriUtils.getUriBeforeKitKat(getmActivity(), imageUri);
                }
                if (imageFile == null) {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.library_file_exception));
                    return;
                }
                WonderfulLogUtils.logi("CreditActivity  ", imageFile + "data.getData()   " + imageFile.getAbsolutePath());
                Bitmap bm = null;
                try {
                    bm = WonderfulBitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivHeader.getWidth(), ivHeader.getHeight());
                } catch (Exception e) {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.library_file_exception));
                }
                ivHeader.setImageBitmap(bm);
                presenter.uploadBase64Pic(SharedPreferenceInstance.getInstance().getTOKEN(), "data:image/jpeg;base64," + WonderfulBitmapUtils.imgToBase64(bm), 2);
                break;
            default:
        }
    }

    public void refreshLoginStatus() {
        if (MyApplication.getApp().isLogin()) {
            loginingViewText();
        } else {
            notLoginViewText();
        }
    }

    private void loginingViewText() {
        getSafeSetting();
        User user = MyApplication.getApp().getCurrentUser();
        //TODO
//        tvNickName.setText(WonderfulStringUtils.getHideString(user.getMobile()));
        tvNickName.setText(user.getNickname());
        tv_user_id.setText("UID:" + user.getId());
        String url = user.getAvatar();
        if (WonderfulStringUtils.isEmpty(url))
            Glide.with(getmActivity()).load(R.mipmap.icon_default_header).centerCrop().into(ivHeader);
        else Glide.with(getmActivity()).load(url).centerCrop().into(ivHeader);
    }

    private void getSafeSetting() {
        RemoteDataSource.getInstance().safeSetting(getmActivity().getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                if (obj == null) {
                    return;
                }
                safeSetting = (SafeSetting) obj;
                MyApplication.realVerified = safeSetting.getRealVerified();
                ll_credit.setEnabled(safeSetting.getRealVerified() == 1 ? false : true);
                imgCredit.setVisibility(safeSetting.getRealVerified() == 1 ? View.GONE : View.VISIBLE);
                if(safeSetting.getMemberGradeId()==1){
                    llMemberGrade.setVisibility(View.GONE);
                    ivMemberGrade.setBackgroundResource(R.drawable.icon_member_grade_1);
                    tvMemberGrade.setText(getResources().getString(R.string.agent));
                }else if(safeSetting.getMemberGradeId()==2){
                    llMemberGrade.setVisibility(View.GONE);
                    ivMemberGrade.setBackgroundResource(R.drawable.icon_member_grade_2);
                    tvMemberGrade.setText("IB");
                }else if(safeSetting.getMemberGradeId()==3){
                    llMemberGrade.setVisibility(View.GONE);
                    ivMemberGrade.setBackgroundResource(R.drawable.icon_member_grade_3);
                    tvMemberGrade.setText("MIB");
                }else if(safeSetting.getMemberGradeId()==4){
                    llMemberGrade.setVisibility(View.GONE);
                    ivMemberGrade.setBackgroundResource(R.drawable.icon_member_grade_4);
                    tvMemberGrade.setText("PIB");
                }else {
                    llMemberGrade.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                if (code == 4000) {
                    MyApplication.getApp().setCurrentUser(null);
                    SharedPreferenceInstance.getInstance().saveaToken("");
                    SharedPreferenceInstance.getInstance().saveTOKEN("");
                    notLoginViewText();
                }
            }
        });
    }

    private void notLoginViewText() {
        tvNickName.setText(getResources().getString(R.string.not_logged_in));
        ll_credit.setEnabled(false);
        imgCredit.setVisibility(View.GONE);
    }

    //头像相关
    private void toCamera(int requestCode, String[] permissions) {
        if (!WonderfulPermissionUtils.isCanUseCamera(getmActivity())) {
            checkPermission(requestCode, permissions);
        } else {
            showHeaderSelectDialog();
        }
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private void showHeaderSelectDialog() {
        try {
            if (headerSelectDialogFragment == null) {
                headerSelectDialogFragment = HeaderSelectDialogFragment.getInstance(getmActivity());
                headerSelectDialogFragment.setOnDeleteListener(new HeaderSelectDialogFragment.OperateCallback() {
                    @Override
                    public void toTakePhoto() {
                        if (!WonderfulPermissionUtils.isCanUseCamera(getmActivity())) {
                            checkPermission(GlobalConstant.PERMISSION_CAMERA, Permission.CAMERA);
                        } else {
                            startCamera();
                        }
                    }

                    @Override
                    public void toChooseFromAlbum() {
                        if (!WonderfulPermissionUtils.isCanUseStorage(getmActivity())) {
                            checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                        } else {
                            chooseFromAlbum();
                        }
                    }
                });
            }
            headerSelectDialogFragment.show(getmActivity().getSupportFragmentManager(), "header_select");
        } catch (Exception e) {

        }
    }

    private void startCamera() {
        if (imageFile == null) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.unknown_error));
            return;
        }
        imageUri = WonderfulFileUtils.getUriForFile(getmActivity(), imageFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, GlobalConstant.TAKE_PHOTO);
    }

    private void chooseFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GlobalConstant.CHOOSE_ALBUM);
    }

    @Override
    public void uploadBase64PicFail(Integer code, String toastMessage) {
        WonderfulToastUtils.showToast(toastMessage);
    }

    @Override
    public void changeAvatarFail(Integer code, String toastMessage) {
        WonderfulToastUtils.showToast(toastMessage);
    }

    @Override
    public void uploadBase64PicSuccess(String obj, int type) {
        if (WonderfulStringUtils.isEmpty(obj)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.empty_address));
            return;
        }
        presenter.changeAvatar(SharedPreferenceInstance.getInstance().getTOKEN(), obj);
    }

    @Override
    public void changeAvatarSuccess(String obj, String url) {
        WonderfulToastUtils.showToast(obj);
        User user = MyApplication.getApp().getCurrentUser();
        user.setAvatar(url);
        MyApplication.getApp().setCurrentUser(user);
        if (WonderfulStringUtils.isEmpty(url))
            Glide.with(getmActivity()).load(R.mipmap.icon_default_header).centerCrop().into(ivHeader);
        else Glide.with(getmActivity()).load(url).centerCrop().into(ivHeader);
    }

    @Override
    public void setPresenter(MainContract.MinePresenter presenter) {
        this.presenter = presenter;
    }

    private void getMessage() {
        WonderfulOkhttpUtils.post().url(UrlFactory.getMessageUrl())
                .addParams("pageNo", 1 + "").addParams("pageSize", "100")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Message> messages = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<Message>>() {
                        }.getType());
                        messageList.clear();
                        messageList.addAll(messages);
                        setMarqueeView(messageList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                MessageDetailActivity.actionStart(getActivity(), messageList.get(infoss.get(position)).getId());
            }
        });
    }

    private void setMarqueeView(List<Message> messageList) {
        info.clear();
        int code = SharedPreferenceInstance.getInstance().getLanguageCode();
        if (code == 1) {
            //中文
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                /* if (isContainChinese(message.getTitle())) {*/
                String str = "";
                if (message.getTitle().length() > 15) {
                    str = message.getTitle();
                    str = str.substring(0, 15);
                    info.add(str + "...");
                } else {
                    info.add(message.getTitle());
                }
                infoss.add(i);
                /*}*/
            }

        } else {
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                if (!isContainChinese(message.getTitle())) {
                    info.add(message.getTitle());
                    infoss.add(i);
                }
            }
        }
        if (info.size() > 0) {
            marqueeView.startWithList(info);
        }
    }

    public static boolean isContainChinese(String str) {
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 文件写入权限 （初始化需要写入文件，点击在线客服按钮之前需打开文件写入权限）
     * 读取设备 ID 权限 （初始化需要获取用户的设备 ID）
     */
    private void handlePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.hasAlwaysDeniedPermission(getmActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                PermissionUtils.requestPermissions(getmActivity(), 0x11, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        User user = MyApplication.getApp().getCurrentUser();
                        Identity identity = new AnonymousIdentity.Builder()
                                .withNameIdentifier(user.getId()+"")
                                .build();
                        Zendesk.INSTANCE.setIdentity(identity);
                        Intent requestActivityIntent = RequestListActivity.builder()
                                .intent(getmActivity());
                        startActivity(requestActivityIntent);
                        //7room客服
//                        initSdk();
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        Toast.makeText(getmActivity(), R.string.notpermession, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }
                });
            }
        }
    }

    /**
     * 初始化聊天SDK
     */
    private void initSdk() {
        /*
          第一步:初始化help
         */
        final KfStartHelper helper = new KfStartHelper(getmActivity());

        /*
          商品信息实例，若有需要，请参照此方法；
         */

//        handleCardInfo(helper);

         /*
          新卡片类型示例，若有需要，请参照此方法；
         */
//        handleNewCardInfo(helper);

        /*
          第二步:设置参数
          初始化sdk方法，必须先调用该方法进行初始化后才能使用IM相关功能
          @param accessId       接入id（需后台配置获取）
          @param userName       用户名
          @param userId         用户id
         */

//        helper.initSdkChat("a453fa70-1b1d-11ea-a61e-079363b77025", "测试", "123");
        helper.initSdkChat("04e9eac0-787f-11ea-955e-0dbfe4d496f5", "测试", "123");
    }
}
