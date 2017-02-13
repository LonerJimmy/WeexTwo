package com.alibaba.weex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.weex.constants.Constants;
import com.alibaba.weex.https.HotRefreshManager;
import com.alibaba.weex.https.WXHttpManager;
import com.alibaba.weex.https.WXHttpTask;
import com.alibaba.weex.https.WXRequestListener;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.IWXDebugProxy;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by loner on 2017/2/12.
 */

public abstract class WeexBaseActivity extends AppCompatActivity implements IWXRenderListener, Handler.Callback {

    private static final String TAG = "MainActivity";
    private Handler mWXHandler;

    WXSDKInstance mWXSDKInstance;
    private HashMap mConfigMap = new HashMap<String, Object>();
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadWXfromService(getIndexUrl());
        initHotRefresh();
        startHotRefresh();
    }

    protected void initHotRefresh() {
        mWXHandler = new Handler(this);
        HotRefreshManager.getInstance().setHandler(mWXHandler);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        setContentView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }

    public void onBackPressed() {
        Log.e("USER ACTION", "BACK");
        WXSDKManager.getInstance().fireEvent(mWXSDKInstance.getInstanceId(), "_root", "androidback");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityDestroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            loadWXfromService(getIndexUrl());
        } else if (item.getItemId() == R.id.action_debug) {
            initDebugEnvironment(true, getHost());
            WXSDKEngine.reload();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * hot refresh
     */
    private void startHotRefresh() {
        try {
            String host = getHost();
            String wsUrl = "ws://" + host + ":8082";
            mWXHandler.obtainMessage(Constants.HOT_REFRESH_CONNECT, 0, 0, wsUrl).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract String getHost();

    /**
     * @return
     */
    private String getIndexUrl() {
        return "http://" + getHost() + ":12580/dist/main.js";
    }

    protected String getHotRefreshUrl() {
        return "http://" + getHost() + ":8081/weex_tmp/h5_render/" + setHotRefreshUrl() + ".js?wsport=8082";
    }

    protected abstract String setHotRefreshUrl();

    private void initDebugEnvironment(boolean enable, String host) {
        WXEnvironment.sRemoteDebugMode = enable;
        WXEnvironment.sRemoteDebugProxyUrl = "ws://" + host + ":8088/debugProxy/native";
    }

    private void loadWXfromLocal() {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.destroy();
        }

        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);

        mWXSDKInstance.render(WXFileUtils.loadAsset("dist/main.js", this));
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HOT_REFRESH_CONNECT:
                Log.e(TAG, msg.obj.toString());
                HotRefreshManager.getInstance().connect(msg.obj.toString());
                break;
            case Constants.HOT_REFRESH_REFRESH:
                Log.e("loner", getHotRefreshUrl());
                loadWXfromService(getIndexUrl());
                break;
            case Constants.HOT_REFRESH_DISCONNECT:
                HotRefreshManager.getInstance().disConnect();
                break;
            case Constants.HOT_REFRESH_CONNECT_ERROR:
                Toast.makeText(this, "hot refresh connect error!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    private void loadWXfromService(final String url) {

        if (mWXSDKInstance != null) {
            mWXSDKInstance.destroy();
        }

        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);

        WXHttpTask httpTask = new WXHttpTask();
        httpTask.url = url;
        httpTask.requestListener = new WXRequestListener() {

            @Override
            public void onSuccess(WXHttpTask task) {
                Log.i(TAG, "into--[http:onSuccess] url:" + url);
                try {
                    mConfigMap.put("bundleUrl", url);
                    mWXSDKInstance.render(TAG, new String(task.response.data, "utf-8"), mConfigMap, null, WXRenderStrategy.APPEND_ASYNC);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(WXHttpTask task) {
                Toast.makeText(getApplicationContext(), "network error!", Toast.LENGTH_SHORT).show();
            }
        };

        WXHttpManager.getInstance().sendRequest(httpTask);
    }

    private void registerBroadcastReceiver() {
        mReceiver = new WeexBaseActivity.RefreshBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IWXDebugProxy.ACTION_DEBUG_INSTANCE_REFRESH);
        registerReceiver(mReceiver, filter);
    }

    public class RefreshBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IWXDebugProxy.ACTION_DEBUG_INSTANCE_REFRESH.equals(intent.getAction())) {
                loadWXfromService(getIndexUrl());
            }
        }
    }
}
