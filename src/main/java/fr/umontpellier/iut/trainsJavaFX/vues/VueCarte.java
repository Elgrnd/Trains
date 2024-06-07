package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Cette classe représente la vue d'une carte.
 * <p>
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarte extends Button {

    private final ICarte carte;

    public VueCarte(ICarte carte) {
        this.carte = carte;
        Image image = new Image("images/cartes/" + nomCarteToNomImage() + ".jpg");
        ImageView imageCarte = new ImageView(image);
        imageCarte.setFitWidth(100);
        imageCarte.setFitHeight(150);
        setGraphic(imageCarte);

    }

    public String getNom() {
        return carte.getNom();
    }

    public String nomCarteToNomImage() {
        return carte.getNom().toLowerCase().replace(" ", "_");
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

}
