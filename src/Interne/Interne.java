package Interne;
import InfoGeneral.InfoGeneral;

public class Interne extends InfoGeneral {
	public Interne( int id, String vorname, String nachname, String straße,String plz,String ort, double telefon, String email) {
	super (id, vorname, nachname, straße, plz,ort,telefon, email);
	}
	
	
	public String toString() {
		return "InfoGeneral  [id: \"+ getId() + \", vorname: " + getVorname() + "nachname: " + getNachname() + ", Straße: "+ getStraße() + ", Plz: " + getOrt() + ", Telefon :" + getTelefon() + " Email: " + getEmail() +"]";
		}

}
