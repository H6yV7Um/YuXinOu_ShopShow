package com.liren.live.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.liren.live.AppConfig;
import com.liren.live.AppContext;
import com.liren.live.R;
import com.liren.live.api.remote.ApiUtils;
import com.liren.live.api.remote.PhoneLiveApi;
import com.liren.live.base.BaseActivity;
import com.liren.live.bean.UserBean;
import com.liren.live.utils.LoginUtils;
import com.liren.live.utils.UIHelper;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;
import okhttp3.Response;


//登录选择页面
public class SelectLoginActivity extends BaseActivity implements PlatformActionListener {
    private String[] names = {QQ.NAME,Wechat.NAME, SinaWeibo.NAME};
    private String type;
    @BindView(R.id.iv_select_login_bg)
    ImageView mBg;

    @BindView(R.id.iv_qqlogin)
    ImageView mIvQQLogin;

    @BindView(R.id.iv_wxlogin)
    ImageView mIvWxLogin;

    @BindView(R.id.iv_sllogin)
    ImageView mIvSlLogin;

    private Bitmap bmp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_login;
    }


    @Override
    public void initView() {

        bmp = null;
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.live_show_login_bg);
        mBg.setImageBitmap(bmp);

    }

    @Override
    public void initData() {


        PhoneLiveApi.requestOtherLoginStatus(new StringCallback(){

            @Override
            public void onSuccess(String s, Call call, Response response) {
                JSONArray res = ApiUtils.checkIsSuccess(s);
                if(res != null){
                    try {
                        JSONObject object = res.getJSONObject(0);
                        if(object.getInt("login_qq") == 1){
                            mIvQQLogin.setVisibility(View.VISIBLE);
                        }
                        if(object.getInt("login_sina") == 1){
                            mIvSlLogin.setVisibility(View.VISIBLE);
                        }
                        if(object.getInt("login_wx") == 1){
                            mIvWxLogin.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    @Override
    @OnClick({R.id.tv_login_clause,R.id.iv_qqlogin,R.id.iv_sllogin,R.id.iv_wxlogin,R.id.iv_mblogin})
    public void onClick(View v) {
        int id = v.getId();
        ShareSDK.initSDK(this);
        switch (id){
            case R.id.tv_login_clause:
                //条款
                UIHelper.showWebView(SelectLoginActivity.this, AppConfig.MAIN_URL + "/index.php?g=portal&m=page&a=index&id=3","");
                break;
            case R.id.iv_qqlogin:
                type = "qq";
                otherLogin(names[0]);
                break;
            case R.id.iv_sllogin:
                type = "sina";
                AppContext.showToast("微博");
                otherLogin(names[2]);
                break;
            case R.id.iv_wxlogin:
                type = "wx";
                AppContext.showToast("微信");
                otherLogin(names[1]);
                break;
            case R.id.iv_mblogin:
                UIHelper.showMobilLogin(this);
                break;
        }
    }
    private void otherLogin(String name){
        showWaitDialog("正在跳转...",false);
        Platform other = ShareSDK.getPlatform(name);
        other.showUser(null);//执行登录，登录后在回调里面获取用户资料
        other.SSOSetting(false);  //设置false表示使用SSO授权方式
        other.setPlatformActionListener(this);
        other.removeAccount(true);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideWaitDialog();
                AppContext.showToast("授权成功正在登录....",0);
            }
        });

        //用户资源都保存到res
        //通过打印res数据看看有哪些数据是你想要的
        if (i == Platform.ACTION_USER_INFOR) {
            //showWaitDialog("正在登录...");
            PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据
            PhoneLiveApi.otherLogin(type,platDB,new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    JSONArray requestRes = ApiUtils.checkIsSuccess(s);
                    if(requestRes != null){
                        Gson gson = new Gson();

                        try {
                            UserBean user  = gson.fromJson(requestRes.getString(0), UserBean.class);

                            AppContext.getInstance().saveUserInfo(user);

                            LoginUtils.getInstance().OtherInit(SelectLoginActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else {
                        AppContext.showToast("登录失败",0);
                    }
                }


            });
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        hideWaitDialog();
        AppContext.showToast("授权登录失败",0);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        hideWaitDialog();
        AppContext.showToast("授权已取消",0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bmp!=null)
        bmp.recycle();
        OkHttpUtils.getInstance().cancelTag("requestOtherLoginStatus");
    }

}
