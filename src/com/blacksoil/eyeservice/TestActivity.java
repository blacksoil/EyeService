package com.blacksoil.eyeservice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.blacksoil.thrift.BinaryTransfer;
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

public class TestActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "TestActivity";
	private static final String IP_ADDRESS = "192.168.0.14";
	private static final int PORT = 8080;
	private static final String HTTP_URL = "http://" + IP_ADDRESS + ":8080/php/PhpServer.php";
	Button mPingButton;
	Calculator.Client mTransferClient;
	//Calculator.AsyncClient mAsyncClient;
	TTransport mTransport;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.test_activity);
		mPingButton = (Button) findViewById(R.id.button);
		mPingButton.setOnClickListener(this);
		initNetwork();
	}

	private Handler mNetHandler;
    private class ThriftNetworkHandler extends Handler {
		public static final int MSG_INIT = 0;
		public static final int MSG_BTN_PING = 1;

        public ThriftNetworkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
				case MSG_INIT:
						Log.d(TAG, "handleMessage: MSG_INIT");
						TProtocol proto;
						try {
							mTransport = new THttpClient(HTTP_URL);
							mTransport.open();
							Log.d(TAG, "handleMessage: mTransport.open() == " + mTransport.isOpen());
							if (mTransport.isOpen()) {
								proto = new TBinaryProtocol(mTransport);
								mTransferClient = new Calculator.Client(proto);	
								//mAsyncClient = Calculator.AsyncClient.Factory.getAsyncClient(mTransport);
							}
						} catch(TTransportException e) {
							Log.e(TAG, "handleMessage: MSG_INIT failed!");
						}
						break;
                case MSG_BTN_PING:
					Log.d(TAG, "handleMessage: MSG_BTN_PING");
                    try {
						if (mTransferClient == null) {
							throw new TException("Not currently connected!");
						}
						mTransferClient.ping();
						int result = mTransferClient.add(1,1);
						Log.d(TAG, "handleMessage: 1+1=" + result);
					} catch (TException e) {
                        Log.d(TAG, "MSG_BTN_PING" + e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), "Not connected! Connecting...", Toast.LENGTH_LONG).show();
						//mNetHandler.sendEmptyMessage(MSG_INIT);
                    }
                    break;
            }
        }
    }
	private void initNetwork() {
		Log.d(TAG, "initNetwork");
		HandlerThread t = new HandlerThread("network_thread");
		t.start();  
		mNetHandler = new ThriftNetworkHandler(t.getLooper());
		mNetHandler.sendEmptyMessage(ThriftNetworkHandler.MSG_INIT);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == mPingButton.getId()) {
			Log.d(TAG, "onClick: mPingButton");
			if (mTransport.isOpen()) {
				mNetHandler.sendEmptyMessage(ThriftNetworkHandler.MSG_BTN_PING);
			} else {
				Log.d(TAG, "onClick: transport isn't opened! opening...");
				initNetwork();
				Log.d(TAG, "onClick: sending MSG_BTN_PING");
				mNetHandler.sendEmptyMessage(ThriftNetworkHandler.MSG_BTN_PING);
			}
		}
	}

}
