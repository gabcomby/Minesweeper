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

    //On référence l'objet démineur ici afin de pouvoir y accéder facilement dans toute la classe (mais on ne l'instancie
    //que dans la fonction NouvellePartie
    private Demineur demineur;
    private ImageView[][] tabImageView;
    private final int hauteurGrille = 15;
    private final int largeurGrille = 15;
    //On ouvre toutes les images ici afin de pouvoir les référencer facilement dans la classe
    private Image caseVide = new Image("ferme.png");
    private Image drapeau = new Image("drapeau.png");
    private Image bombe = new Image("bombe.png");
    private Image bombeExplosion = new Image("boom.png");
    private Image case0 = new Image("0.png");
    private Image case1 = new Image("1.png");
    private Image case2 = new Image("2.png");
    private Image case3 = new Image("3.png");
    private Image case4 = new Image("4.png");
    private Image case5 = new Image("5.png");
    private Image case6 = new Image("6.png");
    private Image case7 = new Image("7.png");
    private Image case8 = new Image("8.png");
    private GridPane gridPane = new GridPane();
    private int nbrDrapeauxSurLaGrille = 0;
    private Text nbrDrapeaux = new Text();
    //Différents compteurs afin de faciliter l'affichage de diverses éléments graphiques
    private int nbrBombesSurLaGrille;
    private int coordonneeColonneBombeExplosee;
    private int coordonneeRangeeBombeExplosee;
    private boolean partieAbandonnee;
    //Compte le nbr de coups effectués dans la partie
    private int nbrDeCoups;

    @Override
    public void start(Stage stage) throws Exception {
        //On crée ici un BorderPane qui va contenier plusieurs objets (une VBOX, deux HBOX et un GridPane)
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 900, 600);
        VBox grandeVbox = new VBox();
        grandeVbox.setSpacing(10); //Cette VBOX va contenier plusieurs HBOX
        grandeVbox.setAlignment(Pos.CENTER);
        HBox titre = new HBox(); //Cette HBOX contient le titre du jeu (DÉMINEUR)
        titre.setAlignment(Pos.CENTER);
        HBox menus = new HBox(); //Cette HBOX contient les menus (Nouvelle Partie, Abandonner et Recommencer)
        menus.setAlignment(Pos.CENTER);
        menus.setSpacing(300);
        HBox menuBombes = new HBox(); //Cette HBOX contient le menu pour choisir le nbr de bombes d'une partie
        menuBombes.setSpacing(5);
        root.setTop(grandeVbox);
        grandeVbox.getChildren().addAll(titre, menus, menuBombes);
        root.setCenter(gridPane);
        gridPane.setAlignment(Pos.CENTER);

        Text titreJeu = new Text("Démineur"); //Le titre du jeu
        titreJeu.setFont(Font.font(15));
        titre.getChildren().add(titreJeu);
        //Les 3 boutons des menus
        Button nouvellePartie = new Button("Nouvelle partie");
        Button recommencer = new Button("Recommencer");
        Button abandonner = new Button("Abandonner");
        menus.getChildren().addAll(nouvellePartie, recommencer, abandonner);
        Text mines = new Text("Mines :");
        //La ComboBox qui permet de choisir le nbr de bombes selon une sélection pré-déterminée
        ComboBox choixNbrBombes = new ComboBox();
        menuBombes.getChildren().addAll(mines, choixNbrBombes);
        //Les différents choix de bombes
        choixNbrBombes.getItems().add(1);
        choixNbrBombes.getItems().add(5);
        choixNbrBombes.getItems().add(15);
        choixNbrBombes.getItems().add(30);
        choixNbrBombes.getItems().add(200);
        //Le texte nbrDrapeaux affiche le nbr de bombes théoriquement restantes sur la grille selon le nbr de drapeaux
        root.setBottom(nbrDrapeaux);
        nbrDrapeaux.setFont(Font.font(15));

        stage.setTitle("Démineur");
        stage.setScene(scene);
        stage.show();

        //Définis le comportement du bouton Recommencer
        recommencer.setOnAction((event) -> {
            recommencerPartie();
        });

        //Définis le comportement du bouton NouvellePartie
        nouvellePartie.setOnAction((event) -> {
            //On passe en paramètre le nbr de bombes choisi dans le ComboBox
            nouvellePartie((int) choixNbrBombes.getValue());
        });

        //Définis le comportement du bouton Abandonner
        abandonner.setOnAction((event) -> {
            abandonnerPartie();
            rafraichirGrilleVisuelle();
        });

    }

    /**
     * La fonction recommencerPartie permet de recommencer l'exacte même grille. Pour cela, elle appelle la fonction
     * recommencerPartieEnCours() du démineur, update les IMGVIEW du GridPane et reset l'indicateur du nombre de bombes
     * restantes.
     */
    public void recommencerPartie() {
        //On set partieAbandonnee comme false afin de pouvoir afficher boom.png si le joueur clique sur une bombe
        partieAbandonnee = false;
        //Reset le nbr de coups à 0
        nbrDeCoups = 0;
        demineur.recommencerPartieEnCours();
        genererImgView();
        nbrDrapeaux.setText("Bombes : " + nbrBombesSurLaGrille);
    }

    /**
     * La fonction nouvellePartie génère une nouvelle grille de démineur aléatoirement et génère un nouveau tableau
     * de IMGVIEW.
     *
     * @param nbrBombes Passe en paramètre le nbr de bombes que l'on veut dans la nouvelle partie depuis la valeur
     *                  sélectionnée dans le ComboBox
     */
    public void nouvellePartie(int nbrBombes) {
        //On set partieAbandonnee comme false afin de pouvoir afficher boom.png si le joueur clique sur une bombe
        partieAbandonnee = false;
        //Reset le nbr de coups à 0
        nbrDeCoups = 0;
        nbrBombesSurLaGrille = nbrBombes;
        demineur = new Demineur(largeurGrille, hauteurGrille, nbrBombes);
        tabImageView = new ImageView[largeurGrille][hauteurGrille];
        demineur.genererGrille();
        genererImgView();
    }

    /**
     * La fonction abandonnerPartie set le demineur en gameOver, ce qui va permettre d'afficher toutes les bombes d'un
     * coup lorsque l'on va update les IMGVIEW.
     */
    public void abandonnerPartie() {
        //On set partieAbandonnee comme true pour éviter d'afficher boom.png alors que le joueur n'a cliqué sur aucune bombe
        partieAbandonnee = true;
        //Set le nbr de coups à 100 afin de ne déclencher aucun effet spécial
        nbrDeCoups = 100;
        demineur.setGameOver(true);
        demineur.abandonnerPartieEnCours();
        nbrDrapeaux.setText("Bombes : " + nbrBombesSurLaGrille);
    }

    /**
     * Cette fonction génère les 225 IMGVIEW qui composent la grille de démineur, et leur attribue à chacun un event
     * lié au clic de la souris
     */
    private void genererImgView() {
        for (int i = 0; i < hauteurGrille; i++) {
            for (int j = 0; j < largeurGrille; j++) {
                //Crée un nouveai IMGVIEW et lui set l'image CaseVide par défaut
                ImageView imgView = new ImageView();
                imgView.setImage(caseVide);

                //Garde de note les coordonnées de la case
                int coordonneeColonne = j;
                int coordonneRangee = i;

                //Place le IMGVIEW dans un tableau, car c'est plus simple à manipuler qu'un GridPane
                tabImageView[i][j] = imgView;
                //Ajoute le IMGVIEW dans son conteneur (le GridPane)
                gridPane.add(imgView, i, j);

                //Set le texte du compteur de bombe
                nbrDrapeaux.setText("Bombes : " + (nbrBombesSurLaGrille - nbrDrapeauxSurLaGrille));

                //Déclencheur d'un évènement sur un clic de souris
                imgView.setOnMouseClicked((event) -> {
                    //On s'assure que la partie n'est pas perdue
                    if (demineur.isGameOver() == false) {
                        //Si c'est un clic gauche, on ouvre la case
                        if (event.getButton() == MouseButton.PRIMARY) {
                            nbrDeCoups++;
                            demineur.faireJouerLeJoueur(coordonneRangee, coordonneeColonne, 'o');
                            coordonneeColonneBombeExplosee = coordonneeColonne;
                            coordonneeRangeeBombeExplosee = coordonneRangee;
                            rafraichirGrilleVisuelle();
                        }
                        //Si c'est un clic droit on place un drapeau et on update le compteur de bombe
                        else if (event.getButton() == MouseButton.SECONDARY) {
                            nbrDeCoups++;
                            demineur.faireJouerLeJoueur(coordonneRangee, coordonneeColonne, 'd');
                            rafraichirGrilleVisuelle();
                            nbrDrapeaux.setText("Bombes : " + (nbrBombesSurLaGrille - nbrDrapeauxSurLaGrille));
                        }
                    }
                });
            }
        }
    }

    /**
     * Cette fonction permet d'update l'entièreté de la grille visuelle (le tableau de IMGVIEW) d'un coup.
     */
    private void rafraichirGrilleVisuelle() {
        nbrDrapeauxSurLaGrille = 0;
        //Si le démineur est gameOver, la fonction va afficher toutes les bombes d'un coup
        if (demineur.isGameOver() == true) {
            for (int i = 0; i < largeurGrille; i++) {
                for (int j = 0; j < hauteurGrille; j++) {
                    if (demineur.getGrilleDemineurLogique()[i][j] instanceof Bombes)
                        tabImageView[i][j].setImage(bombe);
                }
            }
            //Si le joueur a vraiment perdu (et pas juste abandonné), on affiche la bombe qui l'a fait perdre avec
            //boom.png plutôt que bombe.png
            if (partieAbandonnee == false)
                tabImageView[coordonneeRangeeBombeExplosee][coordonneeColonneBombeExplosee].setImage(bombeExplosion);
            //Permet d'ouvrir une page YouTube selon le nbr de coups joués ;)
            if(nbrDeCoups <= 5)
                getHostServices().showDocument("https://youtu.be/4Js-XbNj6Tk?t=38");
        } else {
            for (int i = 0; i < largeurGrille; i++) {
                for (int j = 0; j < hauteurGrille; j++) {
                    //Si la case du démineur contient un drapeau, alors l'image est changée pour celle d'un drapeau
                    if ((demineur.getGrilleDemineurLogique()[i][j]) instanceof Drapeau == true) {
                        tabImageView[i][j].setImage(drapeau);
                        nbrDrapeauxSurLaGrille++;
                    } else if ((demineur.getGrilleDemineurLogique()[i][j]) instanceof Drapeau == false) {
                        //Si la case n'a pas été ouverte, l'image est changée pour celle d'une case vide
                        if ((demineur.getGrilleDemineurLogique()[i][j]).isEstOuvert() == false) {
                            tabImageView[i][j].setImage(caseVide);
                        }
                        //Si la case est ouverte, on se fie à l'affichage de l'objet CaseVide pour déterminer l'image
                        //à afficher
                        else if ((demineur.getGrilleDemineurLogique()[i][j]).isEstOuvert() == true) {
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
