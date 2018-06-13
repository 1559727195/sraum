package com.massky.sraum;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.AppManager;
import com.Util.BitmapUtil;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.PopwindowUtil;

import com.Util.SharedPreferencesUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.name;

/**
 * Created by masskywcy on 2016-09-20.
 */
//账号管理界面
public class AccountnumberActivity extends Basecactivity {
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.aonerelative)
    RelativeLayout aonerelative;
    @InjectView(R.id.athreerelative)
    RelativeLayout athreerelative;
    @InjectView(R.id.logoutrelative)
    RelativeLayout logoutrelative;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.asixrelative)
    RelativeLayout asixrelative;
    @InjectView(R.id.afourrelative)
    RelativeLayout afourrelative;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.asevenrelative)
    RelativeLayout asevenrelative;
    @InjectView(R.id.atworelative)
    RelativeLayout atworelative;
    @InjectView(R.id.headportrait)
    CircleImageView headportrait;
    @InjectView(R.id.container)
    View container;
    @InjectView(R.id.sextext_id)
    TextView sextext_id;
    @InjectView(R.id.birthdaytext_id)
    TextView birthdaytext_id;
    @InjectView(R.id.nickNameaaccount)
    TextView nickNameaaccount;
    @InjectView(R.id.mobilePho)
    TextView mobilePho;
    @InjectView(R.id.myfamliy)
    TextView myfamliy;

    Drawable[] thumbPicIds;
    private DialogUtil dialogUtil, dialogBack;
    private View v, view;
    private Button cancelbtn_id, camera_id, photoalbum, cancle_id, save_id, qxbutton_id, checkbutton_id;
    private TextView dtext_id, belowtext_id;
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //调用照相机返回图片临时文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type = 1;
    private final int SEX = 2;
    private final int BIRTH = 3;
    //用于popwindow加载布局
    private PopwindowUtil popwindowUtil, nickPopwindow;
    private EditText acctext_id;
    private boolean popflag;

    @Override
    protected int viewId() {
        return R.layout.accountnumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        headportrait.setImageBitmap(BitmapUtil.stringtoBitmap((String)
                SharedPreferencesUtil.getData(AccountnumberActivity.this, "avatar", "")));
        addViewid();
        getAccountInfo();
        thumbPicIds = new Drawable[]{headportrait.getDrawable()};
        titlecen_id.setText(R.string.accountnumber);
        onclick();
        deleteDialog();
        createCameraTempFile(savedInstanceState);
    }

    //删除dialog提示框
    private void deleteDialog() {
        View view = getLayoutInflater().inflate(R.layout.check, null);
        dtext_id = (TextView) view.findViewById(R.id.dtext_id);
        belowtext_id = (TextView) view.findViewById(R.id.belowtext_id);
        qxbutton_id = (Button) view.findViewById(R.id.qxbutton_id);
        checkbutton_id = (Button) view.findViewById(R.id.checkbutton_id);
        dtext_id.setText("退出登录");
        belowtext_id.setText("确定退出登录吗？");
        qxbutton_id.setOnClickListener(this);
        checkbutton_id.setOnClickListener(this);
        dialogBack = new DialogUtil(AccountnumberActivity.this, view);
    }

    private void onclick() {
        atworelative.setOnClickListener(this);
        backrela_id.setOnClickListener(this);
        logoutrelative.setOnClickListener(this);
        asevenrelative.setOnClickListener(this);
        headportrait.setOnClickListener(this);
        cancelbtn_id.setOnClickListener(this);
        photoalbum.setOnClickListener(this);
        camera_id.setOnClickListener(this);
        asixrelative.setOnClickListener(this);
        aonerelative.setOnClickListener(this);
        cancle_id.setOnClickListener(this);
        save_id.setOnClickListener(this);
        athreerelative.setOnClickListener(this);
        afourrelative.setOnClickListener(this);
    }

    @Override
    protected void onView() {
    }

    private void addViewid() {
        view = LayoutInflater.from(AccountnumberActivity.this).inflate(R.layout.accountpop, null);
        v = LayoutInflater.from(AccountnumberActivity.this).inflate(R.layout.camera, null);
        cancelbtn_id = (Button) v.findViewById(R.id.cancelbtn_id);
        photoalbum = (Button) v.findViewById(R.id.photoalbum);
        camera_id = (Button) v.findViewById(R.id.camera_id);
        acctext_id = (EditText) view.findViewById(R.id.acctext_id);
        cancle_id = (Button) view.findViewById(R.id.cancle_id);
        save_id = (Button) view.findViewById(R.id.save_id);
        dialogUtil = new DialogUtil(this, v, 1);
        popwindowUtil = new PopwindowUtil(aonerelative, view, this, 0, 0);
        nickPopwindow = new PopwindowUtil(atworelative, view, this, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                AccountnumberActivity.this.finish();
                break;
            case R.id.logoutrelative:
                dialogBack.loadViewdialog();
                break;
            case R.id.qxbutton_id:
                dialogBack.removeviewDialog();
                break;
            case R.id.checkbutton_id:
                dialogBack.removeviewDialog();
                SharedPreferencesUtil.saveData(AccountnumberActivity.this, "editFlag", false);
                SharedPreferencesUtil.saveData(AccountnumberActivity.this, "loginflag", false);
                IntentUtil.startActivityAndFinishFirst(AccountnumberActivity.this, LoginActivity.class);
                AppManager.getAppManager().finishAllActivity();
                break;
            case R.id.asevenrelative:
                Intent mintent = new Intent(AccountnumberActivity.this, MyfamilyActivity.class);
                startActivity(mintent);
                break;
            case R.id.headportrait:
                dialogUtil.loadViewBottomdialog();
                break;
            case R.id.cancelbtn_id:
                dialogUtil.removeviewBottomDialog();
                break;
            case R.id.photoalbum:
                //权限判断
                if (ContextCompat.checkSelfPermission(AccountnumberActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(AccountnumberActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    dialogUtil.removeviewBottomDialog();
                    gotoPhoto();
                }
                break;
            case R.id.camera_id:
                //跳转到调用系统图库
                if (ContextCompat.checkSelfPermission(AccountnumberActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(AccountnumberActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    dialogUtil.removeviewBottomDialog();
                    gotoCarema();
                }
                break;
            case R.id.asixrelative:
                Intent intentch = new Intent(AccountnumberActivity.this, ChangepassActivity.class);
                startActivity(intentch);
                break;
            case R.id.aonerelative:
                acctext_id.setText("");
                popflag = true;
                popwindowUtil.loadPopupwindow();
                break;
            case R.id.cancle_id:
                if (popflag) {
                    popwindowUtil.removePopwindow();
                } else {
                    nickPopwindow.removePopwindow();
                }
                break;
            case R.id.save_id:
                if (popflag) {
                    //点击确定更改用户名
                    popwindowUtil.removePopwindow();
                    updateUserId(acctext_id.getText().toString());
                } else {
                    nickPopwindow.removePopwindow();
                    updateAccountInfo(acctext_id.getText().toString(), sextext_id.getText().toString(),
                            birthdaytext_id.getText().toString(), mobilePho.getText().toString());
                }
                break;
            case R.id.athreerelative:
                Intent intent1sex = new Intent(AccountnumberActivity.this, SexActivity.class);
                startActivityForResult(intent1sex, SEX);
                break;
            case R.id.afourrelative:
                Intent intent1birthday = new Intent(AccountnumberActivity.this, BirthdayActivity.class);
                intent1birthday.putExtra("type", "birthday");
                startActivityForResult(intent1birthday, BIRTH);
                break;
            case R.id.atworelative:
                acctext_id.setText("");
                popflag = false;
                nickPopwindow.loadPopupwindow();
                break;
        }
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    //此处后面可以将bitMap转为二进制上传后台网络
                    updateAvatar(bitMap);
                }
                break;
            case SEX://性别选择
                if (intent != null) {
                    if (intent.getStringExtra("resultsex") != null) {
                        String resultsex = intent.getStringExtra("resultsex");// 取出传回来的数据
                        LogUtil.i("dn=1", resultsex + "这是性别 选择");
                        //sextext_id.setText(resultsex);
                        updateAccountInfo(nickNameaaccount.getText().toString(), resultsex,
                                birthdaytext_id.getText().toString(), mobilePho.getText().toString());
                    }
                }
                break;
            case BIRTH://生日选择
                if (intent != null) {
                    if (intent.getStringExtra("birthactivity") != null) {
                        String birthactivity = intent.getStringExtra("birthactivity");// 取出传回来的数据
                        LogUtil.i("", birthactivity + "onActivityResult: " + "这是设置");
                        updateAccountInfo(nickNameaaccount.getText().toString(), sextext_id.getText().toString(),
                                birthactivity, mobilePho.getText().toString());
                        // birthdaytext_id.setText(birthactivity);
                    }
                }
                break;
        }
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 外部存储权限申请返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCarema();
            } else {
                // Permission Denied
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            } else {
                // Permission Denied
            }
        }
    }

    //更新头像
    private void updateAvatar(final Bitmap bitMap) {
        dialogUtil.loadDialog();
        sraum_updateAvatar(bitMap);
    }

    private void sraum_updateAvatar(final Bitmap bitMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(AccountnumberActivity.this));
        map.put("avatar", BitmapUtil.bitmaptoString(bitMap));
        MyOkHttp.postMapObject(ApiHelper.sraum_updateAvatar, map, new Mycallback
                (new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_updateAvatar(bitMap);
                    }
                }, AccountnumberActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                SharedPreferencesUtil.saveData(AccountnumberActivity.this, "avatar",
                        BitmapUtil.bitmaptoString(bitMap));
                headportrait.setImageBitmap(bitMap);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    //更新用户名
    private void updateUserId(final String userId) {
        dialogUtil.loadDialog();
        sraum_updateUserId(userId);
    }

    private void sraum_updateUserId(final String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(AccountnumberActivity.this));
        map.put("userId", userId);
        MyOkHttp.postMapObject(ApiHelper.sraum_updateUserId, map, new Mycallback
                (new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_updateUserId(userId);
                    }
                }, AccountnumberActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                username.setText(userId);
                android.util.Log.e("peng","AccountnumberActivity->name:" + username.getText().toString());
                SharedPreferencesUtil.saveData(AccountnumberActivity.this, "userName", username.getText().toString());
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    //用于更新用户
    private void updateAccountInfo(final String nickName, final String gender, final String birthDay, final String mobilePhone) {
        account_num_sraum_update(nickName, gender, birthDay, mobilePhone);
    }

    private void account_num_sraum_update(final String nickName, final String gender, final String birthDay,final String mobilePhone) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapInfo = new HashMap<String, Object>();
        mapInfo.put("nickName", nickName);
        mapInfo.put("gender", gender);
        mapInfo.put("birthDay", birthDay);
        mapInfo.put("mobilePhone", mobilePhone);
        map.put("token", TokenUtil.getToken(AccountnumberActivity.this));
        map.put("userInfo", mapInfo);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_updateAccountInfo, map, new Mycallback
                (new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        account_num_sraum_update(nickName, gender, birthDay, mobilePhone);
                    }
                }, AccountnumberActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                nickNameaaccount.setText(nickName);
                sextext_id.setText(gender);
                birthdaytext_id.setText(birthDay);
                android.util.Log.e("peng","AccountnumberActivity->name:" + username.getText().toString());
                SharedPreferencesUtil.saveData(AccountnumberActivity.this, "userName", username.getText().toString());
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    //账号个人基本信息
    private void getAccountInfo() {
        dialogUtil.loadDialog();
        account_number_Act();
    }

    private void account_number_Act() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(AccountnumberActivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getAccountInfo, map, new Mycallback
                (new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        account_number_Act();
                    }
                }, AccountnumberActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                User.userinfo userinfo = user.userInfo;
                username.setText(userinfo.userId);
                SharedPreferencesUtil.saveData(AccountnumberActivity.this, "userName", username.getText().toString());
                nickNameaaccount.setText(userinfo.nickname);
                sextext_id.setText(userinfo.gender);
                birthdaytext_id.setText(userinfo.birthDay);
                mobilePho.setText(userinfo.mobilePhone);
                myfamliy.setText("我的家庭" + "(" + userinfo.family + ")");
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }
}
