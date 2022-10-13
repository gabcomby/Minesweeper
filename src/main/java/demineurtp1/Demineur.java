package demineurtp1;

import java.util.Random;
import java.util.Scanner;

public class Demineur {
    private int largeurDeGrille;
    private int hauteurDeGrille;
    private int nbrDeBombes;
    //Grille logique du demineur constituée de Tuiles (qui sont soit des cases vides, des bombes ou des drapeaux)
    private Tuiles[][] grilleDemineurLogique;
    //Une copie de la grille du démineur utilisée lorsque l'on recommence une partie ou lorsque l'on enlève un drapeau
    private Tuiles[][] grilleDemineurLogiqueCopie;
    private boolean gameOver;

    //Constructeur de la classe demineur
    public Demineur(int largeurDeGrille, int hauteurDeGrille, int nbrDeBombes) {
        this.largeurDeGrille = largeurDeGrille;
        this.hauteurDeGrille = hauteurDeGrille;
        this.nbrDeBombes = nbrDeBombes;
        this.gameOver = false;
        this.grilleDemineurLogique = new Tuiles[hauteurDeGrille][largeurDeGrille];
        this.grilleDemineurLogiqueCopie = new Tuiles[hauteurDeGrille][largeurDeGrille];
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Tuiles[][] getGrilleDemineurLogique() {
        return grilleDemineurLogique;
    }

    public int getLargeurDeGrille() {
        return largeurDeGrille;
    }

    public int getHauteurDeGrille() {
        return hauteurDeGrille;
    }

    /**
     * Génère une grille de démineur aléatoire avec une taille et un nbr de bombe
     */
    protected void genererGrille() {
        Random rnd = new Random();
        for (int i = 0; i < nbrDeBombes; i++) {
            boolean dejaUneBombe = false;
            int hauteurBombe;
            int largeurBombe;
            //Permet de placer aléatoirement les bombes tout en s'assurant qu'il ne puisse pas y avoir 2 bombes
            //sur la même case
            do {
                hauteurBombe = rnd.nextInt(hauteurDeGrille);
                largeurBombe = rnd.nextInt(largeurDeGrille);
                if (grilleDemineurLogique[hauteurBombe][largeurBombe] instanceof Bombes)
                    dejaUneBombe = true;
                else if (grilleDemineurLogique[hauteurBombe][largeurBombe] instanceof Bombes == false)
                    dejaUneBombe = false;
            } while (dejaUneBombe == true);
            grilleDemineurLogique[hauteurBombe][largeurBombe] = new Bombes();
            grilleDemineurLogiqueCopie[hauteurBombe][largeurBombe] = new Bombes();
        }
        //Après que toutes les bombes soient placées, remplie toutes les autres cases par des cases vides
        for (int i = 0; i < hauteurDeGrille; i++) {
            for (int j = 0; j < largeurDeGrille; j++) {
                if (grilleDemineurLogique[i][j] == null) {
                    grilleDemineurLogique[i][j] = new CaseVide();
                    grilleDemineurLogiqueCopie[i][j] = new CaseVide();
                }
            }
        }
    }

    /**
     * Fonction qui effectue l'action du joueur dans le démineur
     *
     * @param rangée  La coordonnée en Y de la case sélectionnée
     * @param colonne La coordonnée en X de la case sélectionnée
     * @param action  L'action effectuée par le joueur
     */
    protected void faireJouerLeJoueur(int rangée, int colonne, char action) {
        //S'assure que la case dans laquelle le joueur veut effectuer une action n'a pas déjà été ouverte
        if (grilleDemineurLogique[rangée][colonne].isEstOuvert() == false) {
            //Switch selon l'action (o = ouvrir une case, d = placer ou retirer un drapeau)
            switch (action) {
                case 'o': {
                    //S'assure que l'on n'ouvre pas une case sur laquelle se trouve un drapeau
                    if (grilleDemineurLogique[rangée][colonne] instanceof Drapeau == false) {
                        //Si la case est une bombe, c'est game over
                        if (grilleDemineurLogique[rangée][colonne] instanceof Bombes) {
                            gameOver = true;
                            abandonnerPartieEnCours();
                        }
                        //Si la case est une case vide, on l'ouvre
                        else if (grilleDemineurLogique[rangée][colonne] instanceof CaseVide) {
                            //Compte le nbr de bombes adjacentes
                            int nbrBombesAdjacentes = compterBombesAdjacentes(rangée, colonne);
                            //Si il n'y a aucune bombe adjacente, appelle la fonction récursive qui ouvre toutes les
                            //cases vides adjacentes
                            if (nbrBombesAdjacentes == 0) {
                                ouvrirCasesVidesAdjacentes(rangée, colonne);
                            }
                            //Si il y a une ou plus bombe adjacente, set l'affichage de la case au nbr de bombes adjacentes
                            //et set la case a ouverte
                            else if (nbrBombesAdjacentes > 0) {
                                grilleDemineurLogique[rangée][colonne].setEstOuvert(true);
                                grilleDemineurLogique[rangée][colonne].setAffichage((char) (nbrBombesAdjacentes + 48));
                            }
                        }
                    }
                }
                break;
                case 'd': {
                    //Si la cases n'avait pas de drapeau avant, on y place un drapeau
                    if (grilleDemineurLogique[rangée][colonne] instanceof Drapeau == false)
                        grilleDemineurLogique[rangée][colonne] = new Drapeau();
                        //Si la case contenait déjà un drapeau, on le retire et on y copie l'ancien contenu de la case
                        //que l'on obtient de la copie de la grille de démineur créée au début
                    else if (grilleDemineurLogique[rangée][colonne] instanceof Drapeau == true) {
                        grilleDemineurLogique[rangée][colonne] = grilleDemineurLogiqueCopie[rangée][colonne];
                        grilleDemineurLogique[rangée][colonne].setEstOuvert(false);
                    }
                }
                break;
            }
        }
    }

    /**
     * Simple fonction qui compte le nombre de bombes dans les 8 cases entourant une case spécifique
     *
     * @param rangée  La coordonnée en Y de la case
     * @param colonne La coordonnée en X de la case
     * @return Le nombre de bombes dans les 8 cases adjacentes
     */
    protected int compterBombesAdjacentes(int rangée, int colonne) {
        int compteurBombeAdjacente = 0;
        if (rangée - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= hauteurDeGrille - 1 && grilleDemineurLogique[rangée + 1][colonne] instanceof Bombes)
            compteurBombeAdjacente++;
        if (colonne - 1 >= 0 && grilleDemineurLogique[rangée][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (colonne + 1 <= largeurDeGrille - 1 && grilleDemineurLogique[rangée][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée - 1 >= 0 && colonne - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée - 1 >= 0 && colonne + 1 <= largeurDeGrille - 1 && grilleDemineurLogique[rangée - 1][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= hauteurDeGrille - 1 && colonne - 1 >= 0 && grilleDemineurLogique[rangée + 1][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= hauteurDeGrille - 1 && colonne + 1 <= largeurDeGrille - 1 && grilleDemineurLogique[rangée + 1][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        return compteurBombeAdjacente;
    }

    /**
     * Fonction récusrive qui ouvre toutes les cases avec aucune bombe adjacentes qui sont collées
     *
     * @param rangée  Coordonnée en Y de la case intiale
     * @param colonne Coordonnée en X de la case initiale
     */
    protected void ouvrirCasesVidesAdjacentes(int rangée, int colonne) {
        final int rangéeConstante = rangée;
        final int colonneConstante = colonne;
        int nbrBombesAdjacentes;
        //Si la case n'a jamais été ouverte, on compte son nbr de bombe adjacente, et on l'affiche dans la case. Si ce
        //nombre est 0, on réappelle la même fonction (récursivité) pour ouvrir toutes les cases vides adjacentes
        if (rangée - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante - 1, colonneConstante);
            grilleDemineurLogique[rangée - 1][colonne].setEstOuvert(true);
            grilleDemineurLogique[rangée - 1][colonne].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante - 1, colonneConstante);
        }
        if (rangée + 1 <= hauteurDeGrille - 1 && grilleDemineurLogique[rangée + 1][colonne].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante + 1, colonneConstante);
            grilleDemineurLogique[rangée + 1][colonne].setEstOuvert(true);
            grilleDemineurLogique[rangée + 1][colonne].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante + 1, colonneConstante);
        }
        if (colonne - 1 >= 0 && grilleDemineurLogique[rangée][colonne - 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante, colonneConstante - 1);
            grilleDemineurLogique[rangée][colonne - 1].setEstOuvert(true);
            grilleDemineurLogique[rangée][colonne - 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante, colonneConstante - 1);
        }
        if (colonne + 1 <= largeurDeGrille - 1 && grilleDemineurLogique[rangée][colonne + 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante, colonneConstante + 1);
            grilleDemineurLogique[rangée][colonne + 1].setEstOuvert(true);
            grilleDemineurLogique[rangée][colonne + 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante, colonneConstante + 1);
        }
        if (rangée - 1 >= 0 && colonne - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne - 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante - 1, colonneConstante - 1);
            grilleDemineurLogique[rangée - 1][colonne - 1].setEstOuvert(true);
            grilleDemineurLogique[rangée - 1][colonne - 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante - 1, colonneConstante - 1);
        }
        if (rangée - 1 >= 0 && colonne + 1 <= largeurDeGrille - 1 && grilleDemineurLogique[rangée - 1][colonne + 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante - 1, colonneConstante + 1);
            grilleDemineurLogique[rangée - 1][colonne + 1].setEstOuvert(true);
            grilleDemineurLogique[rangée - 1][colonne + 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante - 1, colonneConstante + 1);
        }
        if (rangée + 1 <= hauteurDeGrille - 1 && colonne - 1 >= 0 && grilleDemineurLogique[rangée + 1][colonne - 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante + 1, colonneConstante - 1);
            grilleDemineurLogique[rangée + 1][colonne - 1].setEstOuvert(true);
            grilleDemineurLogique[rangée + 1][colonne - 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante + 1, colonneConstante - 1);
        }
        if (rangée + 1 <= hauteurDeGrille - 1 && colonne + 1 <= largeurDeGrille - 1 && grilleDemineurLogique[rangée + 1][colonne + 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante + 1, colonneConstante + 1);
            grilleDemineurLogique[rangée + 1][colonne + 1].setEstOuvert(true);
            grilleDemineurLogique[rangée + 1][colonne + 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante + 1, colonneConstante + 1);
        }
    }

    /**
     * Fonction qui recommence la même partie en recopiant la grille de base non-changée dans la grille actuelle case
     * par case. Set aussi gameOver à false afin de pouvoir recommencer une partie déjà perdue
     */
    protected void recommencerPartieEnCours() {
        for (int i = 0; i < hauteurDeGrille; i++) {
            for (int j = 0; j < largeurDeGrille; j++) {
                grilleDemineurLogique[i][j] = grilleDemineurLogiqueCopie[i][j];
            }
        }
        gameOver = false;
    }

    /**
     * Fonction qui copie toutes les bombes de la grille originale non-changée dans la grille actuelle du jeu afin de
     * retirer les drapeaux qui pourraient être placés sur une bombe, afin de pouvoir afficher toutes les bombes
     * de la grille.
     */
    protected void abandonnerPartieEnCours() {
        for (int i = 0; i < hauteurDeGrille; i++) {
            for (int j = 0; j < largeurDeGrille; j++) {
                if (grilleDemineurLogiqueCopie[i][j] instanceof Bombes)
                    grilleDemineurLogique[i][j] = grilleDemineurLogiqueCopie[i][j];
            }
        }
    }
}
