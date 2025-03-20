import csv

def gerar_dtd(csv_file, dtd_file, root_element, row_element):
    with open(csv_file, newline='', encoding='utf-8-sig') as csvfile:
        reader = csv.reader(csvfile, delimiter=';')
        headers = next(reader)

        # Limpa os headers (remove espaços, caracteres especiais etc.)
        headers = [header.strip().replace("\ufeff", "").replace(" ", "_") for header in headers]

        with open(dtd_file, "w", encoding="utf-8") as f:
            f.write(f"<!ELEMENT {root_element} ({row_element}*)>\n")
            f.write(f"<!ELEMENT {row_element} ({', '.join(headers)})>\n")

            for header in headers:
                f.write(f"<!ELEMENT {header} (#PCDATA)>\n")

        print(f"✅ DTD gerado: {dtd_file}")

# Exemplo de uso
gerar_dtd("Externe.csv", "xml/Externe.dtd", "persons", "person")
gerar_dtd("Interne.csv", "xml/Interne.dtd", "persons", "person")
gerar_dtd("Kunden.csv", "xml/Kunden.dtd", "persons", "person")
