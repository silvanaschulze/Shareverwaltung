package Kunden;

import InfoGeneral.InfoGeneral;



public class Kunden extends InfoGeneral {
	 String  Branche;
	 
	 public Kunden (int id, String vorname, String nachname, String straße,String plz,String ort, double telefon, String email, String branche) {
	 super(id, vorname, nachname, straße, plz,ort,telefon, email);
	 this.Branche = branche;
	 }
	 public String toString() {
		 return "Kunden [id: "+ getId() + ", vorname: " + getVorname() + "nachname: " + getNachname() + ", Straße: "+ getStraße() + ", Plz: " + getOrt() + ", Telefon :" + getTelefon() + " Email: " + getEmail() + "Branche:"+ getBranche()+"]";
	 }
	private String getBranche() {
		// TODO Auto-generated method stub
		return null;
	}
}
