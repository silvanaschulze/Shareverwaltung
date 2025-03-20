import csv
import xml.etree.ElementTree as ET
import os

def indent(elem, level=0):
    indent_space = "    "  # 4 espaços
    if len(elem):
        if not elem.text or not elem.text.strip():
            elem.text = indent_space * (level + 1)
        if not elem.tail or not elem.tail.strip():
            elem.tail = indent_space * level
        for elem in elem:
            indent(elem, level+1)
        if not elem.tail or not elem.tail.strip():
            elem.tail = indent_space * level
    elif level and (not elem.tail or not elem.tail.strip()):
        elem.tail = indent_space * level


def csv_to_xml(csv_file, xml_file, root_element, row_element):
    tree = ET.Element(root_element)

    with open(csv_file, newline='', encoding='utf-8-sig') as csvfile:  # Usa encoding 'utf-8-sig' para remover BOM
        reader = csv.reader(csvfile, delimiter=';')  # Ajuste o delimitador se necessário
        headers = next(reader)  # Lê os cabeçalhos

        # Remove espaços extras nos cabeçalhos e caracteres invisíveis
        headers = [header.strip().replace("\ufeff", "") for header in headers]

        for row in reader:
            item = ET.SubElement(tree, row_element) 
            for i, value in enumerate(row):
                tag = headers[i].strip().replace(" ", "_")  # Substitui espaços no nome da tag
                ET.SubElement(item, tag).text = (value.strip() + "\n") if value else "\n" 


    # Garante que a pasta XML existe
    xml_dir = os.path.dirname(xml_file)
    os.makedirs(xml_dir, exist_ok=True)

    # Salva o arquivo XML corretamente formatado
    xml_output_path = os.path.join(xml_dir, os.path.basename(xml_file))
    indent(tree)
    with open(xml_output_path, "wb") as xmlfile:
        tree = ET.ElementTree(tree)
        tree.write(xmlfile, encoding="utf-8", xml_declaration=True)

# Converter cada arquivo CSV para XML
csv_to_xml("Interne.csv", "xml/Interne.xml", "persons", "person")
csv_to_xml("Externe.csv", "xml/Externe.xml", "persons", "person")
csv_to_xml("Kunden.csv", "xml/Kunden.xml", "persons", "person")

print("✅ Conversão concluída! Os arquivos XML foram gerados na pasta 'xml'.")
