package com.cryallen.commonlib.widgets;

import android.app.ProgressDialog;
import android.content.Context;

/***
 * 等待提示dialog
 Created by chenran on 2018/6/30.
 ***/
public class WaitPorgressDialog extends ProgressDialog {
	public WaitPorgressDialog(Context context) {
		this(context, 0);
	}

	public WaitPorgressDialog(Context context, int theme) {
		super(context, theme);
		setCanceledOnTouchOutside(false);
	}
}
