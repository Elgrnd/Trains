package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.Joueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private HBox infoJoueur;
    private Map<Carte, VueCarte> carteButtonMap;

    private IJeu jeu;

    public VueJoueurCourant(IJeu jeu) {
        this.jeu = jeu;
        nomJoueur = new Label();
        instruction = new Label();
        HBox mainEtInfo = new HBox();
        mainJoueur = new HBox();
        infoJoueur = new HBox();
        infoJoueur.setSpacing(30);
        mainEtInfo.getChildren().addAll(mainJoueur,new Separator(Orientation.VERTICAL), infoJoueur);
        mainEtInfo.setSpacing(10);
        creerInfosJoueurCourant();
        Labels = new HBox();
        Label tiret = new Label(" - ");
        Labels.getChildren().addAll(nomJoueur, tiret, instruction);
        Labels.setAlignment(javafx.geometry.Pos.CENTER);
        carteButtonMap = new HashMap<>();
        getChildren().addAll(Labels, mainEtInfo);
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

    public void creerInfosJoueurCourant() {
        VBox infosEnJeu = new VBox();
        infosEnJeu.setSpacing(10);
        infoJoueur.getChildren().add(infosEnJeu);

        Label titre = new Label("En jeu :");
        titre.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #000000");
        infosEnJeu.getChildren().add(titre);

        StackPane pieces = new StackPane();
        Image imagePiece = new Image("images/boutons/coins.png");
        ImageView imagePieceView = new ImageView(imagePiece);
        imagePieceView.setFitWidth(50);
        imagePieceView.setFitHeight(50);
        Label nbPieces = new Label(String.valueOf(jeu.joueurCourantProperty().get().argentProperty().get()));
        nbPieces.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #000000");
        pieces.getChildren().addAll(imagePieceView, nbPieces);
        infosEnJeu.getChildren().add(pieces);

        StackPane rails = new StackPane();
        Image imageRail = new Image("images/boutons/rail.png");
        ImageView imageRailView = new ImageView(imageRail);
        imageRailView.setFitWidth(50);
        imageRailView.setFitHeight(50);
        Label nbRails = new Label(String.valueOf(jeu.joueurCourantProperty().get().pointsRailsProperty().get()));
        nbRails.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        rails.getChildren().addAll(imageRailView, nbRails);
        infosEnJeu.getChildren().add(rails);

        VBox autresInfos = new VBox();
        autresInfos.setSpacing(10);
        infoJoueur.getChildren().add(autresInfos);

        StackPane score = new StackPane();
        Image imageScore = new Image("images/boutons/score.png");
        ImageView imageScoreView = new ImageView(imageScore);
        imageScoreView.setFitWidth(50);
        imageScoreView.setFitHeight(50);
        Label scoreLabel = new Label(String.valueOf(jeu.joueurCourantProperty().get().scoreProperty().get()));
        scoreLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        score.getChildren().addAll(imageScoreView, scoreLabel);
        autresInfos.getChildren().add(score);

        StackPane nbCartes = new StackPane();
        Image imageCarte = new Image("images/boutons/deck.png");
        ImageView imageCarteView = new ImageView(imageCarte);
        imageCarteView.setFitWidth(50);
        imageCarteView.setFitHeight(50);
        Label nbCartesLabel = new Label(String.valueOf(jeu.joueurCourantProperty().get().mainProperty().size()));
        nbCartesLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        nbCartes.getChildren().addAll(imageCarteView, nbCartesLabel);
        autresInfos.getChildren().add(nbCartes);

        StackPane nbCartesEnJeu = new StackPane();
        Image imageCarteEnJeu = new Image("images/boutons/defausse.png");
        ImageView imageCarteEnJeuView = new ImageView(imageCarteEnJeu);
        imageCarteEnJeuView.setFitWidth(50);
        imageCarteEnJeuView.setFitHeight(50);
        Label nbCartesEnJeuLabel = new Label(String.valueOf(jeu.joueurCourantProperty().get().cartesEnJeuProperty().size()));
        nbCartesEnJeuLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        nbCartesEnJeu.getChildren().addAll(imageCarteEnJeuView, nbCartesEnJeuLabel);
        autresInfos.getChildren().add(nbCartesEnJeu);

        VBox infosNbJetonsRails = new VBox();
        infosNbJetonsRails.setSpacing(10);
        infosNbJetonsRails.setAlignment(Pos.CENTER);
        infoJoueur.getChildren().add(infosNbJetonsRails);
        StackPane jetonsRails = new StackPane();
        Image imageJetonsRails = new Image("images/boutons/rails.png");
        ImageView imageJetonsRailsView = new ImageView(imageJetonsRails);
        imageJetonsRailsView.setFitWidth(50);
        imageJetonsRailsView.setFitHeight(50);
        Label nbJetonsRails = new Label(String.valueOf(jeu.joueurCourantProperty().get().nbJetonsRailsProperty().get()));
        nbJetonsRails.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        jetonsRails.getChildren().addAll(imageJetonsRailsView, nbJetonsRails);
        infosNbJetonsRails.getChildren().add(jetonsRails);
    }

    public Button trouverBoutonCarte(Carte carteATrouver) {
        return carteButtonMap.get(carteATrouver);
    }
}
