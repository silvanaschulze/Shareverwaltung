package Externe;
import InfoGeneral.InfoGeneral;

public class Externe extends InfoGeneral {
    private String firma;

    public Externe(int id, String vorname, String nachname, String straße, int plz, String ort, String telefon, String email, String firma) {
        super(id, vorname, nachname, straße, plz, ort, telefon, email);
        this.firma = firma;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    @Override
    public String toString() {
        return "Externe [id=" + getId() + ", vorname=" + getVorname() + 
               ", nachname=" + getNachname() + ", Straße=" + getStraße() + 
               ", Plz=" + getPlz() + ", Ort=" + getOrt() + 
               ", Telefon=" + getTelefon() + ", Email=" + getEmail() + 
               ", Firma=" + getFirma() + "]";
    }
}
