// Kunden.java
package Kunden;

import InfoGeneral.InfoGeneral;

public class Kunden extends InfoGeneral {
    private String branche; // Setor da empresa (Branche em alemão)

    // Construtor
    public Kunden(int id, String vorname, String nachname, String strasse, int plz, String ort, String telefon, String email, String branche) {
        super(id, vorname, nachname, strasse, plz, ort, telefon, email);
        this.branche = branche;
    }

    // Getter para Branche
    public String getBranche() {
        return branche;
    }

    // Setter para Branche
    public void setBranche(String branche) {
        this.branche = branche;
    }

    // ✅ Método toString() atualizado para exibição clara dos dados (Português e Alemão)
    @Override
    public String toString() {
        return "Kunden {ID=" + getId() +
                ", Vorname (Nome)='" + getVorname() + '\'' +
                ", Nachname (Sobrenome)='" + getNachname() + '\'' +
                ", Strasse (Rua)='" + getStrasse() + '\'' +
                ", PLZ (CEP)=" + getPlz() +
                ", Ort (Cidade)='" + getOrt() + '\'' +
                ", Telefon (Telefone)='" + getTelefon() + '\'' +
                ", E-Mail='" + getEmail() + '\'' +
                ", Branche (Setor)='" + branche + '\'' +
                '}';
    }
}
