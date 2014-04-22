package com.medrecbuddy.medrecbuddy;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.medrecbuddy.R;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;



public class PatientAdapter extends ArrayAdapter<DBObject>{
	
	private static class ViewHolder {
		ImageView photo;
        TextView first_name;
        TextView last_name;
        ViewGroup attrs1, attrs2;
    }
	
	public PatientAdapter(Context context, List<DBObject> patients) {
		super(context, R.layout.patient, patients) ;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("add one item");
		DBObject patient = this.getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.patient, null);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
			viewHolder.first_name = (TextView) convertView.findViewById(R.id.first_name);
			viewHolder.last_name = (TextView) convertView.findViewById(R.id.last_name);
			viewHolder.attrs1 = (ViewGroup) convertView.findViewById(R.id.patient_attrs_1);
			viewHolder.attrs2 = (ViewGroup) convertView.findViewById(R.id.patient_attrs_2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
	    
		try {
			File imageFile = new File(getContext().getCacheDir(), "tempPic");
			FileInputStream fis = new FileInputStream(new File(getContext().getCacheDir(), "tempPic"));
			viewHolder.photo.setImageBitmap(BitmapFactory.decodeStream(fis));
			imageFile.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//viewHolder.photo.setImageResource(patient.get("photo"));
		viewHolder.first_name.setText(patient.get("fname").toString());
		viewHolder.last_name.setText(patient.get("lname").toString());
		
		BasicDBList attrs = (BasicDBList) patient.get("attributes");
		PatientAttributeAdapter adapter = new PatientAttributeAdapter(getContext(), attrs);
		viewHolder.attrs1.removeAllViews();
		viewHolder.attrs2.removeAllViews();
		for(int i = 0; i < adapter.getCount(); i++)
			if(i % 2 == 0)
				viewHolder.attrs1.addView(adapter.getView(i, null, null));
			else viewHolder.attrs2.addView(adapter.getView(i, null, null));
	    
		return convertView;
	}
	

}
