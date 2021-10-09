package ca.qc.bdeb.inf203.demineurtp1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainJavaFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    Demineur demineur;
    ImageView[][] tabImageView;
    final int hauteurGrille = 15;
    final int largeurGrille = 15;
    Image caseVide = new Image("ferme.png");
    Image drapeau = new Image("drapeau.png");
    Image bombe = new Image("bombe.png");
    Image bombeExplosion = new Image("boom.png");
    Image case0 = new Image("0.png");
    Image case1 = new Image("1.png");
    Image case2 = new Image("2.png");
    Image case3 = new Image("3.png");
    Image case4 = new Image("4.png");
    Image case5 = new Image("5.png");
    Image case6 = new Image("6.png");
    Image case7 = new Image("7.png");
    Image case8 = new Image("8.png");
    GridPane gridPane = new GridPane();
    int nbrDrapeauxSurLaGrille = 0;
    Text nbrDrapeaux = new Text();
    int nbrBombesSurLaGrille;

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
        root.setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER);

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
        root.setBottom(nbrDrapeaux);

        stage.setTitle("Démineur");
        stage.setScene(scene);
        stage.show();

        recommencer.setOnAction((event) -> {
            recommencerPartie();
        });

        nouvellePartie.setOnAction((event) -> {
            nouvellePartie((int) choixNbrBombes.getValue());
        });

        abandonner.setOnAction((event) -> {
            abandonnerPartie();
            rafraichirGrilleVisuelle();
        });

    }

    public void recommencerPartie() {
        demineur.recommencerPartieEnCours();
        genererImgView();
    }

    public void nouvellePartie(int nbrBombes) {
        nbrBombesSurLaGrille = nbrBombes;
        demineur = new Demineur(largeurGrille, hauteurGrille, nbrBombes);
        tabImageView = new ImageView[largeurGrille][hauteurGrille];
        demineur.genererGrille();
        genererImgView();
    }

    public void abandonnerPartie() {
        demineur.setGameOver(true);
    }

    private void genererImgView() {
        for (int i = 0; i < hauteurGrille; i++) {
            for (int j = 0; j < largeurGrille; j++) {
                ImageView imgView = new ImageView();
                imgView.setImage(caseVide);

                int coordonneeColonne = j;
                int coordonneRangee = i;

                tabImageView[i][j] = imgView;
                gridPane.add(imgView, i, j);

                nbrDrapeaux.setText("Bombes : " + (nbrBombesSurLaGrille - nbrDrapeauxSurLaGrille));

                imgView.setOnMouseClicked((event) -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        demineur.faireJouerLeJoueur(coordonneRangee, coordonneeColonne, 'o');
                        rafraichirGrilleVisuelle();
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        demineur.faireJouerLeJoueur(coordonneRangee, coordonneeColonne, 'd');
                        rafraichirGrilleVisuelle();
                        nbrDrapeaux.setText("Bombes : " + (nbrBombesSurLaGrille - nbrDrapeauxSurLaGrille));
                    }
                });
            }
        }
    }

    private void rafraichirGrilleVisuelle() {
        nbrDrapeauxSurLaGrille = 0;
        if (demineur.isGameOver() == true) {
            for (int i = 0; i < largeurGrille; i++) {
                for (int j = 0; j < hauteurGrille; j++) {
                    if (demineur.getGrilleDemineurLogique()[i][j] instanceof Bombes)
                        tabImageView[i][j].setImage(bombe);
                }
            }
        } else {
            for (int i = 0; i < largeurGrille; i++) {
                for (int j = 0; j < hauteurGrille; j++) {
                    if ((demineur.getGrilleDemineurLogique()[i][j]) instanceof Drapeau == true) {
                        tabImageView[i][j].setImage(drapeau);
                        nbrDrapeauxSurLaGrille++;
                    } else if ((demineur.getGrilleDemineurLogique()[i][j]) instanceof Drapeau == false) {
                        if ((demineur.getGrilleDemineurLogique()[i][j]).isEstOuvert() == false) {
                            tabImageView[i][j].setImage(caseVide);
                        } else if ((demineur.getGrilleDemineurLogique()[i][j]).isEstOuvert() == true) {
                            switch ((demineur.getGrilleDemineurLogique()[i][j]).getAffichage()) {
                                case '0':
                                    tabImageView[i][j].setImage(case0);
                                    break;
                                case '1':
                                    tabImageView[i][j].setImage(case1);
                                    break;
                                case '2':
                                    tabImageView[i][j].setImage(case2);
                                    break;
                                case '3':
                                    tabImageView[i][j].setImage(case3);
                                    break;
                                case '4':
                                    tabImageView[i][j].setImage(case4);
                                    break;
                                case '5':
                                    tabImageView[i][j].setImage(case5);
                                    break;
                                case '6':
                                    tabImageView[i][j].setImage(case6);
                                    break;
                                case '7':
                                    tabImageView[i][j].setImage(case7);
                                    break;
                                case '8':
                                    tabImageView[i][j].setImage(case8);
                                    break;
                            }
                        }
                    }
                }
            }
        }

    }
}
