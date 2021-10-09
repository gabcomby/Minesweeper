package ca.qc.bdeb.inf203.demineurtp1;

import java.util.Random;
import java.util.Scanner;

public class Demineur {
    private int largeurDeGrille;
    private int hauteurDeGrille;
    private int nbrDeBombes;
    private Tuiles[][] grilleDemineurLogique;
    private Tuiles[][] grilleDemineurLogiqueCopie;
    private boolean gameOver;


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

    protected void genererGrille() {
        Random rnd = new Random();
        for (int i = 0; i < nbrDeBombes; i++) {
            boolean dejaUneBombe = false;
            int hauteurBombe;
            int largeurBombe;
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
        for (int i = 0; i < hauteurDeGrille; i++) {
            for (int j = 0; j < largeurDeGrille; j++) {
                if (grilleDemineurLogique[i][j] == null) {
                    grilleDemineurLogique[i][j] = new CaseVide();
                    grilleDemineurLogiqueCopie[i][j] = new CaseVide();
                }
            }
        }
    }

    protected void faireJouerLeJoueur(int rangée, int colonne, char action) {
        if (grilleDemineurLogique[rangée][colonne].isEstOuvert() == false) {
            switch (action) {
                case 'o': {
                    if (grilleDemineurLogique[rangée][colonne] instanceof Drapeau == false) {
                        if (grilleDemineurLogique[rangée][colonne] instanceof Bombes) {
                            gameOver = true;
                            abandonnerPartieEnCours();
                        } else if (grilleDemineurLogique[rangée][colonne] instanceof CaseVide) {
                            int nbrBombesAdjacentes = compterBombesAdjacentes(rangée, colonne);
                            if (nbrBombesAdjacentes == 0) {
                                ouvrirCasesVidesAdjacentes(rangée, colonne);
                            } else if (nbrBombesAdjacentes > 0) {
                                grilleDemineurLogique[rangée][colonne].setEstOuvert(true);
                                grilleDemineurLogique[rangée][colonne].setAffichage((char) (nbrBombesAdjacentes + 48));
                            }
                        }
                    }
                }
                break;
                case 'd': {
                    if (grilleDemineurLogique[rangée][colonne] instanceof Drapeau == false)
                        grilleDemineurLogique[rangée][colonne] = new Drapeau();
                    else if (grilleDemineurLogique[rangée][colonne] instanceof Drapeau == true) {
                        grilleDemineurLogique[rangée][colonne] = grilleDemineurLogiqueCopie[rangée][colonne];
                        grilleDemineurLogique[rangée][colonne].setEstOuvert(false);
                    }
                }
                break;
            }
        }
    }

    protected int compterBombesAdjacentes(int rangée, int colonne) {
        int compteurBombeAdjacente = 0;
        if (rangée - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= hauteurDeGrille-1 && grilleDemineurLogique[rangée + 1][colonne] instanceof Bombes)
            compteurBombeAdjacente++;
        if (colonne - 1 >= 0 && grilleDemineurLogique[rangée][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (colonne + 1 <= largeurDeGrille-1 && grilleDemineurLogique[rangée][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée - 1 >= 0 && colonne - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée - 1 >= 0 && colonne + 1 <= largeurDeGrille-1 && grilleDemineurLogique[rangée - 1][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= hauteurDeGrille-1 && colonne - 1 >= 0 && grilleDemineurLogique[rangée + 1][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= hauteurDeGrille-1 && colonne + 1 <= largeurDeGrille-1 && grilleDemineurLogique[rangée + 1][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        return compteurBombeAdjacente;
    }

    protected void ouvrirCasesVidesAdjacentes(int rangée, int colonne) {
        final int rangéeConstante = rangée;
        final int colonneConstante = colonne;
        int nbrBombesAdjacentes;
        if (rangée - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante - 1, colonneConstante);
            grilleDemineurLogique[rangée - 1][colonne].setEstOuvert(true);
            grilleDemineurLogique[rangée - 1][colonne].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante - 1, colonneConstante);
        }
        if (rangée + 1 <= hauteurDeGrille-1 && grilleDemineurLogique[rangée + 1][colonne].isEstOuvert() == false) {
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
        if (colonne + 1 <= largeurDeGrille-1 && grilleDemineurLogique[rangée][colonne + 1].isEstOuvert() == false) {
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
        if (rangée - 1 >= 0 && colonne + 1 <= largeurDeGrille-1 && grilleDemineurLogique[rangée - 1][colonne + 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante - 1, colonneConstante + 1);
            grilleDemineurLogique[rangée - 1][colonne + 1].setEstOuvert(true);
            grilleDemineurLogique[rangée - 1][colonne + 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante - 1, colonneConstante + 1);
        }
        if (rangée + 1 <= hauteurDeGrille-1 && colonne - 1 >= 0 && grilleDemineurLogique[rangée + 1][colonne - 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante + 1, colonneConstante - 1);
            grilleDemineurLogique[rangée + 1][colonne - 1].setEstOuvert(true);
            grilleDemineurLogique[rangée + 1][colonne - 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante + 1, colonneConstante - 1);
        }
        if (rangée + 1 <= hauteurDeGrille-1 && colonne + 1 <= largeurDeGrille-1 && grilleDemineurLogique[rangée + 1][colonne + 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante + 1, colonneConstante + 1);
            grilleDemineurLogique[rangée + 1][colonne + 1].setEstOuvert(true);
            grilleDemineurLogique[rangée + 1][colonne + 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if (nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante + 1, colonneConstante + 1);
        }
    }

    protected void recommencerPartieEnCours () {
        for(int i = 0; i<hauteurDeGrille; i++) {
            for(int j = 0; j<largeurDeGrille; j++) {
                grilleDemineurLogique[i][j] = grilleDemineurLogiqueCopie[i][j];
            }
        }
        gameOver = false;
    }

    protected void  abandonnerPartieEnCours () {
        for(int i = 0; i<hauteurDeGrille; i++) {
            for(int j = 0; j<largeurDeGrille; j++) {
                if(grilleDemineurLogiqueCopie[i][j] instanceof Bombes)
                    grilleDemineurLogique[i][j] = grilleDemineurLogiqueCopie[i][j];
            }
        }
    }
}
