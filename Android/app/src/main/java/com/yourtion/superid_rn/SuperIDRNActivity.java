package com.yourtion.superid_rn;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;

public class SuperIDRNActivity extends ReactActivity {

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;


    @Override
    protected String getMainComponentName() {
        return "SimpleApp";
    }

    @Override
    protected boolean getUseDeveloperSupport() {
        return true;
    }

    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new SuperIDRNReactPackage());
    }
}
