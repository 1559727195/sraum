package com.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.Util.UpdateManager;
import com.Util.VersionUtil;
import com.base.Basecfragment;
import com.data.User;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.DeveloperActivity;
import com.massky.sraum.HelpYouActivity;
import com.massky.sraum.ProductActivity;
import com.massky.sraum.R;
import com.massky.sraum.YinSiActivity;
import com.permissions.RxPermissions;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.massky.sraum.R.id.aonerelative;

/**
 * Created by masskywcy on 2016-09-23.
 */
//关于界面
public class AboutFragment extends Basecfragment {
    @InjectView(R.id.afiverelative)
    RelativeLayout afiverelative;
    @InjectView(R.id.asixrelative)
    RelativeLayout asixrelative;
    @InjectView(R.id.asixrelative_upgrate)
    RelativeLayout asixrelative_upgrate;
    @InjectView(R.id.atworelative)
    RelativeLayout atworelative;
    @InjectView(R.id.afourrelative)
    RelativeLayout afourrelative;

    @InjectView(R.id.sideslip_id)
    RelativeLayout sideslip_id;
    @InjectView(R.id.addrelative_id)
    RelativeLayout addrelative_id;
    @InjectView(R.id.addimage_id)
    ImageView addimage_id;
    @InjectView(R.id.cenimage_id)
    ImageView cenimage_id;
    @InjectView(R.id.centext_id)
    TextView centext_id;
    private Button checkbutton_id, qxbutton_id;
    private TextView dtext_id, belowtext_id;
    private DialogUtil viewDialog;
    private int versionCode;
    private String Version;
    private static SlidingMenu mySlidingMenu;

    public static AboutFragment newInstance(SlidingMenu mySlidingMenu1) {
        AboutFragment newFragment = new AboutFragment();
        Bundle bundle = new Bundle();
        mySlidingMenu = mySlidingMenu1;
        newFragment.setArguments(bundle);
        return newFragment;

    }

    private void  chageSlideMenu () {
        if ( mySlidingMenu != null) {
            if ( mySlidingMenu.isMenuShowing()) {
                mySlidingMenu.showContent();
            } else {
                mySlidingMenu.showMenu();
            }
        }
    }

    @Override
    protected int viewId() {
        return R.layout.about;
    }

    @Override
    protected void onView() {
        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.VISIBLE);
        addimage_id.setVisibility(View.GONE);
        centext_id.setText("关于");
        versionCode = Integer.parseInt(VersionUtil.getVersionCode(getActivity()));
        afiverelative.setOnClickListener(this);
        asixrelative.setOnClickListener(this);
        asixrelative_upgrate.setOnClickListener(this);
        getDialog();
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(getActivity());
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
        sideslip_id.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    //用于设置dialog展示
    private void getDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.check, null);
        checkbutton_id = (Button) view.findViewById(R.id.checkbutton_id);
        qxbutton_id = (Button) view.findViewById(R.id.qxbutton_id);
        dtext_id = (TextView) view.findViewById(R.id.dtext_id);
        belowtext_id = (TextView) view.findViewById(R.id.belowtext_id);
        dtext_id.setText("发现新版本");
        checkbutton_id.setText("立即更新");
        qxbutton_id.setText("以后再说");
        viewDialog = new DialogUtil(getActivity(), view);
        checkbutton_id.setOnClickListener(this);
        qxbutton_id.setOnClickListener(this);
        atworelative.setOnClickListener(this);
        afourrelative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sideslip_id:
                chageSlideMenu();
                break;
            case R.id.checkbutton_id:
                viewDialog.removeviewDialog();
                String UpApkUrl = ApiHelper.UpdateApkUrl + "sraum" + Version + ".apk";
                UpdateManager manager = new UpdateManager(getActivity(), UpApkUrl);
                manager.showDownloadDialog();
                break;
            case R.id.qxbutton_id:
                viewDialog.removeviewDialog();
                break;
            case R.id.afiverelative:
                IntentUtil.startActivity(getActivity(), DeveloperActivity.class);
                break;
            case R.id.asixrelative:
                IntentUtil.startActivity(getActivity(),ProductActivity.class);
                break;
            case R.id.atworelative:
                IntentUtil.startActivity(getActivity(),HelpYouActivity.class);
                break;
            case R.id.afourrelative:
                IntentUtil.startActivity(getActivity(),YinSiActivity.class);
                break;
            case R.id.asixrelative_upgrate:
                viewDialog.loadDialog();
                about_togglen();
                break;
        }
    }

    private void about_togglen() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        MyOkHttp.postMapObject(ApiHelper.sraum_getVersion, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                about_togglen();
            }
        }, getActivity(), viewDialog) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                Version = user.version;
                int sracode = Integer.parseInt(user.versionCode);
                if (versionCode >= sracode) {
                    ToastUtil.showDelToast(getActivity(), "您的应用为最新版本");
                } else {
                    belowtext_id.setText("版本更新至" + Version);
                    viewDialog.loadViewdialog();
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
