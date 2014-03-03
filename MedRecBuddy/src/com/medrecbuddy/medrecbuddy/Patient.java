package com.medrecbuddy.medrecbuddy;

import java.util.ArrayList;
import java.util.List;

public class Patient {
	class PatientAttr { 
		String attr_name, val;
		public PatientAttr(String attr_name, String val) {
			this.attr_name = attr_name;
			this.val = val;
		}
	}
	
	class BloodPressure{
		Integer Sys, Dia;
		public String toString() {return Sys + "/" + Dia;}
	}
	
	String 	firstName, lastName; 
	Integer	photo, height, weight, cholesterol; 
	String BloodType;
	BloodPressure bp; 
	List<PatientAttr> attrs;
	
	public Patient(String firstName, String lastName, int photo) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.photo = photo;
		attrs = new ArrayList<PatientAttr>();
	}
}