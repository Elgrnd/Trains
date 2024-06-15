package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 * <p>
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, ses cartes en main, son score, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {
    private final IJeu jeu;
    private VuePlateau plateau;

    private VueJoueurCourant joueurCourant;

    private VueAutresJoueurs autresJoueursInfo;

    @FXML
    private VBox vboxJoueurCourant, vboxReserve, vboxCartes;
    @FXML
    private HBox hboxCartesJouees, hboxCartesRecues, hboxPasser;
    @FXML
    private VBox panePlateau;
    @FXML
    private HBox hboxAutresJoueurs;
    @FXML
    private Button boutonReserve;
    @FXML
    private Button passer;

    private Map<String, Integer> quantitesCartesReserves = new HashMap<>();


    public VueDuJeu(IJeu jeu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/jeu.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.jeu = jeu;
        plateau = new VuePlateau();
        joueurCourant = new VueJoueurCourant(jeu);
        autresJoueursInfo = new VueAutresJoueurs(jeu);
        panePlateau.getChildren().add(plateau);
        vboxJoueurCourant.getChildren().add(joueurCourant);
        hboxAutresJoueurs.getChildren().add(autresJoueursInfo);
        Image image = new Image("images/boutons/passer.png");
        ImageView imagePasser = new ImageView(image);
        imagePasser.setFitWidth(75);
        imagePasser.setFitHeight(75);
        passer.setGraphic(imagePasser);
        passer.setStyle("-fx-background-color: transparent");
        quantitesCartesReserves.put("Aiguillage", 10);
        quantitesCartesReserves.put("Appartement", 10);
        quantitesCartesReserves.put("Atelier de maintenance", 10);
        quantitesCartesReserves.put("Bureau du chef de gare", 10);
        quantitesCartesReserves.put("Cabine du conducteur", 10);
        quantitesCartesReserves.put("Centre de contrôle", 10);
        quantitesCartesReserves.put("Centre de renseignements", 10);
        quantitesCartesReserves.put("Coopération", 10);
        quantitesCartesReserves.put("Décharge", 10);
        quantitesCartesReserves.put("Dépôt", 10);
        quantitesCartesReserves.put("Dépotoir", 10);
        quantitesCartesReserves.put("Échangeur", 10);
        quantitesCartesReserves.put("Ferraille", 70);
        quantitesCartesReserves.put("Ferronnerie", 10);
        quantitesCartesReserves.put("Feu de signalisation", 10);
        quantitesCartesReserves.put("Gare", 20);
        quantitesCartesReserves.put("Gratte-ciel", 10);
        quantitesCartesReserves.put("Horaires estivaux", 10);
        quantitesCartesReserves.put("Horaires temporaires", 10);
        quantitesCartesReserves.put("Immeuble", 10);
        quantitesCartesReserves.put("Parc d’attractions", 10);
        quantitesCartesReserves.put("Passage en gare", 10);
        quantitesCartesReserves.put("Personnel de gare", 10);
        quantitesCartesReserves.put("Pont en acier", 10);
        quantitesCartesReserves.put("Pose de rails", 20);
        quantitesCartesReserves.put("Remorquage", 10);
        quantitesCartesReserves.put("Salle de contrôle", 10);
        quantitesCartesReserves.put("TGV", 10);
        quantitesCartesReserves.put("Train de marchandises", 10);
        quantitesCartesReserves.put("Train de tourisme", 10);
        quantitesCartesReserves.put("Train direct", 10);
        quantitesCartesReserves.put("Train express", 20);
        quantitesCartesReserves.put("Train matinal", 10);
        quantitesCartesReserves.put("Train omnibus", 30);
        quantitesCartesReserves.put("Train postal", 10);
        quantitesCartesReserves.put("Tunnel", 10);
        quantitesCartesReserves.put("Usine de wagons", 10);
        quantitesCartesReserves.put("Viaduc", 10);
        quantitesCartesReserves.put("Voie souterraine", 10);
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        joueurCourant.creerBindings();
        passer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getJeu().passerAEteChoisi());
        passer.setOnMousePressed(actionPasserParDefaut);

        boutonReserve.setOnAction((ouvrirNouvFenetreHandler) -> {
            Stage nouvFenetre = new Stage();
            nouvFenetre.setTitle("Réserve");
            nouvFenetre.initModality(Modality.WINDOW_MODAL); // Définir modalité : Empecher interaction avec fenêtre principale
            nouvFenetre.initOwner(getScene().getWindow());   // définir propriétaire de la nouvelle fenêtre

            BorderPane layoutReserve = new BorderPane();
            Scene sceneReserve = new Scene(layoutReserve, 1150, 680);

            GridPane gridPaneCartesReserves = new GridPane();

            Label topLabelReserve = new Label("Bienvenue dans la réserve ! \nVeuillez acheter une carte ou quitter.");
            HBox topHboxReserve = new HBox(topLabelReserve);
            topHboxReserve.setAlignment(Pos.CENTER);
            topHboxReserve.setPadding(new Insets(12));
            topLabelReserve.setTextAlignment(TextAlignment.CENTER);
            topLabelReserve.setStyle("-fx-font-size: 20");

            Button bottomButtonQuitReserve = new Button("Quitter");
            Image image = new Image("images/boutons/passer.png");
            ImageView imagePasser = new ImageView(image);
            imagePasser.setFitWidth(75);
            imagePasser.setFitHeight(75);
            bottomButtonQuitReserve.setStyle("-fx-font-size: 15");
            bottomButtonQuitReserve.setGraphic(imagePasser);
            passer.setStyle("-fx-background-color: transparent");
            HBox bottomHboxReserve = new HBox(bottomButtonQuitReserve);
            bottomHboxReserve.setAlignment(Pos.CENTER);
            bottomHboxReserve.setPadding(new Insets(50));

            EventHandler<MouseEvent> clicQuitter = event -> nouvFenetre.close();
            bottomButtonQuitReserve.addEventHandler(MouseEvent.MOUSE_CLICKED, clicQuitter);

            nouvFenetre.setScene(sceneReserve);
            layoutReserve.setCenter(gridPaneCartesReserves);
            layoutReserve.setTop(topHboxReserve);
            layoutReserve.setBottom(bottomHboxReserve);

            int ligne = 0;
            int colonne = 0;
            if(!jeu.getReserve().isEmpty() && jeu.getReserve() != null) {
                for (Carte carte : jeu.getReserve()) {
                    VueCarte vueCarte = new VueCarte(carte);
                    vueCarte.setOnMouseEntered(event -> {
                        ImageView imageView = vueCarte.getImageView();
                        imageView.setFitWidth(imageView.getFitWidth() * 1.4);
                        imageView.setFitHeight(imageView.getFitHeight() * 1.4);
                    });
                    vueCarte.setOnMouseExited(event -> {
                        ImageView imageView = vueCarte.getImageView();
                        imageView.setFitWidth(imageView.getFitWidth() / 1.4);
                        imageView.setFitHeight(imageView.getFitHeight() / 1.4);
                    });

                    vueCarte.setCarteChoisieListener(event -> {
                        int qttCourante = quantitesCartesReserves.get(carte.getNom());
                        if (qttCourante > 0) {
                            getJeu().uneCarteDeLaReserveEstAchetee(vueCarte.getNom());
                            quantitesCartesReserves.put(carte.getNom(), qttCourante - 1);
                            Label labelQtt = (Label) ((VBox) vueCarte.getParent()).getChildren().get(1);    // Rafraichir le Label pour avoir la bonne quantité
                            labelQtt.setText("Disponible: " + (qttCourante - 1));
                            if (qttCourante - 1 == 0) {
                                vueCarte.setDisable(true);
                            }
                        }
                    });
                    Label labelQtt = new Label("Disponible: " + (quantitesCartesReserves.get(carte.getNom())));
                    labelQtt.setAlignment(Pos.CENTER);
                    labelQtt.setStyle("-fx-font-size: 12");

                    VBox cardBox = new VBox(vueCarte, labelQtt);
                    cardBox.setAlignment(Pos.CENTER);

                    gridPaneCartesReserves.add(cardBox, colonne, ligne);
                    colonne++;
                    if (colonne == 8) {
                        colonne = 0;
                        ligne++;
                    }
                }
            }
            nouvFenetre.setX(350);
            nouvFenetre.setY(200);
            nouvFenetre.showAndWait();  // Empêcher intéraction avec fenêtre principale tant que fenêtre réserve n'est pas fermée
        });

        autresJoueursInfo.createBindings();
    }


    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));

}
