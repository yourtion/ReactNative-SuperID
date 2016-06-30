package com.yourtion.superid_rn;


import android.widget.Toast;

import com.facebook.react.ReactPackage;
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
import com.isnc.facesdk.common.SDKConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Yourtion on 6/30/16.
 */
public class SuperIDRN extends ReactContextBaseJavaModule {

    private boolean isRuning;
    private boolean isRegisted;
    private Promise mPromise;

    public SuperIDRN(ReactApplicationContext reactContext) {
        super(reactContext);
        isRuning = false;
        isRegisted = false;
    }

    private void cleanRuning () {
        isRuning = false;
        isRegisted = false;
        mPromise = null;
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
    }

    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
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
