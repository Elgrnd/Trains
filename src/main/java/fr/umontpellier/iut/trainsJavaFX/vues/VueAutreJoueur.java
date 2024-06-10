package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class VueAutreJoueur extends HBox {

    private IJoueur autreJoueur;

    public VueAutreJoueur(IJoueur autreJoueur) {
        this.autreJoueur = autreJoueur;
        getChildren().add(new Label(autreJoueur.getNom()));
        getChildren().add(new Label("Score : " + autreJoueur.getScoreTotal()));
        getChildren().add(new Label("Cartes en main : " + autreJoueur.mainProperty().size()));
        getChildren().add(new Label("Cartes jouées : " + autreJoueur.cartesEnJeuProperty().size()));
        setStyle("-fx-background-color: " + CouleursJoueurs.couleursBackgroundJoueur.get(autreJoueur.getCouleur()));
    }

    public void createBindings() {
    }
}
