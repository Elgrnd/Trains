package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
public class VueDuJeu extends VBox {
    private Label instruction;
    private final IJeu jeu;
    private VuePlateau plateau;
    private Label nomJoueur;
    private Button passer;
    private HBox mainJoueur;
    private Map<Carte, Button> carteButtonMap;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        nomJoueur = new Label();
        instruction = new Label();
        passer = new Button("Passer");
        mainJoueur = new HBox();
        carteButtonMap = new HashMap<>();
        getChildren().addAll(plateau, instruction, nomJoueur, passer, mainJoueur);
        instruction.setStyle("-fx-font-size: 30");
        nomJoueur.setStyle("-fx-font-size: 25");
        passer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getJeu().passerAEteChoisi());
        passer.setOnMousePressed(actionPasserParDefaut);
        jeu.joueurCourantProperty().addListener(((observableValue, ancienJoueur, nouveauJoueur) -> {
            mainJoueur.getChildren().clear();
            carteButtonMap.clear();
            for (Carte carte : nouveauJoueur.mainProperty()) {
                Button carteButton = new Button(carte.getNom());
                carteButtonMap.put(carte, carteButton);
                mainJoueur.getChildren().add(carteButton);
                carteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    nouveauJoueur.uneCarteDeLaMainAEteChoisie(carteButton.getText());
                });
            }
            nomJoueur.setText(nouveauJoueur.getNom());
            nouveauJoueur.mainProperty().addListener((ListChangeListener<Carte>) change -> {
                while (change.next()) {
                    if (change.wasRemoved()) {
                        for (Carte carteEnlevee : change.getRemoved()) {
                            Button boutonARetirer = trouverBoutonCarte(carteEnlevee);
                            if (boutonARetirer != null) {
                                mainJoueur.getChildren().remove(boutonARetirer);
                                carteButtonMap.remove(carteEnlevee);
                            }
                        }
                    }
                    if (change.wasAdded()) {
                        for (Carte carteAjoutee : change.getAddedSubList()) {
                            Button carteButton = new Button(carteAjoutee.getNom());
                            carteButtonMap.put(carteAjoutee, carteButton);
                            mainJoueur.getChildren().add(carteButton);
                            carteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                                nouveauJoueur.uneCarteDeLaMainAEteChoisie(carteButton.getText());
                            });
                        }
                    }
                }
            });
        }));
    }
    public Button trouverBoutonCarte(Carte carteATrouver) {
        return carteButtonMap.get(carteATrouver);
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        instruction.textProperty().bind(getJeu().instructionProperty());
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));

}
