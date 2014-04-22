package com.medrecbuddy.medrecbuddy;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.medrecbuddy.R;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

public class Database {
	private static MongoClientURI theURI = new MongoClientURI(
			"mongodb://ajmalk:gatech2011@ds037987.mongolab.com:37987/medrec_development");
	private static String addr = "ds037987.mongolab.com:37987/medrec_development";
	private MongoClient aMongo;
	private DB db;
	private DBCollection patientCollection;
	
	private List<DBObject> patients = new ArrayList<DBObject>();
	private PatientAdapter adapter;
	private Context aContext;
	private GridFS gfs;
	
	public static Database instance = new Database();
	
	public void setActivity(Activity activity){
		ListView patientList = (ListView) activity.findViewById(R.id.patient_list);
		adapter = new PatientAdapter(activity, patients);
		patientList.setAdapter(adapter);
		aContext = patientList.getContext();
	}
	
	public boolean connectToDatabase(String username, String password){
		try {
			System.out.println("connecting with " + username + " " + password);
			MongoClientURI uri = new MongoClientURI("mongodb://" + username + 
					":" + password + "@" + addr);
			aMongo = new MongoClient(uri);
			System.out.println("hellp");
			db = aMongo.getDB( "medrec_development" );
			System.out.println(db.getStats());
			patientCollection = db.getCollection("PatientsDoc");
			gfs = new GridFS(db, "photo");
			DBCollection userCollection = db.getCollection("Users");
			DBObject theUser = userCollection.findOne(new BasicDBObject("name", username));
			int userType = Integer.parseInt(theUser.get("userType").toString());
			if (userType == 2) {
				System.out.println("PatientsSec");
				patientCollection = db.getCollection("PatientsSec");
			}
			return true;
		} catch (UnknownHostException e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
			return false;
		}
	}
	
	private class GetUserTask extends AsyncTask<String, Void, Void> {
		@Override
	    protected Void doInBackground(String... IDs) {
			try { 
				System.out.println("find " + IDs[0]); 
				DBObject patient = patientCollection.findOne(new BasicDBObject("_id", IDs[0]));
				if(patient != null) {
					System.out.println("found " + patient); 
					patients.add(patient); 		
					String photoName = patient.get("pic").toString();
					GridFSDBFile gfsFile = gfs.findOne(photoName);
					File outFile = new File(aContext.getCacheDir(), photoName);
					gfsFile.writeTo(outFile);
				}
				else System.out.println("did not find " + IDs[0]); 
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
	
	public void findAndAdd(String ID){
		new GetUserTask().execute(ID); 
	}

	public void clear(){
		patients.clear();
		adapter.notifyDataSetChanged();
	}
}
