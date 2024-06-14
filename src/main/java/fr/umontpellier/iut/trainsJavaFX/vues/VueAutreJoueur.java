package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class VueAutreJoueur extends HBox {

    private IJoueur autreJoueur;

    public VueAutreJoueur(IJoueur autreJoueur) {
        this.autreJoueur = autreJoueur;
        creerVueAutreJoueur();
    }

    public void creerVueAutreJoueur() {
        StackPane score = new StackPane();
        StackPane jetonsRails = new StackPane();
        StackPane carteEnMain = new StackPane();
        StackPane carteEnJeu = new StackPane();

        Image imageScore = new Image("images/boutons/score.png");
        Image imageJetonsRails = new Image("images/boutons/rails.png");
        Image imageCarteEnMain = new Image("images/boutons/deck.png");
        Image imageCarteEnJeu = new Image("images/boutons/defausse.png");
        ImageView imageScoreView = new ImageView(imageScore);
        ImageView imageJetonsRailsView = new ImageView(imageJetonsRails);
        ImageView imageCarteEnMainView = new ImageView(imageCarteEnMain);
        ImageView imageCarteEnJeuView = new ImageView(imageCarteEnJeu);
        imageScoreView.setFitWidth(50);
        imageScoreView.setFitHeight(50);
        imageJetonsRailsView.setFitWidth(50);
        imageJetonsRailsView.setFitHeight(50);
        imageCarteEnMainView.setFitWidth(50);
        imageCarteEnMainView.setFitHeight(50);
        imageCarteEnJeuView.setFitWidth(50);
        imageCarteEnJeuView.setFitHeight(50);

        Label scoreLabel = new Label(String.valueOf(autreJoueur.scoreProperty().get()));
        Label jetonsRailsLabel = new Label(String.valueOf(autreJoueur.nbJetonsRailsProperty().get()));
        Label carteEnMainLabel = new Label(String.valueOf(autreJoueur.mainProperty().size()));
        Label carteEnJeuLabel = new Label(String.valueOf(autreJoueur.cartesEnJeuProperty().size()));
        scoreLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        jetonsRailsLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        carteEnMainLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        carteEnJeuLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #ffc400");

        getChildren().add(new Label(autreJoueur.getNom()));
        score.getChildren().addAll(imageScoreView, scoreLabel);
        jetonsRails.getChildren().addAll(imageJetonsRailsView, jetonsRailsLabel);
        carteEnMain.getChildren().addAll(imageCarteEnMainView, carteEnMainLabel);
        carteEnJeu.getChildren().addAll(imageCarteEnJeuView, carteEnJeuLabel);
        getChildren().addAll(score,jetonsRails, carteEnMain, carteEnJeu);
        setStyle("-fx-background-color: " + CouleursJoueurs.couleursBackgroundJoueur.get(autreJoueur.getCouleur()) + "; -fx-font-size: 25; -fx-padding: 10; -fx-border-radius: 10; -fx-border-color: black; -fx-border-width: 2");
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(10);
    }

    public void createBindings() {
    }
}
