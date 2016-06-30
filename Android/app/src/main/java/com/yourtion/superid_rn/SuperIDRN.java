package com.yourtion.superid_rn;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ViewManager;
import com.isnc.facesdk.SuperID;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Yourtion on 6/30/16.
 */
public class SuperIDRN extends ReactContextBaseJavaModule implements ActivityEventListener {

    private boolean isRuning;
    private boolean isRegisted;
    private Promise mPromise;

    public SuperIDRN(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
        isRuning = false;
        isRegisted = false;
        mPromise = null;
    }

    private void cleanRunning() {
        isRuning = false;
        mPromise = null;
    }

    private boolean checkStatus(Promise promise) {
        if (!isRegisted) {
            promise.reject("not register", "Please RegisterApp First");
            return false;
        }
        if (isRuning) {
            promise.reject("meathd is runing", "Please wait");
            return false;
        }
        return true;

    }

    @Override
    public String getName() {
        return "SuperIDRN";
    }

    @ReactMethod
    public void version(Promise promise) {
        WritableMap map = Arguments.createMap();
        map.putString("build", SDKConfig.SDKVERSION);
        map.putString("version", SDKConfig.SDKVERSIONV);
        promise.resolve(map);
    }

    @ReactMethod
    public void debug(boolean debug) {
        SuperID.setDebugMode(debug);
    }

    @ReactMethod
    public void registe(String appId, String appSecret) {
        SuperID.initFaceSDK(getReactApplicationContext(), appId, appSecret);
        isRegisted = true;
    }

    @ReactMethod
    public void login(Promise promise) {
        if (!checkStatus(promise)) return;
        Activity activity = getCurrentActivity();
        if (activity != null) {
            SuperID.faceLogin(activity);
            isRuning = true;
            mPromise = promise;
        } else {
            promise.reject("-2", "Activity is null");
        }

    }

    @ReactMethod
    public void verify(int count, Promise promise) {
        if (!checkStatus(promise)) return;
        Activity activity = getCurrentActivity();
        if (activity != null) {
            SuperID.faceVerify(activity, count);
            isRuning = true;
            mPromise = promise;
        } else {
            promise.reject("-2", "Activity is null");
        }

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (isRuning && mPromise != null) {
            switch (resultCode) {
                case SDKConfig.AUTH_SUCCESS:
                case SDKConfig.LOGINSUCCESS:
                    String openid = Cache.getCached(getReactApplicationContext(), SDKConfig.KEY_OPENID);
                    String userInfo = Cache.getCached(getReactApplicationContext(), SDKConfig.KEY_APPINFO);
                    try {
                        JSONObject info = new JSONObject(userInfo);
                        WritableMap infoMap = JsonConvert.jsonToReact(info);
                        WritableMap map = Arguments.createMap();
                        map.putString("openId", openid);
                        map.putMap("userInfo", infoMap);
                        mPromise.resolve(map);

                    } catch (Exception e) {
                        Log.e("aaaa", e.toString());
                        mPromise.reject("-1", "JSON err");
                    }

                    break;
                case SDKConfig.VERIFY_SUCCESS:
                    mPromise.resolve(0);
                    break;
                case SDKConfig.VERIFY_FAIL:
                    mPromise.resolve(1);
                    break;

                default:
                    mPromise.reject("-1", "Fail");
                    break;
            }
            cleanRunning();
        }
    }
}

class SuperIDRNReactPackage implements ReactPackage {

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new SuperIDRN(reactContext));

        return modules;
    }
}
