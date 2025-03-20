package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

	private static final String DB_PATH = "shareverwaltung.db";
	public static final String URL = "jdbc:sqlite:" + DB_PATH;

	public static Connection conectar() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC"); // Certificar que o driver do SQLite está carregado
		} catch (ClassNotFoundException e) {
			throw new SQLException("❌ Erro: Driver do SQLite não encontrado / Fehler: SQLite-Treiber nicht gefunden.", e);
		}
		return DriverManager.getConnection(URL);
	}

	public static void inicializarBanco() {
		try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
			stmt.execute("CREATE TABLE IF NOT EXISTS InfoGeneral (" +
					"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Vorname TEXT NOT NULL, " +
					"Nachname TEXT NOT NULL, " +
					"Strasse TEXT, " +
					"PLZ INTEGER, " +
					"Ort TEXT, " +
					"Telefon TEXT, " +
					"Email TEXT NOT NULL UNIQUE" +
					");");

			stmt.execute("CREATE TABLE IF NOT EXISTS Externe (" +
					"ID INTEGER PRIMARY KEY, " +
					"Firma TEXT NOT NULL" +
					");");

			stmt.execute("CREATE TABLE IF NOT EXISTS Kunden (" +
					"ID INTEGER PRIMARY KEY, " +
					"Branche TEXT NOT NULL" +
					");");

			stmt.execute("CREATE TABLE IF NOT EXISTS Interne (" +
					"ID INTEGER PRIMARY KEY, " +
					"Abteilung TEXT NOT NULL" +
					");");

			System.out.println("✅ Banco de dados inicializado com sucesso! / Datenbank erfolgreich initialisiert!");
		} catch (SQLException e) {
			System.err.println("❌ Erro ao inicializar o banco de dados / Fehler beim Initialisieren der Datenbank: " + e.getMessage());
		}
	}
}
