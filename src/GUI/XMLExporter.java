package GUI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XMLExporter {

    public static <T> void exportToXML(List<T> data, String rootElementName, String fileName) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement(rootElementName);
            doc.appendChild(rootElement);

            for (T item : data) {
                Element element = doc.createElement("Registro");
                rootElement.appendChild(element);

                for (var field : item.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Element fieldElement = doc.createElement(field.getName());
                    fieldElement.appendChild(doc.createTextNode(field.get(item).toString()));
                    element.appendChild(fieldElement);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

            transformer.transform(source, result);

            System.out.println("Arquivo XML salvo com sucesso: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
