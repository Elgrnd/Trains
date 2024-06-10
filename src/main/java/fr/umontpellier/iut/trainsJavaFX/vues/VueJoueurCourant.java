package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.Joueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

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
    private HBox mainJoueur, Labels;
    private Map<Carte, VueCarte> carteButtonMap;

    private IJeu jeu;

    public VueJoueurCourant(IJeu jeu) {
        this.jeu = jeu;
        nomJoueur = new Label();
        instruction = new Label();
        mainJoueur = new HBox();
        Labels = new HBox();
        Label tiret = new Label(" - ");
        Labels.getChildren().addAll(nomJoueur, tiret, instruction);
        Labels.setAlignment(javafx.geometry.Pos.CENTER);
        carteButtonMap = new HashMap<>();
        getChildren().addAll(Labels, mainJoueur);
    }

    public void creerBindings() {
        jeu.joueurCourantProperty().addListener(((observableValue, ancienJoueur, nouveauJoueur) -> {
            mainJoueur.getChildren().clear();
            carteButtonMap.clear();
            for (Carte carte : nouveauJoueur.mainProperty()) {
                VueCarte carteButton = new VueCarte(carte);
                carteButtonMap.put(carte, carteButton);
                mainJoueur.getChildren().add(carteButton);
                carteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    nouveauJoueur.uneCarteDeLaMainAEteChoisie(carteButton.getNom());
                });
            }

            nomJoueur.setText(nouveauJoueur.getNom());
            Labels.setStyle("-fx-background-color: " + CouleursJoueurs.couleursBackgroundJoueur.get(nouveauJoueur.getCouleur()) + "; -fx-font-size: 25");

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
                            VueCarte carteButton = new VueCarte(carteAjoutee);
                            carteButtonMap.put(carteAjoutee, carteButton);
                            mainJoueur.getChildren().add(carteButton);
                            carteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                                nouveauJoueur.uneCarteDeLaMainAEteChoisie(carteButton.getNom());
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
