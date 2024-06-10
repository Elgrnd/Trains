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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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

    private VueAutresJoueurs autresJoueurs;

    @FXML
    private VBox vboxJoueurCourant, vboxReserve, vboxCartes;
    @FXML
    private HBox hboxAutresJoueurs, hboxCartesJouees, hboxCartesRecues, hboxPasser;
    @FXML
    private VBox panePlateau;
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
        autresJoueurs = new VueAutresJoueurs();
        panePlateau.getChildren().add(plateau);
        vboxJoueurCourant.getChildren().add(joueurCourant);
        hboxAutresJoueurs.getChildren().add(autresJoueurs);
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        joueurCourant.creerBindings();
        passer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getJeu().passerAEteChoisi());
        passer.setOnMousePressed(actionPasserParDefaut);
        boutonReserve.setOnAction(new OuvrirNouvFenetreHandler());
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));

}
