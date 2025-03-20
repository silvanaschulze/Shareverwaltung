package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class MainController {

    @FXML
    private void abrirTelaInternos() {
        abrirNovaJanela("/resources/fxml/InterneView.fxml", "Gerenciar Internos / Interne verwalten");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/InterneView.fxml"));
            Parent root = loader.load();

            // Obtendo o controlador e garantindo que ele é inicializado corretamente
            InternenController controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gerenciar Internos | Interne verwalten");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao abrir a tela: Gerenciar Internos / Interne verwalten");
        }


    }

    @FXML
    private void abrirTelaExternos() {
        abrirNovaJanela("/resources/fxml/ExterneView.fxml", "Gerenciar Externos / Externe verwalten");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/ExterneView.fxml"));
            Parent root = loader.load();

            ExterneController controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gerenciar Externos | Externe verwalten");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao abrir a tela: Gerenciar Externos / Externe verwalten");
        }
    }

    @FXML
    private void abrirTelaKunden() {
        abrirNovaJanela("/resources/fxml/KundenView.fxml", "Gerenciar Clientes / Kunden verwalten");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/KundenView.fxml"));
            Parent root = loader.load();

            KundenController controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gerenciar Clientes | Kunden verwalten");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao abrir a tela: Gerenciar Clientes / Kunden verwalten");
        }
    }



    @FXML
    private void abrirTelaConfiguracoes() {
        abrirNovaJanela("/resources/fxml/ConfiguracoesView.fxml", "Configurações / Einstellungen");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/ConfiguracoesView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Configurações | Einstellungen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao abrir a tela: Configurações / Einstellungen");
        }
    }




    @FXML
    private void abrirNovaJanela(String caminhoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao abrir a tela: " + titulo);
        }
    }
}
