package Kunden;
import InfoGeneral.InfoGeneral;

public class Kunden extends InfoGeneral {
    private String branche;

    public Kunden(int id, String vorname, String nachname, String straße, int plz, String ort, String telefon, String email, String branche) {
        super(id, vorname, nachname, straße, plz, ort, telefon, email);
        this.branche = branche;
    }

    public String getBranche() {
        return branche;
    }

    public void setBranche(String branche) {
        this.branche = branche;
    }

    @Override
    public String toString() {
        return "Kunden [id=" + getId() + ", vorname=" + getVorname() + 
               ", nachname=" + getNachname() + ", Straße=" + getStraße() + 
               ", Plz=" + getPlz() + ", Ort=" + getOrt() + 
               ", Telefon=" + getTelefon() + ", Email=" + getEmail() + 
               ", Branche=" + getBranche() + "]";
    }
}
