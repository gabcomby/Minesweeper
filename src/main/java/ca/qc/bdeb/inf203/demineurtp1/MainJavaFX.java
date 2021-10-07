package ca.qc.bdeb.inf203.demineurtp1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//TODO :
//Bouton recommencer : reset la grille logique en copiant "grilleLogiqueCopie" dessus
//Bouton abandonner : set gameOver = true et afficher toutes les bombes
//Bouton nouvellePartie : passer en paramètre la taille de la grille (15x15) et le nbr de bombe
//Afficher la grille

public class MainJavaFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 900, 600);
        VBox grandeVbox = new VBox();
        grandeVbox.setSpacing(10);
        grandeVbox.setAlignment(Pos.CENTER);
        HBox titre = new HBox();
        titre.setAlignment(Pos.CENTER);
        HBox menus = new HBox();
        menus.setAlignment(Pos.CENTER);
        menus.setSpacing(300);
        HBox menuBombes = new HBox();
        menuBombes.setSpacing(5);
        root.setTop(grandeVbox);
        grandeVbox.getChildren().addAll(titre, menus, menuBombes);

        Text titreJeu = new Text("Démineur");
        titreJeu.setFont(Font.font(15));
        titre.getChildren().add(titreJeu);
        Button nouvellePartie = new Button("Nouvelle partie");
        Button recommencer = new Button("Recommencer");
        Button abandonner = new Button("Abandonner");
        menus.getChildren().addAll(nouvellePartie, recommencer, abandonner);
        Text mines = new Text("Mines :");
        ComboBox choixNbrBombes = new ComboBox();
        menuBombes.getChildren().addAll(mines, choixNbrBombes);
        choixNbrBombes.getItems().add(1);
        choixNbrBombes.getItems().add(5);
        choixNbrBombes.getItems().add(15);
        choixNbrBombes.getItems().add(30);
        choixNbrBombes.getItems().add(200);

        stage.setTitle("Démineur");
        stage.setScene(scene);
        stage.show();
    }
}
