package InfoGeneral;

public class InfoGeneral {
    private int id;
    private String vorname;
    private String nachname;
    private String straße;
    private int plz;
    private String ort;
    private String telefon;
    private String email;

    //  Construtor 
    public InfoGeneral(int id, String vorname, String nachname, String straße, int plz, String ort, String telefon, String email) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.straße = straße;
        this.plz = plz;
        this.ort = ort;
        this.telefon = telefon;
        this.email = email;
    }

    // Métodos Getters
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

    public int getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEmail() {
        return email;
    }

    // Método toString() para exibir os dados corretamente
    @Override
    public String toString() {
        return "InfoGeneral [id=" + id + ", vorname=" + vorname + ", nachname=" + nachname +
               ", straße=" + straße + ", plz=" + plz + ", ort=" + ort +
               ", telefon=" + telefon + ", email=" + email + "]";
    }
}
