package com.cryallen.commomhello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cryallen.commonlib.utils.LogUtils;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LogUtils.setDebuggable(true);
		LogUtils.d("MainActivity","test122323");
	}
}
