// Externe.java
package Externe;

import InfoGeneral.InfoGeneral;

public class Externe extends InfoGeneral {
    private String firma; // Nome da empresa (Firma em alemão)

    // Construtor
    public Externe(int id, String vorname, String nachname, String strasse, int plz, String ort, String telefon, String email, String firma) {
        super(id, vorname, nachname, strasse, plz, ort, telefon, email);
        this.firma = firma;
    }

    // Getter para Firma
    public String getFirma() {
        return firma;
    }

    // Setter para Firma
    public void setFirma(String firma) {
        this.firma = firma;
    }

    // ✅ Método toString() atualizado para exibição clara dos dados (Português e Alemão)
    @Override
    public String toString() {
        return "Externe {ID=" + getId() +
                ", Vorname (Nome)='" + getVorname() + '\'' +
                ", Nachname (Sobrenome)='" + getNachname() + '\'' +
                ", Strasse (Rua)='" + getStrasse() + '\'' +
                ", PLZ (CEP)=" + getPlz() +
                ", Ort (Cidade)='" + getOrt() + '\'' +
                ", Telefon (Telefone)='" + getTelefon() + '\'' +
                ", E-Mail='" + getEmail() + '\'' +
                ", Firma (Empresa)='" + firma + '\'' +
                '}';
    }
}
