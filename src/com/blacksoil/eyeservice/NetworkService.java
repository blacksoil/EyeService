package com.blacksoil.eyeservice;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import tutorial.Calculator;
import java.lang.Thread;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.blacksoil.eyeservice.R;

import android.app.Service;


class ConnectionHandler extends Handler {
	private final static String TAG = "ConnectionHandler";
	private static final String IP_ADDRESS = "192.168.0.14";
	private static final String HTTP_URL = "http://" + IP_ADDRESS + ":8080/php/PhpServer.php";

	private final static int STATE_IDLE = 0;
	private final static int STATE_BUSY = 1;
	public final static int MSG_CONNECT = 0;
	public final static int MSG_PING = 1;

	private int mState = 0;
	private static ConnectionHandler mInstance = null;

	Calculator.Client mTransferClient;
	TTransport mTransport;

	private ConnectionHandler(Looper looper) {
		super(looper);

	}

	public static ConnectionHandler getInstance() {
		return mInstance;
	}

	public static ConnectionHandler createInstance(Looper looper) {
		mInstance = new ConnectionHandler(looper);
		return mInstance;
	}

	@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "ConnectionHandler.handleMessage: msg.what=" + msg.what + " mState=" + mState);
			switch (msg.what) {
				case MSG_CONNECT:
				{
					Log.d(TAG, "mConnectionHandler.handleMessage: MSG_CONNECT");
					ActionListener am = (ActionListener) msg.obj;
					try {
						TProtocol proto;
						mTransport = new THttpClient(HTTP_URL);
						proto = new TBinaryProtocol(mTransport);
						mTransferClient = new Calculator.Client(proto);	
						Log.d(TAG, "handleMessage: mTransport.open() == " + mTransport.isOpen());
						if (mTransport.isOpen()) {
							proto = new TBinaryProtocol(mTransport);
							mTransferClient = new Calculator.Client(proto);	
							am.onSuccess();
							//mAsyncClient = Calculator.AsyncClient.Factory.getAsyncClient(mTransport);
						} else {
							am.onFailure(0);
						}
					} catch(TTransportException e) {
						Log.e(TAG, "handleMessage: MSG_INIT failed!");
					}
					break;
				}
				case MSG_PING:
				{
					Log.d(TAG, "mConnectionHandler.handleMessage: MSG_PING");
					ActionListener am = (ActionListener) msg.obj;
					if (mState != STATE_BUSY) {
						final ActionListener listener = (ActionListener) msg.obj;
						if (listener == null) {
							Log.d(TAG, "ConnectioNHandler.handleMessage: MSG_PING listener is null");
						}
						// TODO: Do thrift call for ping()
						try {
							mTransferClient.ping();
							listener.onSuccess();
						} catch(TException te) {
							Log.e(TAG, "mConnectionHandler.handleMessage: ping fails!");
							listener.onFailure(0);
						}
						/*
						final boolean success = false;
						final int reason = 0;

						// Here we need to make sure that onSuccess and onFailure 
						// are called on caller's thread, because otherwise it might 
						// used up network thread's resources
						listener.getHandler().post(new Runnable() {
							@Override
							public void run() {
								if (success) {
									listener.onSuccess();
								} else {
									listener.onFailure(reason);
								}
							}
						});
						*/
					} else {
						am.onFailure(1);
					}
					break;
				}
			}
		}
}


public class NetworkService extends Service {
	private static final String TAG = "NetworkService";
	private final IBinder mBinder = new LocalBinder();
	private ConnectionHandler mConnHandler;

	public NetworkService() {
		HandlerThread ht = new HandlerThread("NetworkThread");
		ht.start();
		mConnHandler = ConnectionHandler.createInstance(ht.getLooper());
		if (mConnHandler == null) {
			Log.e(TAG, "ConnectionHandler can't be instantiated!");
		}
	}

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


	/* methods for client start here*/
	public boolean isConnected() {
		return false;
	}

	public void ping(final ActionListener listener) {
		ActionListener am = new ActionListener(new Handler()) {
			@Override
			public void onSuccess() {
				Log.d(TAG,"ping: connect -> success. Invoking ping...");
				Message m = new Message();
				m.what = ConnectionHandler.MSG_PING;
				m.obj = (Object) listener;
				mConnHandler.sendMessage(m);
			}

			@Override
			public void onFailure(int reason) {
				Log.e(TAG, "ping: connect -> failure");
				listener.onFailure(reason);
			}
		};
		connect(am);
	}

	public void connect(ActionListener listener) {
		Message m = new Message();
		m.what = ConnectionHandler.MSG_CONNECT;
		m.obj = (Object) listener;
		mConnHandler.sendMessage(m);
	}

	public void disconnect() {
		throw new RuntimeException("Not implemented!");
	}

	public void sendMessage(int id, String message, ActionListener listener) {
		throw new RuntimeException("Not implemneted!");
	}
}
