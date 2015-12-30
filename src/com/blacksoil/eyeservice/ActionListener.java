package com.blacksoil.eyeservice;

import android.os.Handler;


public abstract class ActionListener {
	private Handler mHandler = null;
	public ActionListener(Handler handler) {
		mHandler = handler;
	}

	// onSuccess and onFailure will be called through this handler
	public Handler getHandler() {
		return mHandler;
	}

	public abstract void onSuccess();
	public abstract void onFailure(int reason);
}
