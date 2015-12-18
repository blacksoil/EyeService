package com.blacksoil.eyeservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class CoreStarter extends BroadcastReceiver {
	private String TAG = "CoreStarter";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.d(TAG, "onReceive: ACTION_BOOT_COMPLETED");
			if (context != null) {
				Log.d(TAG, "onReceive: startService...");
				context.startService(new Intent(context, CoreService.class));
			} else {
				Log.d(TAG, "onReceive: context is null!");
			}
		}
	}
}
