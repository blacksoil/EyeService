package com.blacksoil.eyeservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.app.Service;

public class CoreService extends Service {
	private static final String TAG = "CoreService";
	private NetworkService mNetService = null;
	private boolean mNetBound = false;

	private static final int MSG_TEST = 0;
	private Handler mTestHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_TEST:
					Log.d(TAG, "mTestHandler.handleMessage: MSG_TEST");
					ActionListener am = new ActionListener(new Handler()) {
						@Override
						public void onSuccess() {
							Log.d(TAG, "mTestHandler.handleMessage: onSuccess!");
						}
						@Override
						public void onFailure(int reason) {
							Log.d(TAG, "mTestHandler.handleMessage: onFailure=" + reason);
						}
					};
					mNetService.ping(am);
					this.sendEmptyMessageDelayed(MSG_TEST, 3000);
				break;
			}
		}
	};

	private ServiceConnection mNetConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName cn, IBinder service) {
			if (service != null) {
				Log.d(TAG, "onServiceConnected: service retrieved!");
				mNetService = ((NetworkService.LocalBinder) service).getService();
				mNetBound = true;

				mTestHandler.sendEmptyMessage(MSG_TEST);
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
