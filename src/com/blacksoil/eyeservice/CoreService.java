package com.blacksoil.eyeservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import android.app.Service;

public class CoreService extends Service {
	private static final String TAG = "CoreService";
	private NetworkService mNetService = null;
	private boolean mNetBound = false;

	private ServiceConnection mNetConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName cn, IBinder service) {
			if (service != null) {
				Log.d(TAG, "onServiceConnected: service retrieved!");
				mNetService = ((NetworkService.LocalBinder) service).getService();
				mNetBound = true;
			} else {
				Log.d(TAG, "onServiceConnected: IBinder is null!");
			}
		}
		@Override
		public void onServiceDisconnected(ComponentName cn) {
			Log.d(TAG, "onServiceDisconnected");
			mNetBound = false;
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(TAG, "onStart: binding NetworkService");
		bindService(new Intent(this, NetworkService.class), mNetConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");

		if(mNetBound) {
			unbindService(mNetConnection);
			mNetBound = false;
		}
	}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
