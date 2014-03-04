package com.medrecbuddy.medrecbuddy;

import java.util.ArrayList;
import java.util.List;

import com.example.medrecbuddy.R;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

public class Main extends Activity {
	private List<Patient> patients = new ArrayList<Patient>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView patientList = (ListView) findViewById(R.id.patient_list);
		patientList.setAdapter(new PatientAdapter(this, patients));
		
		// add data
		patients.add(new Patient("Ajmal", "Kunnummal", R.drawable.ajmal));
	//	patients.add(new Patient("Jill", "Cagz", R.drawable.jill));
		patients.add(new Patient("Patrick","Steele", R.drawable.patrick));
		patients.add(new Patient("Jiwon", "Han", R.drawable.jiwon));
		patients.get(0).attrs.add(patients.get(0).new PatientAttr("Blood Type", "A+"));
		patients.get(0).attrs.add(patients.get(0).new PatientAttr("Cholesterol", "100"));
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
