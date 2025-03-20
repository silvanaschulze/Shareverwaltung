package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseReset {

    public static void resetarBanco() {
        try (Connection conn = DatabaseConfig.conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS InfoGeneral;");
            stmt.execute("DROP TABLE IF EXISTS Externe;");
            stmt.execute("DROP TABLE IF EXISTS Kunden;");
            stmt.execute("DROP TABLE IF EXISTS Interne;");

            System.out.println("♻️ Banco de dados resetado com sucesso! / Datenbank erfolgreich zurückgesetzt!");

            DatabaseConfig.inicializarBanco(); // Recriar as tabelas
        } catch (SQLException e) {
            System.err.println("❌ Erro ao resetar o banco de dados / Fehler beim Zurücksetzen der Datenbank: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        resetarBanco();
    }
}
