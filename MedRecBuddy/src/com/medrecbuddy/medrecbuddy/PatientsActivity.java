package com.medrecbuddy.medrecbuddy;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.medrecbuddy.R;

public class PatientsActivity extends Activity {
	private Set<String> BIDs = new HashSet<String>();
	
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private final BroadcastReceiver bIDFound = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            if(device.getName() != null && !BIDs.contains(device.getName())){
		            BIDs.add(device.getName());
		            System.out.println(device.getName());
		            Database.instance.findAndAdd(device.getName());
	            }
	            else System.out.println("null or duplicate");
	            System.out.println(BIDs);
	        }
	    }
	};
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				System.out.println("Bluetooth turned on");
				discoverPatients();
			}
			else System.out.println("Bluetooth enable failed");
		}
	}
	
	protected void discoverPatients() {
		if (mBluetoothAdapter.isDiscovering()) {
			System.out.println("discovering");
			mBluetoothAdapter.cancelDiscovery();
		}
		else System.out.println("not discovering");
		System.out.println("start discovering");
		mBluetoothAdapter.startDiscovery();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patients);
		
		Database.instance.setActivity(this);

		registerReceiver(bIDFound, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			System.out.println("Device does not support Bluetooth"); 
		}
		else System.out.println("Device supports Bluetooth");
		if (!mBluetoothAdapter.isEnabled()) {
			System.out.println("Bluetooth disabled");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		else {
			System.out.println("Bluetooth already enabled");
			discoverPatients();
		}
	}
	
	protected void onDestroy(Bundle savedInstanceState) {
		unregisterReceiver(bIDFound);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
