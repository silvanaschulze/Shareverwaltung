package Main;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class SimpleErrorHandler implements ErrorHandler {

	@Override
	public void warning(SAXParseException e) {
		System.out.println("⚠️ Aviso: " + e.getMessage() + " / Warnung: " + e.getMessage());
	}

	@Override
	public void error(SAXParseException e) {
		System.err.println("❌ Erro: " + e.getMessage() + " / Fehler: " + e.getMessage());
	}

	@Override
	public void fatalError(SAXParseException e) {
		System.err.println("🚨 Erro fatal: " + e.getMessage() + " / Schwerwiegender Fehler: " + e.getMessage());
	}
}
