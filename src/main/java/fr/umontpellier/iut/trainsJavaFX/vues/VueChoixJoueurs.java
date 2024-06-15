package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.plateau.Plateau;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 * <p>
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    private final ObservableList<String> nomsJoueurs;
    private Plateau plateauChoisi;
    private VBox menu, choixJoueurs;
    private ComboBox<Plateau> comboBoxPlateau;
    private ComboBox<Integer> comboBoxNbJoueurs;
    private Label labelNbJoueurs;
    private Button buttonDemarrerPartie;

    public VueChoixJoueurs() {
        reglagesFenetre();
        nomsJoueurs = FXCollections.observableArrayList();
        comboBoxPlateau = new ComboBox<>();
        comboBoxPlateau.getItems().addAll(Plateau.values());
        comboBoxNbJoueurs = new ComboBox<>();
        comboBoxNbJoueurs.getItems().addAll(2, 3, 4);
        Label labelPlateau = new Label("Choisissez le plateau :");
        labelNbJoueurs = new Label("Choisissez le nombre de joueurs :");
        choixJoueurs = new VBox();
        choixJoueurs.setSpacing(10);
        choixJoueurs.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(labelPlateau, comboBoxPlateau, labelNbJoueurs, comboBoxNbJoueurs, choixJoueurs);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(20);
        buttonDemarrerPartie = new Button("Démarrer la partie");
        creerBindings();

    }

    public void reglagesFenetre() {
        setTitle("Début de la partie");
        menu = new VBox();
        Scene scene = new Scene(menu);
        setScene(scene);
        menu.setPadding(new Insets(20));
    }

    public void creerBindings() {
        BooleanProperty plateauChoisiProperty = new SimpleBooleanProperty(false);
        comboBoxPlateau.valueProperty().addListener((observable, oldValue, newValue) -> {
            plateauChoisi = newValue;
            plateauChoisiProperty.set(true);
        });
        labelNbJoueurs.visibleProperty().bind(plateauChoisiProperty);
        comboBoxNbJoueurs.visibleProperty().bind(plateauChoisiProperty);

        comboBoxNbJoueurs.valueProperty().addListener((observable, oldValue, newValue) -> {
            choixJoueurs.getChildren().clear();
            nomsJoueurs.clear();
            for (int i = 0; i < newValue; i++) {
                nomsJoueurs.add("");
                Label label = new Label("Nom du joueur " + (i + 1) + " :");
                TextField textField = new TextField();
                choixJoueurs.getChildren().addAll(label, textField);
                int j = i;
                textField.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                    nomsJoueurs.set(j, newValue1);
                });
            }
            choixJoueurs.getChildren().add(buttonDemarrerPartie);
            getScene().getWindow().sizeToScene();
        });

    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        buttonDemarrerPartie.setOnAction(event -> {
            setListeDesNomsDeJoueurs();
            if (!nomsJoueurs.isEmpty())
                quandLesNomsDesJoueursSontDefinis.onChanged(null);
        });
    }

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs() ; i++) {
            String name = getJoueurParNumero(i-1);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            }
            else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        return getNomsJoueurs().size();
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return getNomsJoueurs().get(playerNumber);
    }

    public Plateau getPlateau() {
        return plateauChoisi;
    }
}
