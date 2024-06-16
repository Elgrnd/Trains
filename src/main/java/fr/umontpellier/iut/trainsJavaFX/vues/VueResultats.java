package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.concurrent.atomic.AtomicInteger;

public class VueResultats extends HBox {

    private TrainsIHM ihm;
    VBox classement;
    VBox joueurs;
    VBox scores;


    public VueResultats(TrainsIHM ihm) {
        this.ihm = ihm;
        classement = new VBox();
        joueurs = new VBox();
        scores = new VBox();
        classement.setAlignment(Pos.TOP_CENTER);
        joueurs.setAlignment(Pos.TOP_CENTER);
        scores.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(classement, new Separator(Orientation.VERTICAL), joueurs, new Separator(Orientation.VERTICAL), scores);
        afficherResultats();
        setAlignment(Pos.CENTER);
        setSpacing(10);
    }

    public void afficherResultats() {
        AtomicInteger i = new AtomicInteger(1);
        classement.getChildren().clear();
        joueurs.getChildren().clear();
        scores.getChildren().clear();
        classement.getChildren().add(new Label("Classement"));
        joueurs.getChildren().add(new Label("Joueurs"));
        scores.getChildren().add(new Label("Scores"));
        ihm.getJeu().getJoueurs().stream().sorted((j1, j2) -> j2.getScoreTotal() - j1.getScoreTotal()).forEach(joueur -> {
            classement.getChildren().add(new Pane());
            joueurs.getChildren().add(new Pane());
            scores.getChildren().add(new Pane());
            classement.getChildren().add(new Label(String.valueOf(i.get())));
            joueurs.getChildren().add(new Label(joueur.getNom()));
            scores.getChildren().add(new Label(String.valueOf(joueur.getScoreTotal())));
            i.getAndIncrement();
        });
    }

}
