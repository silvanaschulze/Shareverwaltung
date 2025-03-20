package Main;

import InfoGeneral.InfoGeneral;
import Database.DatabaseDAO;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        System.out.println("🔄 Lendo arquivos XML... / XML-Dateien werden geladen...");

        // Caminho dos arquivos XML
        String pathInterne = "xml/Interne.xml";
        String pathExterne = "xml/Externe.xml";
        String pathKunden = "xml/Kunden.xml";

        // Criar instância do DatabaseDAO para inserir no banco
        DatabaseDAO db = new DatabaseDAO();

        // Lendo os arquivos XML
        List<InfoGeneral> interneList = readXML(pathInterne, "Interne");
        List<InfoGeneral> externeList = readXML(pathExterne, "Externe");
        List<InfoGeneral> kundenList = readXML(pathKunden, "Kunden");

        // Inserindo dados no banco de dados
        System.out.println("\n🗄 Salvando dados no banco de dados... / Daten werden in die Datenbank gespeichert...");
        insertDataIntoDatabase(db, interneList, "Interne");
        insertDataIntoDatabase(db, externeList, "Externe");
        insertDataIntoDatabase(db, kundenList, "Kunden");

        // Listando os registros cadastrados
        System.out.println("\n🔍 Pessoas cadastradas no banco: / In der Datenbank gespeicherte Personen:");
        List<String> pessoas = db.buscarTodosRegistros();
        if (!pessoas.isEmpty()) {
            pessoas.forEach(System.out::println);
        } else {
            System.out.println("⚠️ Nenhum registro encontrado! / Keine Einträge gefunden!");
        }

        System.out.println("\n✅ Processo concluído com sucesso! / Prozess erfolgreich abgeschlossen!");
    }

    // Método para ler os arquivos XML e transformar em objetos Java
    public static List<InfoGeneral> readXML(String filePath, String type) {
        List<InfoGeneral> people = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists()) {
                System.err.println("❌ Arquivo XML não encontrado: " + filePath + " / XML-Datei nicht gefunden: " + filePath);
                return people;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(type);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = safeParseInt(getTagValue("id", element));
                    String vorname = getTagValue("vorname", element);
                    String nachname = getTagValue("nachname", element);
                    String strasse = getTagValue("strasse", element);
                    int plz = safeParseInt(getTagValue("plz", element));
                    String ort = getTagValue("ort", element);
                    String telefon = getTagValue("telefon", element);
                    String email = getTagValue("email", element);

                    // 🚀 Exibir valores lidos
                    System.out.println("📄 XML -> ID: " + id + ", Nome: " + vorname + ", Sobrenome: " + nachname + ", E-mail: " + email);
                    System.out.println("📄 XML -> ID: " + id + ", Vorname: " + vorname + ", Nachname: " + nachname + ", E-Mail: " + email);

                    people.add(new InfoGeneral(id, vorname, nachname, strasse, plz, ort, telefon, email));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao ler o XML: " + e.getMessage() + " / Fehler beim Lesen der XML-Datei: " + e.getMessage());
        }
        return people;
    }

    // Método para obter valores de uma tag no XML
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0 && nodeList.item(0).getFirstChild() != null) {
            return nodeList.item(0).getFirstChild().getNodeValue();
        }
        return "0"; // Retorna "0" para evitar NumberFormatException
    }

    // Método auxiliar para converter String em int com segurança
    private static int safeParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Método para inserir os dados do XML no banco
    public static void insertDataIntoDatabase(DatabaseDAO db, List<InfoGeneral> people, String categoria) {
        for (InfoGeneral person : people) {
            boolean inserido = db.inserirPessoa(person.getVorname(), person.getNachname(), person.getStrasse(),
                    person.getPlz(), person.getOrt(), person.getTelefon(), person.getEmail(), categoria, "", "");
            if (inserido) {
                System.out.println("✅ Pessoa inserida no banco! / Person erfolgreich in die Datenbank eingefügt!");
            } else {
                System.out.println("❌ Erro ao inserir pessoa! / Fehler beim Einfügen der Person!");
            }
        }
    }
}
