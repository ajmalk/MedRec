package com.medrecbuddy.medrecbuddy;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.medrecbuddy.R;

public class PatientAttributeAdapter extends ArrayAdapter<Patient.PatientAttr>{
	
	private static class ViewHolder {
        TextView attr_name;
        TextView val;
    }
	
	public PatientAttributeAdapter(Context context, List<Patient.PatientAttr> patients) {
		super(context, R.layout.patient, patients) ;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		Patient.PatientAttr patient = this.getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.patient_attr, null);
			viewHolder.attr_name = (TextView) convertView.findViewById(R.id.attr_name);
			viewHolder.val = (TextView) convertView.findViewById(R.id.val);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
	    
		viewHolder.attr_name.setText(patient.attr_name);
		viewHolder.val.setText(patient.val);
	    
		return convertView;
	}

}