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

//TODO :
//Bouton recommencer : reset la grille logique en copiant "grilleLogiqueCopie" dessus
//Bouton abandonner : set gameOver = true et afficher toutes les bombes
//Bouton nouvellePartie : passer en paramètre la taille de la grille (15x15) et le nbr de bombe
//Ajouter une fonction pour créer le GridPane de IMGVIEW comme dans le projet de ce matin
    //Dans les events buttonClicked des imgView, mettre la fct faireJouerJoueur et rafraichir la case

public class MainJavaFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    Demineur demineur;
    ImageView [][] tabImageView;
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

        recommencer.setOnAction((event) -> {
            recommencerPartie();
        });

        nouvellePartie.setOnAction((event) -> {
            nouvellePartie(choixNbrBombes.getVisibleRowCount()); //EST-CE LA BONNE MÉTHODE?????
        });

        abandonner.setOnAction((event) -> {
            abandonnerPartie();
        });

    }

    public void recommencerPartie () {
        demineur.recommencerPartieEnCours();
    }

    public void nouvellePartie (int nbrBombes) {
        demineur = new Demineur(largeurGrille, hauteurGrille, nbrBombes);
        tabImageView = new ImageView[largeurGrille][hauteurGrille];
        demineur.genererGrille();
        genererImgView();
        partieEnCours();
    }

    public void abandonnerPartie () {
        demineur.setGameOver(true);
    }

    public void partieEnCours () {
        //demineur.faireJouerLeJoueur();
    }

    private void genererImgView () {
        for(int i = 0; i<hauteurGrille; i++) {
            for(int j = 0; j<largeurGrille; j++) {
                ImageView imgView = new ImageView();
                imgView.setImage(caseVide);

                int coordonneeColonne =j;
                int coordonneRangee = i;

                tabImageView[i][j] = imgView;
                gridPane.add(imgView, i, j);

                imgView.setOnMouseClicked((event) -> {
                    if(event.getButton() == MouseButton.PRIMARY) {
                        //Action d'ouvrir une case
                        demineur.faireJouerLeJoueur(coordonneRangee, coordonneeColonne, 'o');
                    }
                    else if (event.getButton() == MouseButton.SECONDARY) {
                        //Action de placer un drapeau
                        demineur.faireJouerLeJoueur(coordonneRangee, coordonneeColonne, 'd');
                    }
                });
            }
        }
    }

    private void rafraichirGrilleVisuelle() {

        if(demineur.isGameOver() == true) {
            for(int i = 0; i<largeurGrille; i++) {
                for(int j = 0; j<hauteurGrille; j++) {
                    if(demineur.getGrilleDemineurLogique()[i][j] instanceof Bombes)
                        tabImageView[i][j].setImage(bombe);
                }
            }
        }
        else {
            for(int i = 0; i<largeurGrille; i++) {
                for(int j = 0; j<hauteurGrille; j++) {
                    if((demineur.getGrilleDemineurLogique()[i][j]).isEstOuvert() == false)
                        tabImageView[i][j].setImage(caseVide);
                    else if((demineur.getGrilleDemineurLogique()[i][j]).isEstOuvert() == true) {
                        //Insérer un code qui vérifie quelle image mettre dans la case selon le texte dedans
                    }
                }
            }
        }

    }
}

                /*if((demineur.getGrilleDemineurLogique()[i][j]).isEstOuvert() == false)
                        imgView.setImage(caseVide);
                        else if ((demineur.getGrilleDemineurLogique()[i][j]).isEstOuvert() == true) {
                        switch ((demineur.getGrilleDemineurLogique()[i][j]).getAffichage()) {
                        case '0' : imgView.setImage(case0);break;
                        case '1' : imgView.setImage(case1);break;
                        case '2' : imgView.setImage(case2);break;
                        case '3' : imgView.setImage(case3);break;
                        case '4' : imgView.setImage(case4);break;
                        case '5' : imgView.setImage(case5);break;
                        case '6' : imgView.setImage(case6);break;
                        case '7' : imgView.setImage(case7);break;
                        case '8' : imgView.setImage(case8);break;
                        case 'd' : imgView.setImage(drapeau);break;
                        }
                        }*/
