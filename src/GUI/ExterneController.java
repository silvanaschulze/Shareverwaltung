package GUI;

import Database.DatabaseDAO;
import Externe.Externe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.w3c.dom.*;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javafx.application.Platform;


public class ExterneController {

    @FXML
    private TableView<Externe> tabelaExternos;

    @FXML
    private TableColumn<Externe, Integer> colunaID;

    @FXML
    private TableColumn<Externe, String> colunaNome, colunaSobrenome, colunaEmail, colunaStrasse, colunaPLZ, colunaOrt, colunaTelefon, colunaFirma;

    @FXML
    private TextField inputNome, inputSobrenome, inputEmail, inputStrasse, inputPLZ, inputOrt, inputTelefon, inputFirma;

    @FXML
    private Button btnAnterior, btnProximo, btnAdicionar, btnEditar, btnExcluir, btnLoadXML;

    @FXML
    private Label lblStatus;

    private final DatabaseDAO databaseDAO = new DatabaseDAO();

    @FXML
    public void initialize() {
        colunaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        colunaSobrenome.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaStrasse.setCellValueFactory(new PropertyValueFactory<>("strasse"));
        colunaPLZ.setCellValueFactory(new PropertyValueFactory<>("plz"));
        colunaOrt.setCellValueFactory(new PropertyValueFactory<>("ort"));
        colunaTelefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        colunaFirma.setCellValueFactory(new PropertyValueFactory<>("firma"));

        tabelaExternos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> preencherCampos());

        carregarExternos();
        Platform.runLater(() -> {
            Scene scene = tabelaExternos.getScene();
            if (scene != null) {
                String css = getClass().getResource("/style/styles.css").toExternalForm();
                scene.getStylesheets().add(css);
            }
        });
    }

    private void carregarExternos() {
        List<Externe> lista = databaseDAO.obterExternos();
        ObservableList<Externe> observableList = FXCollections.observableArrayList(lista);
        tabelaExternos.setItems(observableList);
    }

    @FXML
    private void preencherCampos() {
        Externe selecionado = tabelaExternos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            inputNome.setText(selecionado.getVorname());
            inputSobrenome.setText(selecionado.getNachname());
            inputEmail.setText(selecionado.getEmail());
            inputStrasse.setText(selecionado.getStrasse());
            inputPLZ.setText(String.valueOf(selecionado.getPlz()));
            inputOrt.setText(selecionado.getOrt());
            inputTelefon.setText(selecionado.getTelefon());
            inputFirma.setText(selecionado.getFirma());
        }
    }

    @FXML
    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void adicionarExterne() {
        String vorname = inputNome.getText().trim();
        String nachname = inputSobrenome.getText().trim();
        String email = inputEmail.getText().trim();
        String strasse = inputStrasse.getText().trim();
        int plz = inputPLZ.getText().isEmpty() ? 0 : Integer.parseInt(inputPLZ.getText().trim());
        String ort = inputOrt.getText().trim();
        String telefon = inputTelefon.getText().trim();
        String firma = inputFirma.getText().trim();

        if (vorname.isEmpty() || nachname.isEmpty() || email.isEmpty()) {
            mostrarAlerta("Erro / Fehler", "Nome, sobrenome e email são obrigatórios!", Alert.AlertType.ERROR);
            return;
        }

        boolean inserido = databaseDAO.inserirPessoa(vorname, nachname, strasse, plz, ort, telefon, email, "Externe", firma, "");
        if (inserido) {
            carregarExternos();
            limparCampos();
            mostrarAlerta("Sucesso / Erfolg", "Externo adicionado com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao adicionar externo!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void editarExterne(ActionEvent event) {
        Externe selecionado = tabelaExternos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Aviso / Warnung", "Selecione um externo para editar!", Alert.AlertType.WARNING);
            return;
        }

        String vorname = inputNome.getText().trim();
        String nachname = inputSobrenome.getText().trim();
        String email = inputEmail.getText().trim();
        String strasse = inputStrasse.getText().trim();
        int plz = inputPLZ.getText().isEmpty() ? 0 : Integer.parseInt(inputPLZ.getText().trim());
        String ort = inputOrt.getText().trim();
        String telefon = inputTelefon.getText().trim();
        String firma = inputFirma.getText().trim();

        boolean atualizado = databaseDAO.atualizarRegistro(selecionado.getId(), vorname, nachname, strasse, plz, ort, telefon, email, "Externe", firma);

        if (atualizado) {
            carregarExternos();
            limparCampos();
            mostrarAlerta("Sucesso / Erfolg", "Externo atualizado com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao atualizar externo!", Alert.AlertType.ERROR);
        }


    }
    @FXML
    private void excluirExterne(ActionEvent event) {
        Externe selecionado = tabelaExternos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Aviso / Warnung", "Selecione um externo para excluir!", Alert.AlertType.WARNING);
            return;
        }

        boolean removido = databaseDAO.excluirPessoa(selecionado.getId());
        if (removido) {
            carregarExternos();
            mostrarAlerta("Sucesso / Erfolg", "Externo removido com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao remover externo!", Alert.AlertType.ERROR);
        }
    }

    private void limparCampos() {
        inputNome.clear();
        inputSobrenome.clear();
        inputEmail.clear();
        inputStrasse.clear();
        inputPLZ.clear();
        inputOrt.clear();
        inputTelefon.clear();
        inputFirma.clear();
    }
    @FXML
    private void exportarXML(ActionEvent event) {
        System.out.println("Exportação para XML concluída | XML-Export abgeschlossen");
        lblStatus.setText("Exportação para XML concluída | XML-Export abgeschlossen");
    }

@FXML
private void loadXML() {
    File file = new File("xml/Externe.xml");
    if (file.exists()) {
        parseXML(file);
    } else {
        System.out.println("❌ Arquivo XML não encontrado no diretório do projeto!");
    }


}
    private void parseXML(File file) {
        ObservableList<Externe> data = FXCollections.observableArrayList();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("person");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int id = Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent().trim());
                    String vorname = element.getElementsByTagName("Vorname").item(0).getTextContent().trim();
                    String nachname = element.getElementsByTagName("Nachname").item(0).getTextContent().trim();
                    String strasse = element.getElementsByTagName("Strasse").item(0).getTextContent().trim();
                    int plz = Integer.parseInt(element.getElementsByTagName("PLZ").item(0).getTextContent().trim());
                    String ort = element.getElementsByTagName("Ort").item(0).getTextContent().trim();
                    String telefon = element.getElementsByTagName("Telefon").item(0).getTextContent().trim();
                    String email = element.getElementsByTagName("Email").item(0).getTextContent().trim();
                    String firma = element.getElementsByTagName("Firma").item(0).getTextContent().trim();

                    data.add(new Externe(id, vorname, nachname, strasse, plz, ort, telefon, email, firma));
                }
            }
            tabelaExternos.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro / Fehler", "Falha ao carregar XML!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void registroAnterior(ActionEvent event) {
        lblStatus.setText("Registro anterior carregado | Vorheriger Datensatz geladen");
    }

    @FXML
    private void registroProximo(ActionEvent event) {
        lblStatus.setText("Próximo registro carregado | Nächster Datensatz geladen");
    }
}
