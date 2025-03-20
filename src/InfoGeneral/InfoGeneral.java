// InfoGeneral.java
package InfoGeneral;

public class InfoGeneral {
    private int id;
    private String vorname;
    private String nachname;
    private String strasse;
    private int plz;
    private String ort;
    private String telefon;
    private String email;

    // Construtor completo
    public InfoGeneral(int id, String vorname, String nachname, String strasse, int plz, String ort, String telefon, String email) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.strasse = strasse;
        this.plz = plz;
        this.ort = ort;
        this.telefon = telefon;
        this.email = email;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getStrasse() {
        return strasse;
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

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // ✅ Método toString() para exibição clara dos dados (Português e Alemão)
    @Override
    public String toString() {
        return "InfoGeneral {ID=" + id +
                ", Vorname (Nome)='" + vorname + '\'' +
                ", Nachname (Sobrenome)='" + nachname + '\'' +
                ", Strasse (Rua)='" + strasse + '\'' +
                ", PLZ (CEP)=" + plz +
                ", Ort (Cidade)='" + ort + '\'' +
                ", Telefon (Telefone)='" + telefon + '\'' +
                ", E-Mail='" + email + '\'' +
                '}';
    }

}
