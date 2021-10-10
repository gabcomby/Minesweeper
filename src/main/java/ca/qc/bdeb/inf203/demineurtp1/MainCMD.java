package ca.qc.bdeb.inf203.demineurtp1;

import java.util.Scanner;

public class MainCMD {
    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        char[] prochaineActionSéparée;
        int rangée;
        int colonne;
        char action;
        //On crée le démineur avec des valeurs prédéterminées
        Demineur demineur = new Demineur(10, 10, 10);
        //Une grille de char qui servira à l'affichage
        char[][] grilleDemineurAffichage = new char[demineur.getHauteurDeGrille()][demineur.getLargeurDeGrille()];

        //On génère le démineur aléatoirement
        demineur.genererGrille();
        //Update la grille de char que l'on affiche
        grilleDemineurAffichage = genererGrilleAffichage(demineur, grilleDemineurAffichage);
        //Affiche la grille de char du démineur
        afficherGrille(grilleDemineurAffichage, demineur);
        //Boucle de jeu du démineur
        while (demineur.isGameOver() == false) {
            //On place un try catch pour éviter les erreurs de tableau
            try {
            System.out.println("Pour ouvrir une case, rentrez 'o' suivi des coordonnées (ex : o 4 3)");
            System.out.println("Pour placer un drapeau, rentrez 'd' suivi des coordonnées (ex : d 5 6)");
            System.out.println("Entrez l'action à affectuer : ");
            //Scanne l'entrée au clavier et enregistre l'action à effectuer et les coordonnées (avec un espace entre chaque)
            String prochaineAction = clavier.nextLine();
            prochaineActionSéparée = prochaineAction.toCharArray();
            action = prochaineActionSéparée[0];
            rangée = prochaineActionSéparée[2] - 48;
            colonne = prochaineActionSéparée[4] - 48;
            demineur.faireJouerLeJoueur(rangée, colonne, action);
            grilleDemineurAffichage = genererGrilleAffichage(demineur, grilleDemineurAffichage);
            afficherGrille(grilleDemineurAffichage, demineur);
            }
            //On catch ArrayOutOfBond au cas ou l'utilisateur entre incorrectement sa consigne
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Oups, il semblerait que vous ayez rentré une instruction non-valide!");
                afficherGrille(grilleDemineurAffichage, demineur);
            }
        }
        //Affiche un texte de défaite si le joueur explose
        System.out.println("Oh non, vous avez explosé sur une bombe!");
    }

    /**
     * Fonction simple qui affiche la grille de char dans la console
     *
     * @param grilleDemineurAffichage La grille de char représentant le démineur
     * @param demineur                L'objet démineur
     */
    static public void afficherGrille(char[][] grilleDemineurAffichage, Demineur demineur) {
        for (int i = 0; i < demineur.getHauteurDeGrille(); i++) {
            for (int j = 0; j < demineur.getLargeurDeGrille(); j++) {
                System.out.print(grilleDemineurAffichage[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Fonction qui update la grille de char après chaque action du joueur
     *
     * @param demineur                L'objet démineur
     * @param grilleDemineurAffichage La grille de char représentant le démineur
     * @return Return la grille de char grilleDemineurAffichage
     */
    static public char[][] genererGrilleAffichage(Demineur demineur, char[][] grilleDemineurAffichage) {
        for (int i = 0; i < demineur.getHauteurDeGrille(); i++) {
            for (int j = 0; j < demineur.getLargeurDeGrille(); j++) {
                //Si la case est une bombe et que la demineur est gameOver, affiche toutes les bombes du jeu avec un X
                if (demineur.getGrilleDemineurLogique()[i][j] instanceof Bombes) {
                    if (demineur.isGameOver() == true)
                        grilleDemineurAffichage[i][j] = 'x';
                        //Si le demineur n'est pas gameOver, affiche la bombe normale
                    else
                        grilleDemineurAffichage[i][j] = demineur.getGrilleDemineurLogique()[i][j].getAffichage();
                }
                //Affiche la case selon le char qui la représente (stocké dans l'objet Tuile lui-même)
                else {
                    grilleDemineurAffichage[i][j] = demineur.getGrilleDemineurLogique()[i][j].getAffichage();
                }
            }
        }
        return grilleDemineurAffichage;
    }
}
