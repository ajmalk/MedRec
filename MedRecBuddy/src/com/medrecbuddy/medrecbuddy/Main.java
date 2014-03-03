package com.medrecbuddy.medrecbuddy;

import java.util.ArrayList;
import java.util.List;

import com.example.medrecbuddy.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class Main extends Activity {
	private List<Patient> patients = new ArrayList<Patient>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView patientList = (ListView) findViewById(R.id.patient_list);
		patientList.setAdapter(new PatientAdapter(this, patients));
		patients.add(new Patient("Ajmal", "Kunnummal"));
		patients.add(new Patient("Jill", "Cagz"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
