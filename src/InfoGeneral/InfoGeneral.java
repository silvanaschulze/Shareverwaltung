package InfoGeneral;

public class InfoGeneral {
	int id;
	String vorname;
	String nachname;
	String straße;
	String plz;
	String ort;
	double telefon;
	String email;
	
// Construtor
	
	public InfoGeneral (int id, String vorname, String nachname, String straße,String plz,String ort, double telefon, String email) {
		this.id = id;
		this.vorname = vorname;
		this.nachname = nachname;
		this.straße = straße;
		this.plz = plz;
		this.ort = ort;
		this.telefon = telefon;
		this.email = email;
	}
	
	//getters
	
	public int getId() {
		return id;
	}
	
	public String getVorname() {
		return vorname;
	}
	
	public String getNachname() {
		return nachname;
	}
	
	public String getStraße() {
		return straße;
	}
	
	public String getPlz() {
		return plz;
	}
	
	public String getOrt() {
		return ort;
	}
	
	public double getTelefon() {
		return telefon;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String toString() {
		return "InfoGeneral [id: "+ getId() + ", vorname: " + getVorname() + "nachname: " + getNachname() + ", Straße: "+ getStraße() + ", Plz: " + getOrt() + ", Telefon :" + getTelefon() + " Email: " + getEmail() +"]";
		}
}
