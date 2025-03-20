package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlLocation = getClass().getResource("/resources/fxml/MainView.fxml"); // Verifique o caminho do FXML
            if (fxmlLocation == null) {
                throw new IllegalStateException("Arquivo FXML não encontrado! Verifique o caminho.");
            }

            Parent root = FXMLLoader.load(fxmlLocation);
            primaryStage.setTitle("ShareVerwaltung - Sistema de Gestão / Verwaltungssystem");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();

            System.out.println("✅ Aplicação iniciada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao iniciar a aplicação: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
