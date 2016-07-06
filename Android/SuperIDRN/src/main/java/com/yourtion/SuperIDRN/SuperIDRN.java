package com.yourtion.SuperIDRN;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.isnc.facesdk.SuperID;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;

import org.json.JSONObject;

/**
 * Created by Yourtion on 7/5/16.
 */
public class SuperIDRN extends ReactContextBaseJavaModule implements ActivityEventListener {
    public static final String TAG = "com.yourtion.superid.SuperIDRN";

    private boolean isRunning;
    private boolean isRegisted;
    private Promise mPromise;

    public SuperIDRN(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
        isRunning = false;
        isRegisted = false;
        mPromise = null;
    }

    private void cleanRunning() {
        isRunning = false;
        mPromise = null;
    }

    private boolean checkStatus(Promise promise) {
        if (!isRegisted) {
            promise.reject("error", "Please RegisterApp First");
            return false;
        }
        if (isRunning) {
            promise.reject("error", "Method is running. Please wait");
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
            isRunning = true;
            mPromise = promise;
        } else {
            promise.reject("error", "Activity is null");
        }

    }

    @ReactMethod
    public void logout() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            SuperID.faceLogout(activity);
        }
    }

    @ReactMethod
    public void verify(int count, Promise promise) {
        if (!checkStatus(promise)) return;
        Activity activity = getCurrentActivity();
        if (activity != null) {
            SuperID.faceVerify(activity, count);
            isRunning = true;
            mPromise = promise;
        } else {
            promise.reject("error", "Activity is null");
        }

    }

    @ReactMethod
    public void faceFeature(Promise promise) {
        if (!checkStatus(promise)) return;
        Activity activity = getCurrentActivity();
        if (activity != null) {
            SuperID.getFaceFeatures(activity);
            isRunning = true;
            mPromise = promise;
        } else {
            promise.reject("error", "Activity is null");
        }

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (isRunning && mPromise != null) {
            switch (resultCode) {
                case SDKConfig.AUTH_SUCCESS:
                case SDKConfig.LOGINSUCCESS:
                    try {
                        String openid = Cache.getCached(getReactApplicationContext(), SDKConfig.KEY_OPENID);
                        String userInfo = Cache.getCached(getReactApplicationContext(), SDKConfig.KEY_APPINFO);
                        JSONObject info = new JSONObject(userInfo);
                        WritableMap infoMap = JSONConvert.jsonToReact(info);
                        WritableMap map = Arguments.createMap();
                        map.putString("openId", openid);
                        map.putMap("userInfo", infoMap);
                        mPromise.resolve(map);
                    } catch (Exception e) {
                        mPromise.reject("error", "JSON parse error");
                    }

                    break;

                case SDKConfig.GETEMOTIONRESULT:
                    try {
                        String featureInfo = intent.getStringExtra(SDKConfig.FACEDATA);
                        JSONObject info = new JSONObject(featureInfo);
                        WritableMap infoMap = JSONConvert.jsonToReact(info);
                        mPromise.resolve(infoMap);
                    } catch (Exception e) {
                        mPromise.reject("error", "JSON parse error");
                    }

                    break;

                case SDKConfig.VERIFY_SUCCESS:
                    mPromise.resolve(true);
                    break;

                case SDKConfig.VERIFY_FAIL:
                    mPromise.resolve(false);
                    break;

                case SDKConfig.PHONECODE_ERROR:
                case SDKConfig.ACCESSTOKENERROR:
                case SDKConfig.APPTOKENERROR:
                case SDKConfig.OTHER_ERROR:
                    mPromise.reject("error", "Error: " + Integer.toString(resultCode));
                    break;

                default:
                    mPromise.reject("fail", "Result Fail");
                    break;
            }
            cleanRunning();
        }
    }
}
