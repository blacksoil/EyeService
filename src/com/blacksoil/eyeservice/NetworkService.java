package com.blacksoil.eyeservice;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import android.app.Service;

public class NetworkService extends Service {
	private static final String TAG = "NetworkService";
	private final IBinder mBinder = new LocalBinder();

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	public class LocalBinder extends Binder {
		NetworkService getService() {
			return NetworkService.this;
		}
	}

    @Override
    public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
        return mBinder;
    }

	public interface ActionListener {
		public void onSuccess();
		public void onFailure(int reason);
	}

	/* methods for client start here*/
	public boolean isConnected() {
		return false;
	}

	public void connect(String ipAddr, int port, ActionListener listener) {
		throw new RuntimeException("Not implemented!");
	}

	public void disconnect() {
		throw new RuntimeException("Not implemented!");
	}

	public void sendMessage(int id, String message, ActionListener listener) {
		throw new RuntimeException("Not implemneted!");
	}
}
