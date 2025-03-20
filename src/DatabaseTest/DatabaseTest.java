// DatabaseTest.java
package DatabaseTest;

import Database.DatabaseDAO;
import Database.DatabaseConfig;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {
    private static DatabaseDAO db;

    @BeforeAll
    static void setup() {
        System.out.println("🔍 Inicializando testes do banco de dados... / Datenbanktests werden gestartet...");
        db = new DatabaseDAO();
        DatabaseConfig.inicializarBanco();
    }

    @Test
    void testConexao() {
        System.out.println("🟢 Testando conexão com o banco de dados... / Überprüfung der Datenbankverbindung...");
        try (Connection conn = DatabaseConfig.conectar()) {
            assertNotNull(conn, "✅ Conexão bem-sucedida! / Erfolgreiche Verbindung!");
        } catch (SQLException e) {
            fail("❌ Erro ao conectar ao banco / Fehler beim Verbinden mit der Datenbank: " + e.getMessage());
        }
    }

    @Test
    void testInsercaoValida() {
        System.out.println("🟢 Testando inserção de registros válidos... / Überprüfung gültiger Einträge...");
        boolean resultado = db.inserirPessoa("Maria", "Silva", "Rua A", 12345, "Cidade", "123456789", "maria@email.com", "Interne", "", "");
        assertTrue(resultado, "✅ Registro inserido com sucesso! / Eintrag erfolgreich eingefügt!");
    }

    @Test
    void testInsercaoComValoresNulos() {
        System.out.println("🟢 Testando inserção inválida com valores nulos... / Überprüfung der ungültigen Einfügung mit Nullwerten...");
        boolean resultado = db.inserirPessoa("", "", "", 0, "", "", "", "Interne", "", "");
        assertFalse(resultado, "✅ O banco rejeitou corretamente um nome nulo! / Die Datenbank hat einen Nullnamen korrekt abgelehnt!");
    }

    @Test
    void testInsercaoDuplicada() {
        System.out.println("🟢 Testando inserção de registros duplicados... / Überprüfung doppelter Einträge...");

        db.inserirPessoa("Pedro", "Santos", "Rua B", 54321, "Cidade", "987654321", "pedro@email.com", "Kunden", "", "Tecnologia");

        boolean resultado = db.inserirPessoa("Pedro", "Santos", "Rua B", 54321, "Cidade", "987654321", "pedro@email.com", "Kunden", "", "Tecnologia");
        assertFalse(resultado, "✅ O banco corretamente impediu a inserção duplicada! / Die Datenbank hat die doppelte Einfügung korrekt verhindert!");
    }

    @Test
    void testListagemDeRegistros() {
        System.out.println("🟢 Testando listagem de registros... / Überprüfung der Eintragsliste...");
        List<String> registros = db.buscarTodosRegistros();
        assertNotNull(registros, "❌ Erro: Listagem de registros falhou! / Fehler bei der Auflistung der Einträge!");
        System.out.println("🔍 Registros encontrados: " + registros.size());
        assertFalse(registros.isEmpty(), "❌ Erro: Nenhum registro encontrado! / Fehler: Keine Einträge gefunden!");
    }

    @Test
    void testAtualizacaoDeRegistro() {
        System.out.println("🟢 Testando atualização de registros... / Überprüfung der Aktualisierung von Einträgen...");

        // Primeiro, insere um novo registro
        boolean inserido = db.inserirPessoa("Lucas", "Martins", "Rua C", 67890, "Cidade", "111222333", "lucas@email.com", "Externe", "Empresa X", "");
        assertTrue(inserido, "❌ Erro: Falha ao inserir o registro antes de atualizar! / Fehler: Einfügen des Eintrags vor der Aktualisierung fehlgeschlagen!");

        // Agora busca o ID do Lucas pelo email
        Integer id = db.buscarIdPorEmail("lucas@email.com");

        if (id != null) {
            boolean atualizado = db.atualizarRegistro(
                    id,
                    "Lucas",
                    "Martins",
                    "Rua D",
                    67891,
                    "Cidade Nova",
                    "111222334",
                    "lucas@email.com",
                    "NovaFirma",  // Informação extra para Externe
                    "NovaBranche" // Informação extra para Kunden
            );

            assertTrue(atualizado, "✅ Registro atualizado com sucesso! / Eintrag erfolgreich aktualisiert!");
        } else {
            fail("❌ Erro: ID não encontrado para atualização! / Fehler: ID für die Aktualisierung nicht gefunden!");
        }
    }


    @Test
    void testExclusaoDeRegistro() {
        System.out.println("🟢 Testando exclusão de registros... / Überprüfung der Löschung von Einträgen...");

        // Primeiro, inserir um registro para depois excluir
        db.inserirPessoa("Ana", "Ferreira", "Rua E", 11223, "Cidade", "444555666", "ana@email.com", "Kunden", "", "Saude");

        // Buscar o ID da pessoa inserida
        Integer id = db.buscarIdPorEmail("ana@email.com");

        if (id != null) {
            boolean exclusao = db.excluirPessoa(id);
            if (exclusao) {
                System.out.println("✅ Registro excluído com sucesso! / Eintrag erfolgreich gelöscht!");
            } else {
                System.out.println("❌ ERRO: Falha ao excluir registro! / Fehler beim Löschen des Eintrags!");
            }
            assertTrue(exclusao);
        } else {
            System.out.println("⚠️ ERRO: ID não encontrado para exclusão! / ID für die Löschung nicht gefunden!");
            fail("❌ Erro no teste: Nenhum ID encontrado para exclusão! / Fehler im Test: Keine ID für die Löschung gefunden!");
        }
    }
}
