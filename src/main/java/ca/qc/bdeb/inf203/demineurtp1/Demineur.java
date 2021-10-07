package ca.qc.bdeb.inf203.demineurtp1;

import java.util.Random;
import java.util.Scanner;

public class Demineur {
    private int largeurDeGrille;
    private int hauteurDeGrille;
    private int nbrDeBombes;
    private char[][] grilleDemineurAffichage;
    private Tuiles[][] grilleDemineurLogique;
    private boolean gameOver;


    public Demineur() {
        this.largeurDeGrille = 10;
        this.hauteurDeGrille = 10;
        this.nbrDeBombes = 10;
        this.gameOver = false;
        this.grilleDemineurLogique = new Tuiles[hauteurDeGrille][largeurDeGrille];
        this.grilleDemineurAffichage = new char[hauteurDeGrille][largeurDeGrille];
    }

    public int getLargeurDeGrille() {
        return largeurDeGrille;
    }

    public void setLargeurDeGrille(int largeurDeGrille) {
        this.largeurDeGrille = largeurDeGrille;
    }

    public int getHauteurDeGrille() {
        return hauteurDeGrille;
    }

    public void setHauteurDeGrille(int hauteurDeGrille) {
        this.hauteurDeGrille = hauteurDeGrille;
    }

    public int getNbrDeBombes() {
        return nbrDeBombes;
    }

    public void setNbrDeBombes(int nbrDeBombes) {
        this.nbrDeBombes = nbrDeBombes;
    }

    public char[][] getGrilleDemineurAffichage() {
        return grilleDemineurAffichage;
    }

    public void setGrilleDemineurAffichage(char[][] grilleDemineur) {
        this.grilleDemineurAffichage = grilleDemineur;
    }

