package com.medrecbuddy.medrecbuddy;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.medrecbuddy.R;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
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
			username = "ajmalk";
			password = "gatech2011";
			MongoClientURI uri = new MongoClientURI("mongodb://" + username + 
					":" + password + "@" + addr);
			aMongo = new MongoClient(uri);
			System.out.println("hellp");
			db = aMongo.getDB( "medrec_development" );
			System.out.println(db.getStats());
			patientCollection = db.getCollection("PatientsDoc");
			DBCollection userCollection = db.getCollection("Users");
			DBObject theUser = userCollection.findOne(new BasicDBObject("name", username));
			int userType = (Integer) theUser.get("userType");
			if (userType == 2) {
				patientCollection = db.getCollection("PatientsSec");
			}
			else {
			    patientCollection = db.getCollection("PatientsDoc");
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
					
					GridFS gfs = new GridFS(db, "photo");
					String photoName = patient.get("pic").toString();
					

					GridFSDBFile gfsFile = gfs.findOne(photoName);
					File outFile;
					outFile = File.createTempFile("tempPic", null, aContext.getCacheDir());
					gfsFile.writeTo(outFile);
					// ImageView iv = viewHolder.photo;
					//InputStream is = new FileInputStream(outFile);

					// iv.setImageBitmap(BitmapFactory.decodeStream(is));
					//outFile.delete();
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
		new GetUserTask().execute("psteele7"); 
	}
	
	public void clear(){
		patients.clear();
		adapter.notifyDataSetChanged();
	}
}
