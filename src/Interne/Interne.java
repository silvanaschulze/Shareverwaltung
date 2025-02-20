package Interne;
import InfoGeneral.InfoGeneral;

public class Interne extends InfoGeneral {
    public Interne(int id, String vorname, String nachname, String straße, int plz, String ort, String telefon, String email) {
        super(id, vorname, nachname, straße, plz, ort, telefon, email);
    }

    @Override
    public String toString() {
        return "Interne [id=" + getId() + ", vorname=" + getVorname() + 
               ", nachname=" + getNachname() + ", Straße=" + getStraße() + 
               ", Plz=" + getPlz() + ", Ort=" + getOrt() + 
               ", Telefon=" + getTelefon() + ", Email=" + getEmail() + "]";
    }
}
