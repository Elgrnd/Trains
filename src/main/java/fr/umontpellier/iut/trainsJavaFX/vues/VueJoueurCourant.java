package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {
    private Label nomJoueur, instruction, nbPieces, nbRails, scoreLabel, nbCartesLabel, nbCartesEnJeuLabel, nbJetonsRails;
    private HBox mainJoueur, Labels;
    private HBox infoJoueur, hboxNames;
    private Map<Carte, VueCarte> carteButtonMap;

    private IJeu jeu;

    public VueJoueurCourant(IJeu jeu) {
        this.jeu = jeu;
        nomJoueur = new Label();
        instruction = new Label();
        nbPieces = new Label();
        nbRails = new Label();
        scoreLabel = new Label();
        nbCartesLabel = new Label();
        nbCartesEnJeuLabel = new Label();
        nbJetonsRails = new Label();
        HBox mainEtInfo = new HBox();
        hboxNames = new HBox();
        mainJoueur = new HBox();
        infoJoueur = new HBox();
        infoJoueur.setSpacing(30);
        mainEtInfo.getChildren().addAll(mainJoueur,new Separator(Orientation.VERTICAL), infoJoueur);
        mainEtInfo.setSpacing(10);
        Labels = new HBox();
        Label tiret = new Label(" - ");
        Labels.getChildren().addAll(nomJoueur, tiret, instruction);
        Labels.setAlignment(javafx.geometry.Pos.CENTER);
        carteButtonMap = new HashMap<>();
        getChildren().addAll(Labels, mainEtInfo);
        creerInfosJoueurCourant();
        mainJoueur.setPadding(new Insets(18));
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
            nbCartesLabel.textProperty().bind(Bindings.size(nouveauJoueur.mainProperty()).asString());
            nbCartesEnJeuLabel.textProperty().bind(Bindings.size(nouveauJoueur.cartesEnJeuProperty()).asString());
            nbJetonsRails.textProperty().bind(nouveauJoueur.nbJetonsRailsProperty().asString());
            scoreLabel.textProperty().bind(nouveauJoueur.scoreProperty().asString());
            nbPieces.textProperty().bind(nouveauJoueur.argentProperty().asString());
            nbRails.textProperty().bind(nouveauJoueur.pointsRailsProperty().asString());
        }));
        instruction.textProperty().bind(GestionJeu.getJeu().instructionProperty());

    }

    public void creerInfosJoueurCourant() {
        VBox infosEnJeu = new VBox();
        VBox autresInfos = new VBox();
        VBox infosNbJetonsRails = new VBox();
        autresInfos.getChildren().add(new Label(" "));
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
        nbPieces.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #000000");
        pieces.getChildren().addAll(imagePieceView, nbPieces);
        infosEnJeu.getChildren().add(pieces);

        Label labelPieces = new Label("Argent");
        labelPieces.setStyle("-fx-background-color: white; -fx-border-color: black;");
        labelPieces.setVisible(false);
        infosEnJeu.getChildren().add(labelPieces);
        pieces.setOnMouseEntered(event -> labelPieces.setVisible(true));
        pieces.setOnMouseMoved(event -> {
            labelPieces.setLayoutX(event.getSceneX());
            labelPieces.setLayoutY(event.getSceneY());
        });
        pieces.setOnMouseExited(event -> labelPieces.setVisible(false));

        StackPane rails = new StackPane();
        Image imageRail = new Image("images/boutons/rail.png");
        ImageView imageRailView = new ImageView(imageRail);
        imageRailView.setFitWidth(50);
        imageRailView.setFitHeight(50);
        nbRails.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        rails.getChildren().addAll(imageRailView, nbRails);
        infosEnJeu.getChildren().add(rails);

        Label labelRails = new Label("Jetons Rails");
        labelRails.setStyle("-fx-background-color: white; -fx-border-color: black;");
        labelRails.setVisible(false);
        infosEnJeu.getChildren().add(labelRails);
        rails.setOnMouseEntered(event -> labelRails.setVisible(true));
        rails.setOnMouseMoved(event -> {
            labelRails.setLayoutX(event.getSceneX() + 10);
            labelRails.setLayoutY(event.getSceneY() + 10);
        });
        rails.setOnMouseExited(event -> labelRails.setVisible(false));

        autresInfos.setSpacing(10);
        infoJoueur.getChildren().add(autresInfos);

        StackPane score = new StackPane();
        Image imageScore = new Image("images/boutons/score.png");
        ImageView imageScoreView = new ImageView(imageScore);
        imageScoreView.setFitWidth(50);
        imageScoreView.setFitHeight(50);
        scoreLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        score.getChildren().addAll(imageScoreView, scoreLabel);
        infosNbJetonsRails.getChildren().add(score);

        Label labelScore = new Label("Points Victoire");
        labelScore.setStyle("-fx-background-color: white; -fx-border-color: black;");
        labelScore.setVisible(false);
        infosNbJetonsRails.getChildren().add(labelScore);
        score.setOnMouseEntered(event -> labelScore.setVisible(true));
        score.setOnMouseMoved(event -> {
            labelScore.setLayoutX(event.getSceneX() );
            labelScore.setLayoutY(event.getSceneY() );
        });
        score.setOnMouseExited(event -> labelScore.setVisible(false));

        StackPane nbCartes = new StackPane();
        Image imageCarte = new Image("images/boutons/deck.png");
        ImageView imageCarteView = new ImageView(imageCarte);
        imageCarteView.setFitWidth(50);
        imageCarteView.setFitHeight(50);
        nbCartesLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        nbCartes.getChildren().addAll(imageCarteView, nbCartesLabel);
        autresInfos.getChildren().add(nbCartes);

        Label labelCartesEnMain = new Label("Cartes en main");
        labelCartesEnMain.setStyle("-fx-background-color: white; -fx-border-color: black;");
        labelCartesEnMain.setVisible(false);
        autresInfos.getChildren().add(labelCartesEnMain);
        nbCartes.setOnMouseEntered(event -> labelCartesEnMain.setVisible(true));
        nbCartes.setOnMouseMoved(event -> {
            labelCartesEnMain.setLayoutX(event.getSceneX() );
            labelCartesEnMain.setLayoutY(event.getSceneY() );
        });
        nbCartes.setOnMouseExited(event -> labelCartesEnMain.setVisible(false));

        StackPane nbCartesEnJeu = new StackPane();
        Image imageCarteEnJeu = new Image("images/boutons/defausse.png");
        ImageView imageCarteEnJeuView = new ImageView(imageCarteEnJeu);
        imageCarteEnJeuView.setFitWidth(50);
        imageCarteEnJeuView.setFitHeight(50);
        nbCartesEnJeuLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        nbCartesEnJeu.getChildren().addAll(imageCarteEnJeuView, nbCartesEnJeuLabel);
        autresInfos.getChildren().add(nbCartesEnJeu);

        Label labelCartesEnJeu = new Label("Cartes jouées");
        labelCartesEnJeu.setStyle("-fx-background-color: white; -fx-border-color: black;");
        labelCartesEnJeu.setVisible(false);
        autresInfos.getChildren().add(labelCartesEnJeu);
        nbCartesEnJeu.setOnMouseEntered(event -> labelCartesEnJeu.setVisible(true));
        nbCartesEnJeu.setOnMouseMoved(event -> {
            labelCartesEnJeu.setLayoutX(event.getSceneX() );
            labelCartesEnJeu.setLayoutY(event.getSceneY() );
        });
        nbCartesEnJeu.setOnMouseExited(event -> labelCartesEnJeu.setVisible(false));

        infosNbJetonsRails.setSpacing(10);
        infosNbJetonsRails.setAlignment(Pos.CENTER);
        infoJoueur.getChildren().add(infosNbJetonsRails);
        StackPane jetonsRails = new StackPane();
        Image imageJetonsRails = new Image("images/boutons/rails.png");
        ImageView imageJetonsRailsView = new ImageView(imageJetonsRails);
        imageJetonsRailsView.setFitWidth(50);
        imageJetonsRailsView.setFitHeight(50);
        nbJetonsRails.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ffc400");
        jetonsRails.getChildren().addAll(imageJetonsRailsView, nbJetonsRails);
        infosNbJetonsRails.getChildren().add(jetonsRails);

        Label labelJetonsRails = new Label("Jetons rails");
        labelJetonsRails.setStyle("-fx-background-color: white; -fx-border-color: black;");
        labelJetonsRails.setVisible(false);
        infosNbJetonsRails.getChildren().add(labelJetonsRails);
        jetonsRails.setOnMouseEntered(event -> labelJetonsRails.setVisible(true));
        jetonsRails.setOnMouseMoved(event -> {
            labelJetonsRails.setLayoutX(event.getSceneX() );
            labelJetonsRails.setLayoutY(event.getSceneY() );
        });
        jetonsRails.setOnMouseExited(event -> labelJetonsRails.setVisible(false));
    }

    public Button trouverBoutonCarte(Carte carteATrouver) {
        return carteButtonMap.get(carteATrouver);
    }
}
