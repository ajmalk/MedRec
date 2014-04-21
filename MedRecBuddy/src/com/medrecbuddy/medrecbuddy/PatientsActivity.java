package com.medrecbuddy.medrecbuddy;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.medrecbuddy.R;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ListView;

public class PatientsActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 1;
	private Set<String> BIDs = new HashSet<String>();
	private List<DBObject> patients = new ArrayList<DBObject>();
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private MongoClientURI theURI = new MongoClientURI(
			"mongodb://MedRec:CherryPie@ds037987.mongolab.com:37987/medrec_development");
	
	private PatientAdapter adapter;
	
	private AsyncTask<String, Void, Void> connectToDatabase = new AsyncTask<String, Void, Void>(){
		@Override
	    protected Void doInBackground(String... URIs) {
			System.out.println("connecting");
			try {
				aMongo = new MongoClient(theURI);
//				System.out.println(aMongo);
				db = aMongo.getDB( "medrec_development" );
//				System.out.println(db.getStats());
				patientCollection = db.getCollection("PatientsDoc");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			return null;
		}
	};
	
	private class GetUserTask extends AsyncTask<String, Void, Void> {
		@Override
	    protected Void doInBackground(String... IDs) {
			try { 
				System.out.println("find " + IDs[0]); 
				DBCursor cursor = patientCollection.find(new BasicDBObject("fname", IDs[0])); 
				while(cursor.hasNext()) { 
					DBObject patient = cursor.next(); 
					System.out.println(patient); 
					patients.add(patient); 					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			System.out.println(patients);
			adapter.notifyDataSetChanged(); 
        }
	}
	
	private final BroadcastReceiver bIDFound = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            
//	            mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	            if(device.getName() != null && !BIDs.contains(device.getName())){
		            BIDs.add(device.getName());
		            System.out.println(device.getName());
		            new GetUserTask().execute("Jiwon");
	            }
	            else System.out.println("null or duplicate");
	            System.out.println(BIDs);
//	            DBCursor cursor = patientCollection.find(new BasicDBObject("id", device.getName()));
//				if(cursor.hasNext())
//	            	System.out.println(cursor.next());
	        }
	    }
	};
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				System.out.println("Bluetooth turned on");
				disc();
			}
			else System.out.println("Bluetooth enable failed");
		}
	}
	
	protected void disc() {
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

		ConnectivityManager connMgr = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			System.out.println("internet working"); 
			connectToDatabase.execute();
		} else {
			System.out.println("internet not working"); 
		}
		
		registerReceiver(bIDFound, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		
		ListView patientList = (ListView) findViewById(R.id.patient_list);
		adapter = new PatientAdapter(this, patients);
		patientList.setAdapter(adapter);
		
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
			disc();
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
