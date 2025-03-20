package Database;

import Externe.Externe;
import Interne.Interne;
import Kunden.Kunden;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class DatabaseDAO {

    private static final String URL = "jdbc:sqlite:shareverwaltung.db";

    public DatabaseDAO() {
        ensureTableExists();
    }

    private Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            conn.setAutoCommit(true); // Garante que cada operação seja confirmada automaticamente
            System.out.println("📂 Banco localizado em: " + new File("shareverwaltung.db").getAbsolutePath());
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar ao banco de dados: " + e.getMessage() +
                    " / Fehler beim Verbinden mit der Datenbank: " + e.getMessage());
            return null;
        }
    }


    public void ensureTableExists() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS InfoGeneral (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        Vorname TEXT NOT NULL,
                        Nachname TEXT NOT NULL,
                        Strasse TEXT,
                        PLZ INTEGER,
                        Ort TEXT,
                        Telefon TEXT,
                        Email TEXT NOT NULL UNIQUE
                    );
                
                    CREATE TABLE IF NOT EXISTS Interne (
                        id INTEGER PRIMARY KEY,
                        FOREIGN KEY (id) REFERENCES InfoGeneral(id) ON DELETE CASCADE
                    );
                
                    CREATE TABLE IF NOT EXISTS Externe (
                        id INTEGER PRIMARY KEY,
                        Firma TEXT NOT NULL,
                        FOREIGN KEY (id) REFERENCES InfoGeneral(id) ON DELETE CASCADE
                    );
                
                    CREATE TABLE IF NOT EXISTS Kunden (
                        id INTEGER PRIMARY KEY,
                        Branche TEXT NOT NULL,
                        FOREIGN KEY (id) REFERENCES InfoGeneral(id) ON DELETE CASCADE
                    );
                """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Banco de dados e tabelas verificadas/criadas com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar/verificar tabelas: " + e.getMessage());
        }
    }



    // 📌 MÉTODO PARA VERIFICAR EXISTÊNCIA DE REGISTROS
    public boolean pessoaExiste(String email) {
        String sql = "SELECT COUNT(*) FROM InfoGeneral WHERE Email = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar existência da pessoa: " + e.getMessage());
            return false;
        }
    }


    // 📌 MÉTODO PARA VERIFICAR SE O E-MAIL JÁ EXISTE NO BANCO
    private boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM InfoGeneral WHERE Email = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // O e-mail já existe
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar e-mail: " + e.getMessage());
        }
        return false;
    }


    // 📌 MÉTODO PARA INSERIR UMA NOVA PESSOA NO BANCO
    public boolean inserirPessoa(String vorname, String nachname, String strasse, int plz, String ort, String telefon, String email, String tipo, String extra1, String extra2) {
        // Verifica se o e-mail já está cadastrado antes de inserir
        if (emailExiste(email)) {
            System.err.println("❌ E-mail já cadastrado: " + email);
            return false; // Bloqueia a inserção se o e-mail já existir
        }

        String sqlInfoGeneral = "INSERT INTO InfoGeneral (Vorname, Nachname, Strasse, PLZ, Ort, Telefon, Email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Define qual a tabela específica onde será inserido o ID gerado
        String sqlSubclasse = switch (tipo.toLowerCase()) {
            case "interne" -> "INSERT INTO Interne (id) VALUES (?)";
            case "externe" -> "INSERT INTO Externe (id, Firma) VALUES (?, ?)";
            case "kunden" -> "INSERT INTO Kunden (id, Branche) VALUES (?, ?)";
            default -> null;
        };

        try (Connection conn = conectar();
             PreparedStatement pstmt1 = conn.prepareStatement(sqlInfoGeneral, Statement.RETURN_GENERATED_KEYS)) {

            // Inserção na tabela InfoGeneral
            pstmt1.setString(1, vorname);
            pstmt1.setString(2, nachname);
            pstmt1.setString(3, strasse);
            pstmt1.setInt(4, plz);
            pstmt1.setString(5, ort);
            pstmt1.setString(6, telefon);
            pstmt1.setString(7, email);

            int affectedRows = pstmt1.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt1.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);

                    // Inserção na tabela específica (Interne, Externe ou Kunden)
                    if (sqlSubclasse != null) {
                        try (PreparedStatement pstmt2 = conn.prepareStatement(sqlSubclasse)) {
                            pstmt2.setInt(1, idGerado);
                            if (!tipo.equalsIgnoreCase("interne")) { // Somente Externe e Kunden possuem um campo extra
                                pstmt2.setString(2, extra1); // Firma para Externe, Branche para Kunden
                            }
                            pstmt2.executeUpdate();
                        }
                    }

                    System.out.println("✅ Pessoa inserida com ID: " + idGerado + " como " + tipo);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir pessoa: " + e.getMessage());
        }
        return false;
    }

    // 📌 MÉTODO PARA INSERIR DADOS NAS SUBCLASSES (Interne, Externe, Kunden)
    private void inserirSubclasse(int id, String categoria, String firma, String branche) {
        System.out.println("🔍 Chamando inserirSubclasse() para categoria: " + categoria + ", ID: " + id);

        String sql = switch (categoria.toLowerCase()) {
            case "interne" -> "INSERT INTO Interne (id) VALUES (?)";
            case "externe" -> "INSERT INTO Externe (id, Firma) VALUES (?, ?)";
            case "kunden" -> "INSERT INTO Kunden (id, Branche) VALUES (?, ?)";
            default -> null;
        };

        if (sql == null) {
            System.err.println("❌ Categoria inválida: " + categoria);
            return;
        }

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            if (categoria.equalsIgnoreCase("externe")) {
                pstmt.setString(2, firma);
            } else if (categoria.equalsIgnoreCase("kunden")) {
                pstmt.setString(2, branche);
            }

            pstmt.executeUpdate();
            System.out.println("✅ " + categoria + " inserido com sucesso! ID: " + id);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir na categoria " + categoria + ": " + e.getMessage());
        }
    }



    public int inserirPessoaComRetornoId(String vorname, String nachname, String strasse, int plz, String ort, String telefon, String email, String tipo, String firma, String extra) {
        String sqlInfoGeneral = "INSERT INTO InfoGeneral (Vorname, Nachname, Strasse, PLZ, Ort, Telefon, Email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sqlInfoGeneral, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, vorname);
            pstmt.setString(2, nachname);
            pstmt.setString(3, strasse);
            pstmt.setInt(4, plz);
            pstmt.setString(5, ort);
            pstmt.setString(6, telefon);
            pstmt.setString(7, email);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idGerado = rs.getInt(1);

                    // Se for externo, insere na tabela correta
                    if (tipo.equals("Externe")) {
                        String sqlExterne = "INSERT INTO Externe (id, Firma) VALUES (?, ?)";
                        try (PreparedStatement pstmtExterne = conn.prepareStatement(sqlExterne)) {
                            pstmtExterne.setInt(1, idGerado);
                            pstmtExterne.setString(2, firma);
                            pstmtExterne.executeUpdate();
                        }
                    }

                    return idGerado; // Retorna o ID gerado para associar ao tipo correto
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir pessoa: " + e.getMessage());
        }
        return -1;
    }





    // 📌 MÉTODO PARA BUSCAR REGISTROS
    public List<String> buscarTodosRegistros() {
        List<String> pessoas = new ArrayList<>();
        String sql = "SELECT Vorname, Nachname, Email FROM InfoGeneral";
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pessoas.add(rs.getString("Vorname") + " " + rs.getString("Nachname") + " - " + rs.getString("Email"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar registros: " + e.getMessage());
        }
        return pessoas;
    }

    // 📌 MÉTODO PARA ATUALIZAR REGISTRO
    public boolean atualizarRegistro(int id, String vorname, String nachname, String strasse, int plz, String ort, String telefon, String email, String campoExtra1, String campoExtra2) {
        String sql = "UPDATE InfoGeneral SET Vorname = ?, Nachname = ?, Strasse = ?, PLZ = ?, Ort = ?, Telefon = ?, Email = ? WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vorname);
            pstmt.setString(2, nachname);
            pstmt.setString(3, strasse);
            pstmt.setInt(4, plz);
            pstmt.setString(5, ort);
            pstmt.setString(6, telefon);
            pstmt.setString(7, email);
            pstmt.setInt(8, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✅ Registro atualizado com sucesso no banco de dados!");
                return true;
            } else {
                System.out.println("⚠ Nenhum registro foi atualizado! Verifique o ID.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar o interno: " + e.getMessage());
            return false;
        }
    }


    // 📌 MÉTODO PARA ATUALIZAR SUBCLASSE
    private void atualizarSubclasse(int id, String novaFirma, String novaBranche) {
        String sql = novaFirma != null ? "UPDATE Externe SET Firma=? WHERE id=?" :
                novaBranche != null ? "UPDATE Kunden SET Branche=? WHERE id=?" : null;

        if (sql == null) return;

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novaFirma != null ? novaFirma : novaBranche);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar subclasse: " + e.getMessage());
        }
    }

    // 📌 MÉTODOS PARA OBTENÇÃO DE DADOS
    public List<Externe> obterExternos() {
        List<Externe> listaExternos = new ArrayList<>();
        String sql = "SELECT InfoGeneral.id, Vorname, Nachname, Strasse, PLZ, Ort, Telefon, Email, Firma " +
                "FROM InfoGeneral " +
                "INNER JOIN Externe ON InfoGeneral.id = Externe.id";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                listaExternos.add(new Externe(
                        rs.getInt("id"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname"),
                        rs.getString("Strasse"),
                        rs.getInt("PLZ"),
                        rs.getString("Ort"),
                        rs.getString("Telefon"),
                        rs.getString("Email"),
                        rs.getString("Firma")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao obter lista de Externos: " + e.getMessage());
        }

        return listaExternos;
    }

    public List<Interne> obterInternos() {
        List<Interne> listaInternos = new ArrayList<>();
        String sql = "SELECT InfoGeneral.id, Vorname, Nachname, Strasse, PLZ, Ort, Telefon, Email " +
                "FROM InfoGeneral " +
                "INNER JOIN Interne ON InfoGeneral.id = Interne.id";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                listaInternos.add(new Interne(
                        rs.getInt("id"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname"),
                        rs.getString("Strasse"),
                        rs.getInt("PLZ"),
                        rs.getString("Ort"),
                        rs.getString("Telefon"),
                        rs.getString("Email")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao obter lista de Internos: " + e.getMessage());
        }

        return listaInternos;
    }

    public List<Kunden> obterClientes() {
        List<Kunden> listaClientes = new ArrayList<>();
        String sql = "SELECT InfoGeneral.id, Vorname, Nachname, Strasse, PLZ, Ort, Telefon, Email, Branche " +
                "FROM InfoGeneral " +
                "INNER JOIN Kunden ON InfoGeneral.id = Kunden.id";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                listaClientes.add(new Kunden(
                        rs.getInt("id"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname"),
                        rs.getString("Strasse"),
                        rs.getInt("PLZ"),
                        rs.getString("Ort"),
                        rs.getString("Telefon"),
                        rs.getString("Email"),
                        rs.getString("Branche")
                ));
            }
            System.out.println("🔍 Clientes carregados do banco: " + listaClientes.size());
        } catch (SQLException e) {
            System.err.println("❌ Erro ao obter lista de Clientes: " + e.getMessage());
        }

        return listaClientes;
    }



    // 📌 MÉTODO PARA BUSCAR ID POR EMAIL
    public Integer buscarIdPorEmail(String email) {
        String sql = "SELECT id FROM InfoGeneral WHERE Email = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar ID pelo email: " + e.getMessage());
        }
        return null;
    }

    // 📌 MÉTODO PARA EXCLUIR PESSOA PELO ID
    public boolean excluirPessoa(int id) {
        String sqlExterne = "DELETE FROM Externe WHERE id = ?";
        String sqlInfoGeneral = "DELETE FROM InfoGeneral WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmtExterne = conn.prepareStatement(sqlExterne);
             PreparedStatement pstmtInfoGeneral = conn.prepareStatement(sqlInfoGeneral)) {

            pstmtExterne.setInt(1, id);
            pstmtInfoGeneral.setInt(1, id);

            pstmtExterne.executeUpdate();
            int linhasAfetadasInfoGeneral = pstmtInfoGeneral.executeUpdate();

            return linhasAfetadasInfoGeneral > 0;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao excluir pessoa: " + e.getMessage());
            return false;
        }
    }

}



