package Externe;
import InfoGeneral.InfoGeneral;



public class Externe extends InfoGeneral {
	 String  firma;
	 
	 public Externe (int id, String vorname, String nachname, String straße,String plz,String ort, double telefon, String email, String firma) {
	 super(id, vorname, nachname, straße, plz,ort,telefon, email);
	this.firma = firma;
	 }
	 public String toString() {
		 return "Externe [id: "+ getId() + ", vorname: " + getVorname() + "nachname: " + getNachname() + ", Straße: "+ getStraße() + ", Plz: " + getOrt() + ", Telefon :" + getTelefon() + " Email: " + getEmail() + "Firma:"+ getFirma()+"]";
	 }
	public String getFirma() {
		// TODO Auto-generated method stub
		return null;
	}
}
