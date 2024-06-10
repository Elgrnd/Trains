package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 * <p>
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, ses cartes en main, son score, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {
    private final IJeu jeu;
    private VuePlateau plateau;

    private VueJoueurCourant joueurCourant;

    private VueAutresJoueurs autresJoueursInfo;

    @FXML
    private VBox vboxJoueurCourant, vboxReserve, vboxCartes;
    @FXML
    private HBox hboxCartesJouees, hboxCartesRecues, hboxPasser;
    @FXML
    private VBox panePlateau;
    @FXML
    private HBox hboxAutresJoueurs;
    @FXML
    private Button boutonReserve;
    @FXML
    private Button passer;

    public VueDuJeu(IJeu jeu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/jeu.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.jeu = jeu;
        plateau = new VuePlateau();
        joueurCourant = new VueJoueurCourant(jeu);
        autresJoueursInfo = new VueAutresJoueurs(jeu);
        panePlateau.getChildren().add(plateau);
        vboxJoueurCourant.getChildren().add(joueurCourant);
        hboxAutresJoueurs.getChildren().add(autresJoueursInfo);
        Image image = new Image("images/boutons/passer.png");
        ImageView imagePasser = new ImageView(image);
        imagePasser.setFitWidth(75);
        imagePasser.setFitHeight(75);
        passer.setGraphic(imagePasser);
        passer.setStyle("-fx-background-color: transparent");
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        joueurCourant.creerBindings();
        passer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getJeu().passerAEteChoisi());
        passer.setOnMousePressed(actionPasserParDefaut);
        boutonReserve.setOnAction(new OuvrirNouvFenetreHandler());
        autresJoueursInfo.createBindings();
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));

}
