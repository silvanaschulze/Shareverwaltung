// Interne.java
package Interne;

import InfoGeneral.InfoGeneral;

public class Interne extends InfoGeneral {

    // Construtor
    public Interne(int id, String vorname, String nachname, String strasse, int plz, String ort, String telefon, String email) {
        super(id, vorname, nachname, strasse, plz, ort, telefon, email);
    }

    // ✅ Método toString() atualizado para exibição clara dos dados (Português e Alemão)
    @Override
    public String toString() {
        return "Interne {ID=" + getId() +
                ", Vorname (Nome)='" + getVorname() + '\'' +
                ", Nachname (Sobrenome)='" + getNachname() + '\'' +
                ", Strasse (Rua)='" + getStrasse() + '\'' +
                ", PLZ (CEP)=" + getPlz() +
                ", Ort (Cidade)='" + getOrt() + '\'' +
                ", Telefon (Telefone)='" + getTelefon() + '\'' +
                ", E-Mail='" + getEmail() + '\'' +
                '}';
    }
}
