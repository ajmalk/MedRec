package com.medrecbuddy.medrecbuddy;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medrecbuddy.R;

public class PatientAdapter extends ArrayAdapter<Patient>{
	
	private static class ViewHolder {
		ImageView photo;
        TextView first_name;
        TextView last_name;
        ViewGroup attrs1, attrs2;
    }
	
	public PatientAdapter(Context context, List<Patient> patients) {
		super(context, R.layout.patient, patients) ;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		Patient patient = this.getItem(position);
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
	    
		viewHolder.photo.setImageResource(patient.photo);
		viewHolder.first_name.setText(patient.firstName);
		viewHolder.last_name.setText(patient.lastName);
		
		PatientAttributeAdapter adapter = new PatientAttributeAdapter(getContext(), patient.attrs);
		viewHolder.attrs1.removeAllViews();
		viewHolder.attrs2.removeAllViews();
		for(int i = 0; i < adapter.getCount(); i++)
			if(i % 2 == 0)
				viewHolder.attrs1.addView(adapter.getView(i, null, null));
			else viewHolder.attrs2.addView(adapter.getView(i, null, null));
	    
		return convertView;
	}

}
