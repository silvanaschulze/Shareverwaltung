package GUI;

import Database.DatabaseDAO;
import Interne.Interne;
import Kunden.Kunden;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.File;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.application.Platform;




public class KundenController {

    @FXML
    private TableView<Kunden> tabelaClientes;

    @FXML
    private TableColumn<Kunden, Integer> colunaID;

    @FXML
    private TableColumn<Kunden, String> colunaNome, colunaSobrenome, colunaEmail, colunaStrasse, colunaPLZ, colunaOrt, colunaTelefon, colunaBranche;

    @FXML
    private TextField inputNome, inputSobrenome, inputEmail, inputStrasse, inputPLZ, inputOrt, inputTelefon, inputBranche;

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
        colunaBranche.setCellValueFactory(new PropertyValueFactory<>("branche"));

        tabelaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> preencherCampos());

        carregarClientes();
        Platform.runLater(() -> {
            Scene scene = tabelaClientes.getScene();
            if (scene != null) {
                String css = getClass().getResource("/style/styles.css").toExternalForm();
                scene.getStylesheets().add(css);
            }
        });
    }

    private void carregarClientes() {
        List<Kunden> lista = databaseDAO.obterClientes();
        ObservableList<Kunden> observableList = FXCollections.observableArrayList(lista);
        tabelaClientes.setItems(observableList);
    }

    @FXML
    private void preencherCampos() {
        Kunden selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            inputNome.setText(selecionado.getVorname());
            inputSobrenome.setText(selecionado.getNachname());
            inputEmail.setText(selecionado.getEmail());
            inputStrasse.setText(selecionado.getStrasse());
            inputPLZ.setText(String.valueOf(selecionado.getPlz()));
            inputOrt.setText(selecionado.getOrt());
            inputTelefon.setText(selecionado.getTelefon());
            inputBranche.setText(selecionado.getBranche());
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
    private void adicionarKunden(ActionEvent event) {
        String vorname = inputNome.getText().trim();
        String nachname = inputSobrenome.getText().trim();
        String email = inputEmail.getText().trim();
        String strasse = inputStrasse.getText().trim();
        int plz = inputPLZ.getText().isEmpty() ? 0 : Integer.parseInt(inputPLZ.getText().trim());
        String ort = inputOrt.getText().trim();
        String telefon = inputTelefon.getText().trim();
        String branche = inputBranche.getText().trim();

        if (vorname.isEmpty() || nachname.isEmpty() || email.isEmpty()) {
            mostrarAlerta("Erro / Fehler", "Nome, sobrenome e email são obrigatórios!", Alert.AlertType.ERROR);
            return;
        }

        boolean inserido = databaseDAO.inserirPessoa(vorname, nachname, strasse, plz, ort, telefon, email, "Kunden", branche, "");

        if (inserido) {
            carregarClientes();
            limparCampos();
            mostrarAlerta("Sucesso / Erfolg", "Cliente adicionado com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao adicionar cliente!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void editarKunden(ActionEvent event) {
        Kunden selecionado = tabelaClientes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Aviso / Warnung", "Selecione um cliente para editar!", Alert.AlertType.WARNING);
            return;
        }

        String vorname = inputNome.getText().trim();
        String nachname = inputSobrenome.getText().trim();
        String email = inputEmail.getText().trim();
        String strasse = inputStrasse.getText().trim();
        int plz = inputPLZ.getText().isEmpty() ? 0 : Integer.parseInt(inputPLZ.getText().trim());
        String ort = inputOrt.getText().trim();
        String telefon = inputTelefon.getText().trim();
        String branche = inputBranche.getText().trim();

        boolean atualizado = databaseDAO.atualizarRegistro(selecionado.getId(), vorname, nachname, strasse, plz, ort, telefon, email, "Kunden", branche);

        if (atualizado) {
            carregarClientes();
            limparCampos();
            mostrarAlerta("Sucesso / Erfolg", "Cliente atualizado com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao atualizar cliente!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void excluirKunden(ActionEvent event) {
        Kunden selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Aviso / Warnung", "Selecione um cliente para excluir!", Alert.AlertType.WARNING);
            return;
        }

        boolean removido = databaseDAO.excluirPessoa(selecionado.getId());
        if (removido) {
            carregarClientes();
            mostrarAlerta("Sucesso / Erfolg", "Cliente removido com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao remover cliente!", Alert.AlertType.ERROR);
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

    private void limparCampos() {
        inputNome.clear();
        inputSobrenome.clear();
        inputEmail.clear();
        inputBranche.clear();
    }

    @FXML
    private void loadXML(ActionEvent event) {
        File file = new File("xml/Kunden.xml");
        if (file.exists()) {
            parseXML(file);
        } else {
            System.out.println("❌ Arquivo XML não encontrado no diretório do projeto!");
            mostrarAlerta("Erro / Fehler", "Arquivo XML não encontrado!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void exportarXML(ActionEvent event) {
        System.out.println("Exportação para XML concluída | XML-Export abgeschlossen");
        lblStatus.setText("Exportação para XML concluída | XML-Export abgeschlossen");
    }

    private void parseXML(File file) {
        ObservableList<Kunden> data = FXCollections.observableArrayList();
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
                    String branche = element.getElementsByTagName("Branche").item(0).getTextContent().trim();

                    data.add(new Kunden(id, vorname, nachname, strasse, plz, ort, telefon, email, branche));
                }
            }


            tabelaClientes.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro / Fehler", "Falha ao carregar XML!", Alert.AlertType.ERROR);
        }
    }


    }

