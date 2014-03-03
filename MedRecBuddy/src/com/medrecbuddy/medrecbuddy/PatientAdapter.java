package com.medrecbuddy.medrecbuddy;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.medrecbuddy.R;

public class PatientAdapter extends ArrayAdapter<Patient>{
	
	private static class ViewHolder {
        TextView first_name;
        TextView last_name;
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
			viewHolder.first_name = (TextView) convertView.findViewById(R.id.first_name);
			viewHolder.last_name = (TextView) convertView.findViewById(R.id.last_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
	    
		viewHolder.first_name.setText(patient.firstName);
		viewHolder.last_name.setText(patient.lastName);
	    
		return convertView;
	}

}
