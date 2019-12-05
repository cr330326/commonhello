package com.cryallen.commonlib.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.cryallen.commonlib.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * 权限申请帮助类
 * @author Allen
 * @DATE 2019-10-30
 ***/
public class PermissionHelpActivity extends Activity {
	private final static String TAG = PermissionHelpActivity.class.getSimpleName();
	private final static String INTENT_EXTRA = "permissions";
	private final static int PERMISSION_REQUEST_CODE = 42;

	private String[] permissionReq = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			handleIntent(getIntent());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	@TargetApi(Build.VERSION_CODES.M)
	private void handleIntent(Intent intent) {
		permissionReq = intent.getStringArrayExtra(INTENT_EXTRA);
		if (permissionReq == null) {
			LogUtils.d(TAG,"handleIntent permissionReq is null");
			finish();
			return;
		}
		ActivityCompat.requestPermissions(this, permissionReq, PERMISSION_REQUEST_CODE);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.M)
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		try {
			if (requestCode == PERMISSION_REQUEST_CODE) {
				LogUtils.d(TAG,"onRequestPermissionsResult permissions :" + Arrays.asList(permissions).toString());
				List<String> needRationalList = new ArrayList<>();
				List<String> totallyDeniedList = new ArrayList<>();
				List<String> grantedList = new ArrayList<>();
				if (permissions.length == 0) {
					if(permissionReq != null){
						needRationalList = Arrays.asList(permissionReq);
					}
				} else {
					for (int i = 0; i < permissions.length; i++) {
						LogUtils.d(TAG,"onRequestPermissionsResult permissions name :" + i +" -" + permissions[i]);
						LogUtils.d(TAG,"onRequestPermissionsResult grantResults name :" + i +" -" + grantResults[i]);
						if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
							boolean isRationale = shouldShowRequestPermissionRationale(permissions[i]);
							LogUtils.d(TAG,"onRequestPermissionsResult isRationale: " + isRationale);
							if (isRationale) {
								needRationalList.add(permissions[i]);
							} else {
								totallyDeniedList.add(permissions[i]);
							}
						} else {
							grantedList.add(permissions[i]);
						}
					}
				}
				PermissionHelper.getInstance().onHelperResult(grantedList, needRationalList, totallyDeniedList);
			}
			finish();
		}catch (Exception ex){
			LogUtils.e("onRequestPermissionsResult error:" + ex.getMessage());
		}
	}
}
