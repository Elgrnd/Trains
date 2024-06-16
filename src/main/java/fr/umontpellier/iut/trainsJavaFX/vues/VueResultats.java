package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.util.concurrent.atomic.AtomicInteger;

public class VueResultats extends BorderPane {

    private TrainsIHM ihm;
    private HBox resultats;
    private VBox classement;
    private VBox joueurs;
    private VBox scores;
    private Button rejouer;


    public VueResultats(TrainsIHM ihm) {
        this.ihm = ihm;
        setStyle("-fx-font-size: 20");
        resultats = new HBox();
        classement = new VBox();
        joueurs = new VBox();
        scores = new VBox();
        rejouer = new Button("Rejouer");
        setCenter(resultats);
        setBottom(rejouer);
        BorderPane.setMargin(rejouer, new Insets(10));
        BorderPane.setAlignment(rejouer, Pos.CENTER);
        classement.setAlignment(Pos.TOP_CENTER);
        joueurs.setAlignment(Pos.TOP_CENTER);
        scores.setAlignment(Pos.TOP_CENTER);
        resultats.getChildren().addAll(classement, new Separator(Orientation.VERTICAL), joueurs, new Separator(Orientation.VERTICAL), scores);
        afficherResultats();
        resultats.setAlignment(Pos.CENTER);
        resultats.setSpacing(10);
        rejouer.setOnAction(event -> rejouer());
    }

    public void afficherResultats() {
        AtomicInteger i = new AtomicInteger(1);
        classement.getChildren().clear();
        joueurs.getChildren().clear();
        scores.getChildren().clear();
        Label classementLabel = new Label("Classement");
        classementLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold");
        classement.getChildren().add(classementLabel);
        Label joueursLabel = new Label("Joueurs");
        joueursLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold");
        joueurs.getChildren().add(joueursLabel);
        Label scoresLabel = new Label("Scores");
        scoresLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold");
        scores.getChildren().add(scoresLabel);
        ihm.getJeu().getJoueurs().stream().sorted((j1, j2) -> j2.getScoreTotal() - j1.getScoreTotal()).forEach(joueur -> {
            classement.getChildren().add(new Pane());
            joueurs.getChildren().add(new Pane());
            scores.getChildren().add(new Pane());
            Label labelClassement = new Label(String.valueOf(i.get()));
            Label labelJoueur = new Label(joueur.getNom());
            Label labelScore = new Label(String.valueOf(joueur.getScoreTotal()));
            labelClassement.setTextFill(Paint.valueOf(CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur())));
            labelJoueur.setTextFill(Paint.valueOf(CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur())));
            labelScore.setTextFill(Paint.valueOf(CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur())));
            classement.getChildren().add(labelClassement);
            joueurs.getChildren().add(labelJoueur);
            scores.getChildren().add(labelScore);
            i.getAndIncrement();
        });
    }

    public void rejouer() {
        ihm.getPrimaryStage().close();
        TrainsIHM newIHM = new TrainsIHM();
        newIHM.start(ihm.getPrimaryStage());
    }

}
