package DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static Database.DatabaseConfig.URL;

public class DatabaseManager {

    public DatabaseManager() {
        criarBancoDeDados();
    }

    private Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar ao banco / Fehler beim Verbinden mit der Datenbank: " + e.getMessage());
            return null;
        }
    }

    public static void criarBancoDeDados() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String sql = """
                PRAGMA foreign_keys = ON;

                CREATE TABLE IF NOT EXISTS InfoGeneral (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    vorname TEXT NOT NULL,
                    nachname TEXT NOT NULL,
                    strasse TEXT,
                    plz INTEGER,
                    ort TEXT,
                    telefon TEXT,
                    email TEXT UNIQUE NOT NULL
                );

                CREATE TABLE IF NOT EXISTS Interne (
                    id INTEGER PRIMARY KEY,
                    abteilung TEXT NOT NULL,
                    FOREIGN KEY (id) REFERENCES InfoGeneral(id) ON DELETE CASCADE
                );

                CREATE TABLE IF NOT EXISTS Externe (
                    id INTEGER PRIMARY KEY,
                    firma TEXT NOT NULL,
                    FOREIGN KEY (id) REFERENCES InfoGeneral(id) ON DELETE CASCADE
                );

                CREATE TABLE IF NOT EXISTS Kunden (
                    id INTEGER PRIMARY KEY,
                    branche TEXT NOT NULL,
                    FOREIGN KEY (id) REFERENCES InfoGeneral(id) ON DELETE CASCADE
                );
            """;

            stmt.executeUpdate(sql);
            System.out.println("✅ Banco de dados e tabelas criados com sucesso! / Datenbank und Tabellen erfolgreich erstellt!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar banco de dados / Fehler beim Erstellen der Datenbank: " + e.getMessage());
        }
    }

    public boolean inserirInfoGeneral(String vorname, String nachname, String strasse, int plz, String ort, String telefon, String email, String categoria, String firma, String branche) {
        if (pessoaExiste(email)) {
            System.out.println("⚠️ Pessoa já cadastrada: " + email + " / Person bereits registriert: " + email);
            return false;
        }
        if (vorname.isEmpty() || nachname.isEmpty() || email.isEmpty()) {
            System.out.println("⚠️ Campos obrigatórios não podem estar vazios. / Pflichtfelder dürfen nicht leer sein.");
            return false;
        }
        String sql = "INSERT INTO InfoGeneral (Vorname, Nachname, Strasse, PLZ, Ort, Telefon, Email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, vorname);
            pstmt.setString(2, nachname);
            pstmt.setString(3, strasse);
            pstmt.setInt(4, plz);
            pstmt.setString(5, ort);
            pstmt.setString(6, telefon);
            pstmt.setString(7, email);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("✅ Registro inserido com sucesso: " + vorname + " " + nachname +
                        " / Eintrag erfolgreich hinzugefügt: " + vorname + " " + nachname);
                inserirSubclasse(idGerado, categoria, firma, branche);
            }
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir registro / Fehler beim Einfügen des Eintrags: " + e.getMessage());
            return false;
        }
    }

    private boolean pessoaExiste(String email) {
        String sql = "SELECT COUNT(*) FROM InfoGeneral WHERE Email = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar existência da pessoa / Fehler beim Überprüfen der Person: " + e.getMessage());
            return false;
        }
    }

    private void inserirSubclasse(int id, String categoria, String firma, String branche) {
        String sql;
        if ("Externe".equalsIgnoreCase(categoria) && firma != null && !firma.isEmpty()) {
            sql = "INSERT INTO Externe (id, firma) VALUES (?, ?)";
        } else if ("Kunden".equalsIgnoreCase(categoria) && branche != null && !branche.isEmpty()) {
            sql = "INSERT INTO Kunden (id, branche) VALUES (?, ?)";
        } else if ("Interne".equalsIgnoreCase(categoria)) {
            sql = "INSERT INTO Interne (id, abteilung) VALUES (?, ?)";
        } else {
            System.out.println("⚠️ Categoria inválida: " + categoria +
                    " / Ungültige Kategorie: " + categoria);
            return;
        }

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, (firma != null) ? firma : (branche != null) ? branche : "Allgemein");
            pstmt.executeUpdate();
            System.out.println("✅ Inserido na categoria: " + categoria +
                    " / In Kategorie eingefügt: " + categoria);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir na subclasse / Fehler beim Einfügen in die Unterklasse: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        criarBancoDeDados();
    }
}
