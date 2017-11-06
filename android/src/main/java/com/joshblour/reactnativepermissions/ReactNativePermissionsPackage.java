package com.joshblour.reactnativepermissions;

import android.app.Activity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.bridge.JavaScriptModule;

public class ReactNativePermissionsPackage implements ReactPackage {
    public ReactNativePermissionsModule rnPermissionModule;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        rnPermissionModule = new ReactNativePermissionsModule(reactContext);
      return Arrays.<NativeModule>asList(rnPermissionModule);
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
      return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
    }

    public void onReceiveUserActionPermission(int resultCode, int[] grantResults){
        rnPermissionModule.onReceiveUserActionPermission(resultCode, grantResults);
    }
}
