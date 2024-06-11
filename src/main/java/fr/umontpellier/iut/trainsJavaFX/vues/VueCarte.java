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
        Image image = new Image("images/cartes/" + nomCarteToNomImage());
        ImageView imageCarte = new ImageView(image);
        int largeur = 120;
        imageCarte.setFitWidth(largeur);
        imageCarte.setFitHeight(largeur*1.4);
        setGraphic(imageCarte);
        setStyle("-fx-background-color: transparent");
    }

    public String getNom() {
        return carte.getNom();
    }

    public String nomCarteToNomImage() {
        String nom = carte.getNom();
        switch (nom) {
            case "GratteCiel" -> nom = nom.replaceAll("GratteCiel", "gratte-ciel");
            case "TGV" -> nom = nom.replaceAll("TGV", "tgv");
            case "ParcDAttractions" -> nom = nom.replaceAll("ParcDAttractions", "parc_d'attractions");
            default -> {
                nom = nom.replaceAll("[^a-zA-Z0-9_]|'", "_"); // replace special characters and 'ith _
                nom = nom.toLowerCase(); // convert to lowercase
            }
        }
        return nom + ".jpg";
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

}
