package com.medrecbuddy.medrecbuddy;

public class Patient {
	
	class BloodPressure{
		Integer Sys, Dia;
		public String toString() {return Sys + "/" + Dia;}
	}
	
	String 	firstName, lastName; 
	Integer	height, weight, cholesterol; 
	String BloodType;
	BloodPressure bp; 
	
	public Patient(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
}