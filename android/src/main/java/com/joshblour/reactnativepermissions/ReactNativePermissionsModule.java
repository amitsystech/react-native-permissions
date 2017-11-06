package com.joshblour.reactnativepermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.PromiseImpl;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.modules.permissions.PermissionsModule;

import java.util.Locale;

public class ReactNativePermissionsModule extends ReactContextBaseJavaModule {
  final ReactApplicationContext reactContext;
 final PermissionsModule mPermissionsModule;
  public static final int MY_PERMISSIONS_REQUEST_CODE = 99;
  public Promise _promise;
  public static ReactNativePermissionsModule rnPermissionModule;
  public enum RNType {
    LOCATION,
    CAMERA,
    MICROPHONE,
    CONTACTS,
    EVENT,
    STORAGE,
    PHOTO;
  }


  public ReactNativePermissionsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
      rnPermissionModule = this;
    mPermissionsModule = new PermissionsModule(this.reactContext);
  }

  @Override
  public String getName() {
    return "ReactNativePermissions";
  }


  @ReactMethod
  public boolean isAlertWindowPermissionGranted(String permission) {
    boolean flag = true;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && (PermissionChecker.checkSelfPermission(reactContext.getCurrentActivity(), permissionForString(permission))
            != PackageManager.PERMISSION_GRANTED)) {
      flag = false;
    }
    return flag;
  }

  @ReactMethod
  public void getPermissionStatus(String permission, String type, Promise promise) {

      int result = PermissionChecker.checkSelfPermission(reactContext.getCurrentActivity(), permissionForString(permission));
      switch (result) {
      case PermissionChecker.PERMISSION_DENIED:
        if (getCurrentActivity() != null) {
          boolean deniedOnce = ActivityCompat.shouldShowRequestPermissionRationale(reactContext.getCurrentActivity(), permissionForString(permission));
          promise.resolve(deniedOnce ? "denied" : "undetermined");
        } else {
          promise.resolve("denied");
        }
        break;
      case PermissionChecker.PERMISSION_DENIED_APP_OP:
        promise.resolve("denied");
        break;
      case PermissionChecker.PERMISSION_GRANTED:
        promise.resolve("authorized");
        break;
      default:
        promise.resolve("undetermined");
        break;
    }
  }

  @ReactMethod
  public void requestPermission(final String permissionString, String type, Promise promise) {
      _promise = promise;
    if (!isAlertWindowPermissionGranted(permissionString)) {
      ActivityCompat.requestPermissions(reactContext.getCurrentActivity(),
              new String[]{permissionForString(permissionString)},
              MY_PERMISSIONS_REQUEST_CODE);
    }
  }

  @ReactMethod
  public void checkGPSPermision(Promise promise){
      LocationManager locationManager = (LocationManager) reactContext.getSystemService(reactContext.LOCATION_SERVICE);
      if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
          promise.resolve(true);
      }else{
          promise.resolve(false);
      }
  }
  private String permissionForString(String permission) {
    switch (RNType.valueOf(permission.toUpperCase(Locale.ENGLISH))) {
      case LOCATION:
        return Manifest.permission.ACCESS_FINE_LOCATION;
      case CAMERA:
        return Manifest.permission.CAMERA;
      case MICROPHONE:
        return Manifest.permission.RECORD_AUDIO;
      case CONTACTS:
        return Manifest.permission.READ_CONTACTS;
      case EVENT:
        return Manifest.permission.READ_CALENDAR;
      case STORAGE:
      case PHOTO:
        return Manifest.permission.READ_EXTERNAL_STORAGE;
      default:
        return null;
    }
  }

  public void onReceiveUserActionPermission(int resultCode, int[] grantResults){
      switch (resultCode) {
          case MY_PERMISSIONS_REQUEST_CODE: {
              // If request is cancelled, the result arrays are empty.
              if (grantResults.length > 0
                      && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  _promise.resolve("authorized");

              } else {
                  _promise.resolve("denied");
              }
              return;
          }

      }
  }
}
