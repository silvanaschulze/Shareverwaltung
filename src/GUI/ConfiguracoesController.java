package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class ConfiguracoesController {

    @FXML
    private ComboBox<String> comboIdioma;

    @FXML
    public void initialize() {
        comboIdioma.getItems().addAll("Português", "Deutsch", "English");
        comboIdioma.setValue("Português");
    }

    @FXML
    private void salvarConfiguracoes() {
        System.out.println("✅ Configuração salva: " + comboIdioma.getValue());
    }
}
