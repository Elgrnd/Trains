package fr.umontpellier.iut.trainsJavaFX.vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class OuvrirNouvFenetreHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        Stage nouvFenetre = new Stage();
        nouvFenetre.setTitle("Réserve");
        BorderPane layoutReserve = new BorderPane();
        Scene sceneReserve = new Scene(layoutReserve, 800, 500);

        nouvFenetre.setScene(sceneReserve);
        nouvFenetre.setX(550);
        nouvFenetre.setY(200);
        nouvFenetre.show();
    }
}
