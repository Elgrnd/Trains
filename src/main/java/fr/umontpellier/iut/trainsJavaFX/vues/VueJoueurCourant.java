package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {
    private Label nomJoueur;
    private Label instruction;
    private HBox mainJoueur;
    private Map<Carte, Button> carteButtonMap;

    private IJeu jeu;

    public VueJoueurCourant(IJeu jeu) {
        this.jeu = jeu;
        nomJoueur = new Label();
        instruction = new Label();
        mainJoueur = new HBox();
        carteButtonMap = new HashMap<>();
        getChildren().addAll(nomJoueur, instruction, mainJoueur);
        instruction.setStyle("-fx-font-size: 30");
        nomJoueur.setStyle("-fx-font-size: 25");
    }

    public void creerBindings() {
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
        instruction.textProperty().bind(GestionJeu.getJeu().instructionProperty());
    }

    public Button trouverBoutonCarte(Carte carteATrouver) {
        return carteButtonMap.get(carteATrouver);
    }
}
