package Main;

import InfoGeneral.InfoGeneral;
import Interne.Interne;
import Externe.Externe;
import Kunden.Kunden;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("🔄 Lendo arquivos XML...");

        // Caminho dos arquivos XML
        String pathInterne = "xml/Interne.xml";
        String pathExterne = "xml/Externe.xml";
        String pathKunden = "xml/Kunden.xml";

        // Lendo os arquivos XML
        List<InfoGeneral> interneList = readXML(pathInterne, "interne");
        List<InfoGeneral> externeList = readXML(pathExterne, "externe");
        List<InfoGeneral> kundenList = readXML(pathKunden, "kunden");

        // Exibindo os dados no console
        System.out.println("\n Funcionários Internos:");
        interneList.forEach(System.out::println);

        System.out.println("\n Funcionários Terceirizados:");
        externeList.forEach(System.out::println);

        System.out.println("\n Clientes:");
        kundenList.forEach(System.out::println);
    }

    public static List<InfoGeneral> readXML(String filePath, String type) {
        List<InfoGeneral> people = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists()) {
                System.out.println("❌ Erro: O arquivo XML não foi encontrado! Caminho: " + xmlFile.getAbsolutePath());
                return people;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("person");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int id = Integer.parseInt(getTagValue("id", element));
                    String vorname = getTagValue("vorname", element);
                    String nachname = getTagValue("nachname", element);
                    String straße = getTagValue("straße", element);
                    int plz = Integer.parseInt(getTagValue("plz", element));
                    String ort = getTagValue("ort", element);
                    String telefon = getTagValue("telefon", element);
                    String email = getTagValue("email", element);

                    if (type.equals("interne")) {
                        people.add(new Interne(id, vorname, nachname, straße, plz, ort, telefon, email));
                    } else if (type.equals("externe")) {
                        String firma = getTagValue("firma", element);
                        people.add(new Externe(id, vorname, nachname, straße, plz, ort, telefon, email, firma));
                    } else if (type.equals("kunden")) {
                        String branche = getTagValue("branche", element);
                        people.add(new Kunden(id, vorname, nachname, straße, plz, ort, telefon, email, branche));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return people;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return "";
    }
}
