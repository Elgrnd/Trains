package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends HBox {

    private IJeu jeu;

    public VueAutresJoueurs(IJeu jeu) {
        this.jeu = jeu;
        for (IJoueur joueur : jeu.getJoueurs()) {
            if (joueur != jeu.joueurCourantProperty().get()) {
                VueAutreJoueur vueAutreJoueur = new VueAutreJoueur(joueur);
                getChildren().add(vueAutreJoueur);
            }
        }
        setSpacing(10);
        setMargin(this, new javafx.geometry.Insets(10));
    }

    public void createBindings() {
        jeu.joueurCourantProperty().addListener(((observableValue, ancienJoueur, nouveauJoueur) -> {
            getChildren().clear();
            for (IJoueur joueur : jeu.getJoueurs()) {
                if (joueur != nouveauJoueur) {
                    VueAutreJoueur vueAutreJoueur = new VueAutreJoueur(joueur);
                    getChildren().add(vueAutreJoueur);
                }
            }
        }));
    }
}