    Scanner clavier = new Scanner(System.in);
    protected void demarrerPartie() {
        genererGrille();
        genererGrilleAffichage();
        afficherGrille();
        partieEnCours();
    }
//DÉPLACER PARTIEENCOURS DANS MAINCMD SI POSSIBLE AFIN DE RESPECTER LE VUE-MODELE-CONTROLEUR
    protected void partieEnCours() {
        int rangée;
        int colonne;
        char action;
        char[] prochaineActionSéparée;
        while (gameOver == false) {
            System.out.println("Pour ouvrir une case, rentrez 'o' suivi des coordonnées (ex : o 4 3)");
            System.out.println("Pour placer un drapeau, rentrez 'd' suivi des coordonnées (ex : d 5 6)");
            System.out.println("Entrez l'action à affectuer : ");
            String prochaineAction = clavier.nextLine();
            prochaineActionSéparée = prochaineAction.toCharArray();
            action = prochaineActionSéparée[0];
            rangée = prochaineActionSéparée[2] - 48;
            colonne = prochaineActionSéparée[4] - 48;
            faireJouerLeJoueur(rangée, colonne, action);
            genererGrilleAffichage();
            afficherGrille();
        }
        System.out.println("GAME OVER : La partie est perdue :(");
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
        }
        for (int i = 0; i < hauteurDeGrille; i++) {
            for (int j = 0; j < largeurDeGrille; j++) {
                if (grilleDemineurLogique[i][j] == null)
                    grilleDemineurLogique[i][j] = new CaseVide();
            }
        }
    }

    protected void genererGrilleAffichage() {
        for (int i = 0; i < hauteurDeGrille; i++) {
            for (int j = 0; j < largeurDeGrille; j++) {
                if (grilleDemineurLogique[i][j] instanceof Bombes) {
                    if (gameOver == true)
                        grilleDemineurAffichage[i][j] = 'x';
                    else
                        grilleDemineurAffichage[i][j] = grilleDemineurLogique[i][j].getAffichage();
                } else {
                    grilleDemineurAffichage[i][j] = grilleDemineurLogique[i][j].getAffichage();
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
                }break;
                case 'd': {
                    if (grilleDemineurLogique[rangée][colonne] instanceof Drapeau == false)
                        grilleDemineurLogique[rangée][colonne] = new Drapeau();
                    else if (grilleDemineurLogique[rangée][colonne] instanceof Drapeau == true)
                        grilleDemineurLogique[rangée][colonne] = new CaseVide();
                }break;
            }
        }
    }

    protected int compterBombesAdjacentes(int rangée, int colonne) {
        int compteurBombeAdjacente = 0;
        if (rangée - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= 9 && grilleDemineurLogique[rangée + 1][colonne] instanceof Bombes)
            compteurBombeAdjacente++;
        if (colonne - 1 >= 0 && grilleDemineurLogique[rangée][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= 9 && grilleDemineurLogique[rangée][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée - 1 >= 0 && colonne - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée - 1 >= 0 && colonne + 1 <= 9 && grilleDemineurLogique[rangée - 1][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= 9 && colonne - 1 >= 0 && grilleDemineurLogique[rangée + 1][colonne - 1] instanceof Bombes)
            compteurBombeAdjacente++;
        if (rangée + 1 <= 9 && colonne + 1 <= 9 && grilleDemineurLogique[rangée + 1][colonne + 1] instanceof Bombes)
            compteurBombeAdjacente++;
        return compteurBombeAdjacente;
    }

    protected void afficherGrille() {
        for(int i = 0; i<largeurDeGrille; i++) {
            for(int j = 0; j< hauteurDeGrille; j++) {
                System.out.print(grilleDemineurAffichage[i][j]);
            }
            System.out.println();
        }
    }

    protected void ouvrirCasesVidesAdjacentes (int rangée, int colonne) {
        final int rangéeConstante = rangée;
        final int colonneConstante = colonne;
        int nbrBombesAdjacentes;
        if (rangée - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante - 1, colonneConstante);
            grilleDemineurLogique[rangée - 1][colonne].setEstOuvert(true);
            grilleDemineurLogique[rangée - 1][colonne].setAffichage((char) (nbrBombesAdjacentes + 48));
            if(nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante - 1, colonneConstante);}
        if (rangée + 1 <= 9 && grilleDemineurLogique[rangée + 1][colonne].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante + 1, colonneConstante);
            grilleDemineurLogique[rangée + 1][colonne].setEstOuvert(true);
            grilleDemineurLogique[rangée + 1][colonne].setAffichage((char) (nbrBombesAdjacentes + 48));
            if(nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante + 1, colonneConstante);}
        if (colonne - 1 >= 0 && grilleDemineurLogique[rangée][colonne - 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante, colonneConstante - 1);
            grilleDemineurLogique[rangée][colonne - 1].setEstOuvert(true);
            grilleDemineurLogique[rangée][colonne - 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if(nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante, colonneConstante - 1);}
        if (colonne + 1 <= 9 && grilleDemineurLogique[rangée][colonne + 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante, colonneConstante + 1);
            grilleDemineurLogique[rangée][colonne + 1].setEstOuvert(true);
            grilleDemineurLogique[rangée][colonne + 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if(nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante, colonneConstante + 1);}
        if (rangée - 1 >= 0 && colonne - 1 >= 0 && grilleDemineurLogique[rangée - 1][colonne - 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante - 1, colonneConstante - 1);
            grilleDemineurLogique[rangée - 1][colonne - 1].setEstOuvert(true);
            grilleDemineurLogique[rangée - 1][colonne - 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if(nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante - 1, colonneConstante - 1);}
        if (rangée - 1 >= 0 && colonne + 1 <= 9 && grilleDemineurLogique[rangée - 1][colonne + 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante - 1, colonneConstante + 1);
            grilleDemineurLogique[rangée - 1][colonne + 1].setEstOuvert(true);
            grilleDemineurLogique[rangée - 1][colonne + 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if(nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante - 1, colonneConstante + 1);}
        if (rangée + 1 <= 9 && colonne - 1 >= 0 && grilleDemineurLogique[rangée + 1][colonne - 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante + 1, colonneConstante - 1);
            grilleDemineurLogique[rangée + 1][colonne - 1].setEstOuvert(true);
            grilleDemineurLogique[rangée + 1][colonne - 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if(nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante + 1, colonneConstante - 1);}
        if (rangée + 1 <= 9 && colonne + 1 <= 9 && grilleDemineurLogique[rangée + 1][colonne + 1].isEstOuvert() == false) {
            nbrBombesAdjacentes = compterBombesAdjacentes(rangéeConstante + 1, colonneConstante + 1);
            grilleDemineurLogique[rangée + 1][colonne + 1].setEstOuvert(true);
            grilleDemineurLogique[rangée + 1][colonne + 1].setAffichage((char) (nbrBombesAdjacentes + 48));
            if(nbrBombesAdjacentes == 0)
                ouvrirCasesVidesAdjacentes(rangéeConstante + 1, colonneConstante + 1);}
    }
}
