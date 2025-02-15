package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.collections.ListChangeListener;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    @FXML
    private Label labelCartesEnJeu, labelCartesRecues;
    @FXML
    private ImageView imageReserve;

    private Map<String, Integer> quantitesCartesReserves = new HashMap<>();
    private Stage reserveStage;  // Added reference to the Reserve window stage


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
        vboxReserve = new VBox();
        plateau = new VuePlateau();
        joueurCourant = new VueJoueurCourant(jeu);
        autresJoueursInfo = new VueAutresJoueurs(jeu);
        panePlateau.getChildren().add(plateau);
        vboxJoueurCourant.getChildren().add(joueurCourant);
        hboxAutresJoueurs.getChildren().add(autresJoueursInfo);
        hboxCartesJouees.setPadding(new Insets(14));
        hboxCartesRecues.setPadding(new Insets(14));

        Image imgReserve = new Image("images/boutons/deck.png");
        imageReserve.setImage(imgReserve);
        imageReserve.setVisible(true);
        imageReserve.setFitWidth(75);
        imageReserve.setFitHeight(75);

        Image image = new Image("images/boutons/passer.png");
        ImageView imagePasser = new ImageView(image);
        imagePasser.setFitWidth(75);
        imagePasser.setFitHeight(75);
        passer.setGraphic(imagePasser);
        passer.setStyle("-fx-background-color: transparent");

        labelCartesEnJeu.setStyle("-fx-font-size: 13");
        labelCartesRecues.setStyle("-fx-font-size: 13");
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
        quantitesCartesReserves.put("Parc d'attractions", 10);
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
        getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {  //Passer avec barre d'espace
            if (event.getCode() == KeyCode.SPACE) {
                getJeu().passerAEteChoisi();
            }
        });
        boutonReserve.setOnAction((ouvrirNouvFenetreHandler) -> {
            reserveStage = new Stage();
            reserveStage.setTitle("Réserve");
            reserveStage.initModality(Modality.WINDOW_MODAL); // Définir modalité : Empecher interaction avec fenêtre principale
            reserveStage.initOwner(getScene().getWindow());   // définir propriétaire de la nouvelle fenêtre

            BorderPane layoutReserve = new BorderPane();
            Scene sceneReserve = new Scene(layoutReserve, 1250, 680);

            GridPane gridPaneCartesReserves = new GridPane();
            gridPaneCartesReserves.setAlignment(Pos.CENTER);

            Label topLabelReserve = new Label("Bienvenue dans la réserve ! \nVeuillez acheter une carte ou quitter.");
            HBox topHboxReserve = new HBox(topLabelReserve);
            topHboxReserve.setAlignment(Pos.CENTER);
            topHboxReserve.setPadding(new Insets(12));
            topLabelReserve.setTextAlignment(TextAlignment.CENTER);
            topLabelReserve.setStyle("-fx-font-size: 20");

            Button bottomButtonQuitReserve = new Button();
            Image image = new Image("images/boutons/passer.png");
            ImageView imagePasser = new ImageView(image);
            imagePasser.setFitWidth(75);
            imagePasser.setFitHeight(75);
            bottomButtonQuitReserve.setStyle("-fx-background-color: transparent");
            bottomButtonQuitReserve.setGraphic(imagePasser);
            passer.setStyle("-fx-background-color: transparent");
            HBox bottomHboxReserve = new HBox(bottomButtonQuitReserve);
            bottomHboxReserve.setAlignment(Pos.CENTER);
            bottomHboxReserve.setPadding(new Insets(30));

            EventHandler<MouseEvent> clicQuitter = event -> reserveStage.close();
            bottomButtonQuitReserve.addEventHandler(MouseEvent.MOUSE_CLICKED, clicQuitter);

            reserveStage.setScene(sceneReserve);
            layoutReserve.setCenter(gridPaneCartesReserves);
            layoutReserve.setTop(topHboxReserve);
            layoutReserve.setBottom(bottomHboxReserve);

            int ligne = 0;
            int colonne = 0;
            if (!jeu.getReserve().isEmpty() && jeu.getReserve() != null) {
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
                        int argentJoueur = jeu.getJoueurCourant().getArgent();

                        if (qttCourante > 0 && argentJoueur >= carte.getCout()) {
                            getJeu().uneCarteDeLaReserveEstAchetee(vueCarte.getNom());
                            if(!carte.getNom().equals("Ferraille")) {
                                quantitesCartesReserves.put(carte.getNom(), qttCourante - 1);
                                Label labelQtt = (Label) ((VBox) vueCarte.getParent()).getChildren().get(1);
                                labelQtt.setText("Disponible: " + (qttCourante - 1));

                                if (qttCourante - 1 == 0) {
                                    vueCarte.setDisable(true);
                                }
                            }
                        } else {
                            System.out.println("Pas assez d'argent");
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
            reserveStage.setX(350);
            reserveStage.setY(200);
            reserveStage.showAndWait();  // Empêcher intéraction avec fenêtre principale tant que fenêtre réserve n'est pas fermée
        });

        jeu.joueurCourantProperty().addListener(((observableValue, ancienJoueur, nouveauJoueur) -> {
            hboxCartesJouees.getChildren().clear();
            for (Carte carte : nouveauJoueur.cartesEnJeuProperty()) {
                VueCarte vueCarte = new VueCarte(carte);
                hboxCartesJouees.getChildren().add(vueCarte);
            }
            if (reserveStage != null && reserveStage.isShowing()) {  //Fenetre Reserve se ferme quand le joueur courant change
                reserveStage.close();
            }
        }));
        jeu.joueurCourantProperty().addListener(((observableValue, ancienJoueur, nouveauJoueur) -> {
            hboxCartesRecues.getChildren().clear();
            for (Carte carte : nouveauJoueur.cartesRecuesProperty()) {
                VueCarte vueCarte = new VueCarte(carte);
                hboxCartesRecues.getChildren().add(vueCarte);
            }
            ancienJoueur.cartesEnJeuProperty().removeListener(changementCartesEnJeuListener);
            nouveauJoueur.cartesEnJeuProperty().addListener(changementCartesEnJeuListener);
            ancienJoueur.cartesRecuesProperty().removeListener(changementCartesRecuesListener);
            nouveauJoueur.cartesRecuesProperty().addListener(changementCartesRecuesListener);
        }));

        autresJoueursInfo.createBindings();
    }
    private final ListChangeListener<Carte> changementCartesEnJeuListener = (change) -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (Carte carteEnlevee : change.getRemoved()) {
                    hboxCartesJouees.getChildren().removeIf(node -> ((VueCarte) node).getCarte().equals(carteEnlevee));
                }
            }
            if (change.wasAdded()) {
                for (Carte carteAjoutee : change.getAddedSubList()) {
                    VueCarte vueCarte = new VueCarte(carteAjoutee);
                    hboxCartesJouees.getChildren().add(vueCarte);
                }
            }
        }
    };
    private final ListChangeListener<Carte> changementCartesRecuesListener = (change) -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (Carte carteEnlevee : change.getRemoved()) {
                    hboxCartesRecues.getChildren().removeIf(node -> ((VueCarte) node).getCarte().equals(carteEnlevee));
                }
            }
            if (change.wasAdded()) {
                for (Carte carteAjoutee : change.getAddedSubList()) {
                    VueCarte vueCarte = new VueCarte(carteAjoutee);
                    hboxCartesRecues.getChildren().add(vueCarte);
                }
            }
        }
    };


    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));

}
