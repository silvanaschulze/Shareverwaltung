package GUI;

import Database.DatabaseDAO;
import Interne.Interne;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.w3c.dom.*;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javafx.application.Platform;


import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


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
import java.util.ArrayList;



public class InternenController {


    @FXML
    private Label lblStatus;


    @FXML
    private TableView<Interne> tabelaInternos;

    @FXML
    private TableColumn<Interne, Integer> colunaID;

    @FXML
    private TableColumn<Interne, String> colunaNome, colunaSobrenome, colunaEmail, colunaStrasse, colunaPLZ, colunaOrt, colunaTelefon;

    @FXML
    private TextField inputNome, inputSobrenome, inputEmail, inputStrasse, inputPLZ, inputOrt, inputTelefon;

    @FXML
    private Button btnAnterior, btnProximo, btnAdicionar, btnEditar, btnExcluir, btnLoadXML;


    private final DatabaseDAO databaseDAO = new DatabaseDAO();



    @FXML
    public void initialize() {
        // Configuração das colunas da tabela
        System.out.println(lblStatus);

        colunaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        colunaSobrenome.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaStrasse.setCellValueFactory(new PropertyValueFactory<>("strasse"));
        colunaPLZ.setCellValueFactory(new PropertyValueFactory<>("plz"));
        colunaOrt.setCellValueFactory(new PropertyValueFactory<>("ort"));
        colunaTelefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));

        tabelaInternos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> preencherCampos());

        carregarInternos();
        Platform.runLater(() -> {
            Scene scene = tabelaInternos.getScene();
            if (scene != null) {
                String css = getClass().getResource("style.css").toExternalForm();
                scene.getStylesheets().add(css);
            } else {
                System.out.println("⚠️ Cena não encontrada! Verifique se a tabela está corretamente carregada.");
            }
        });
    }


    private void carregarInternos() {
        List<Interne> lista = databaseDAO.obterInternos();
        ObservableList<Interne> observableList = FXCollections.observableArrayList(lista);
        tabelaInternos.setItems(observableList);
    }

    @FXML
    private void preencherCampos() {
        Interne selecionado = tabelaInternos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            inputNome.setText(selecionado.getVorname());
            inputSobrenome.setText(selecionado.getNachname());
            inputEmail.setText(selecionado.getEmail());
            inputStrasse.setText(selecionado.getStrasse());
            inputPLZ.setText(String.valueOf(selecionado.getPlz()));
            inputOrt.setText(selecionado.getOrt());
            inputTelefon.setText(selecionado.getTelefon());
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
    private void adicionarInterne(ActionEvent event) {
        String vorname = inputNome.getText().trim();
        String nachname = inputSobrenome.getText().trim();
        String email = inputEmail.getText().trim();
        String strasse = inputStrasse.getText().trim();
        int plz = inputPLZ.getText().isEmpty() ? 0 : Integer.parseInt(inputPLZ.getText().trim());
        String ort = inputOrt.getText().trim();
        String telefon = inputTelefon.getText().trim();



        if (vorname.isEmpty() || nachname.isEmpty() || email.isEmpty()) {
            mostrarAlerta("Erro / Fehler", "Nome, sobrenome e email são obrigatórios!", Alert.AlertType.ERROR);
            return;
        }

        boolean inserido = databaseDAO.inserirPessoa(vorname, nachname, strasse, plz, ort, telefon, email, "Interne", "", "");

        if (inserido) {
            carregarInternos();
            limparCampos();
            mostrarAlerta("Sucesso / Erfolg", "Interno adicionado com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao adicionar interno!", Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void editarInterne(ActionEvent event) {
        Interne selecionado = tabelaInternos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Aviso / Warnung", "Selecione um interno para editar!", Alert.AlertType.WARNING);
            return;
        }

        String vorname = inputNome.getText().trim();
        String nachname = inputSobrenome.getText().trim();
        String email = inputEmail.getText().trim();
        String strasse = inputStrasse.getText().trim();
        int plz = inputPLZ.getText().isEmpty() ? 0 : Integer.parseInt(inputPLZ.getText().trim());
        String ort = inputOrt.getText().trim();
        String telefon = inputTelefon.getText().trim();

        boolean atualizado = databaseDAO.atualizarRegistro(selecionado.getId(), vorname, nachname, strasse, plz, ort, telefon, email, "", "");

        if (atualizado) {
            carregarInternos();
            limparCampos();
            mostrarAlerta("Sucesso / Erfolg", "Interno atualizado com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao atualizar interno!", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void excluirInterne(ActionEvent event) {
        Interne selecionado = tabelaInternos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Aviso / Warnung", "Selecione um interno para excluir!", Alert.AlertType.WARNING);
            return;
        }

        boolean removido = databaseDAO.excluirPessoa(selecionado.getId());
        if (removido) {
            carregarInternos();
            mostrarAlerta("Sucesso / Erfolg", "Interno removido com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Erro / Fehler", "Erro ao remover interno!", Alert.AlertType.ERROR);
        }
    }

    private void limparCampos() {
        inputNome.clear();
        inputSobrenome.clear();
        inputEmail.clear();
    }
    @FXML
    private void loadXML(ActionEvent event) {
        File file = new File("xml/Interne.xml"); // Caminho fixo dentro do projeto
        if (file.exists()) {
            parseXML(file);
        } else {
            System.out.println("❌ Arquivo XML não encontrado no diretório do projeto!");
            mostrarAlerta("Erro / Fehler", "Arquivo XML não encontrado!", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void exportarXML(ActionEvent event) {
        List<Interne> lista = new ArrayList<>(tabelaInternos.getItems());
        XMLExporter.exportToXML(lista, "Internos", "exported_internos.xml");

        if (lblStatus != null) {
            lblStatus.setText("Exportação concluída. Arquivo salvo como exported_internos.xml");
        } else {
            System.out.println("⚠️ lblStatus é null! Verifique se está corretamente vinculado no FXML.");
        }
    }


    // 🔹 NOVA FUNCIONALIDADE: Carregar XML e exibir na Tabela


    private void parseXML(File file) {
        ObservableList<Interne> data = FXCollections.observableArrayList();
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

                    String idStr = element.getElementsByTagName("ID").item(0) != null ? element.getElementsByTagName("ID").item(0).getTextContent().trim() : "0";
                    int id = idStr.isEmpty() ? 0 : Integer.parseInt(idStr);

                    String vorname = element.getElementsByTagName("Vorname").item(0) != null ? element.getElementsByTagName("Vorname").item(0).getTextContent().trim() : "";
                    String nachname = element.getElementsByTagName("Nachname").item(0) != null ? element.getElementsByTagName("Nachname").item(0).getTextContent().trim() : "";
                    String strasse = element.getElementsByTagName("Strasse").item(0) != null ? element.getElementsByTagName("Strasse").item(0).getTextContent().trim() : "";

                    String plzStr = element.getElementsByTagName("PLZ").item(0) != null ? element.getElementsByTagName("PLZ").item(0).getTextContent().trim() : "0";
                    int plz = plzStr.isEmpty() ? 0 : Integer.parseInt(plzStr);

                    String ort = element.getElementsByTagName("Ort").item(0) != null ? element.getElementsByTagName("Ort").item(0).getTextContent().trim() : "";
                    String telefon = element.getElementsByTagName("Telefon").item(0) != null ? element.getElementsByTagName("Telefon").item(0).getTextContent().trim() : "";
                    String email = element.getElementsByTagName("Email").item(0) != null ? element.getElementsByTagName("Email").item(0).getTextContent().trim() : "";

                    data.add(new Interne(id, vorname, nachname, strasse, plz, ort, telefon, email));
                }
            }
            tabelaInternos.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro / Fehler", "Falha ao carregar XML!", Alert.AlertType.ERROR);
        }
    }



    @FXML
    private void registroAnterior(ActionEvent event) {
        trocarCena("/resources/fxml/MainView.fxml", "Página Inicial");
    }

    @FXML
    private void registroProximo(ActionEvent event) {
        trocarCena("/resources/fxml/KundenView.fxml", "Gerenciar Clientes / Kunden verwalten");
    }
    private void trocarCena(String caminhoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();

            Stage stage = (Stage) tabelaInternos.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela: " + titulo, Alert.AlertType.ERROR);
        }
    }
}
