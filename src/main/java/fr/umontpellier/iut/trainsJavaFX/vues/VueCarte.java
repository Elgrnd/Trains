package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Cette classe représente la vue d'une carte.
 * <p>
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarte extends Button {

    private final ICarte carte;
    private final ImageView imageCarte;

    public VueCarte(ICarte carte) {
        this.carte = carte;
        Image image = new Image("images/cartes/" + nomCarteToNomImage());
        imageCarte = new ImageView(image); // Store reference to ImageView
        int largeur = 115;
        imageCarte.setFitWidth(largeur);
        imageCarte.setFitHeight(largeur * 1.4);
        setGraphic(imageCarte);
        setStyle("-fx-background-color: transparent");
        setOnMouseEntered(event -> {
            imageCarte.setScaleX(1.2);
            imageCarte.setScaleY(1.2);
        });

        setOnMouseExited(event -> {
            imageCarte.setScaleX(1.0);
            imageCarte.setScaleY(1.0);
        });

    }

    public String getNom() {
        return carte.getNom();
    }

    public String nomCarteToNomImage() {
        String nom = carte.getNom();
        switch (nom) {
            case "Échangeur" -> nom = nom.replaceAll("Échangeur", "echangeur");
            case "Coopération" -> nom = nom.replaceAll("Coopération", "cooperation");
            case "Décharge" -> nom = nom.replaceAll("Décharge", "decharge");
            case "Dépôt" -> nom = nom.replaceAll("Dépôt", "depot");
            case "Dépotoir" -> nom = nom.replaceAll("Dépotoir", "depotoir");
            case "Centre de contrôle" -> nom = nom.replaceAll("Centre de contrôle", "centre_de_controle");
            case "Salle de contrôle" -> nom = nom.replaceAll("Salle de contrôle", "salle_de_controle");
            default -> {
                nom = nom.toLowerCase().replace(" ", "_");
            }
        }
        return nom + ".jpg";
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

    public ICarte getCarte() {
        return carte;
    }

    // Method to get the ImageView
    public ImageView getImageView() {
        return imageCarte;
    }

}
