package ca.qc.bdeb.inf203.demineurtp1;

import java.util.Scanner;

public class MainCMD {
    public static void main(String[] args) {
        //TODO :
        // MainCMD doit contenier tous les SOUT, la boucle principale et l'affichage gameOver
        Scanner clavier = new Scanner(System.in);
        char[]prochaineActionSéparée;
        int rangée;
        int colonne;
        char action;
        Demineur demineur = new Demineur();
        char[][] grilleDemineurAffichage = new char[demineur.getHauteurDeGrille()][demineur.getLargeurDeGrille()];

        demineur.demarrerPartie();
        grilleDemineurAffichage = genererGrilleAffichage(demineur, grilleDemineurAffichage);
        afficherGrille(grilleDemineurAffichage, demineur);
        while(demineur.isGameOver() == false) {
            System.out.println("Pour ouvrir une case, rentrez 'o' suivi des coordonnées (ex : o 4 3)");
            System.out.println("Pour placer un drapeau, rentrez 'd' suivi des coordonnées (ex : d 5 6)");
            System.out.println("Entrez l'action à affectuer : ");
            String prochaineAction = clavier.nextLine();
            prochaineActionSéparée = prochaineAction.toCharArray();
            action = prochaineActionSéparée[0];
            rangée = prochaineActionSéparée[2] - 48;
            colonne = prochaineActionSéparée[4] - 48;
            demineur.faireJouerLeJoueur(rangée, colonne, action);
            grilleDemineurAffichage = genererGrilleAffichage(demineur, grilleDemineurAffichage);
            afficherGrille(grilleDemineurAffichage, demineur);
        }
    }

    static public void afficherGrille(char[][] grilleDemineurAffichage, Demineur demineur) {
        for(int i = 0; i<demineur.getHauteurDeGrille(); i++) {
            for(int j = 0; j< demineur.getLargeurDeGrille(); j++) {
                System.out.print(grilleDemineurAffichage[i][j]);
            }
            System.out.println();
        }
    }

    static public char[][] genererGrilleAffichage(Demineur demineur, char[][]grilleDemineurAffichage) {
        for (int i = 0; i < demineur.getHauteurDeGrille(); i++) {
            for (int j = 0; j < demineur.getLargeurDeGrille(); j++) {
                if (demineur.getGrilleDemineurLogique()[i][j] instanceof Bombes) {
                    if (demineur.isGameOver() == true)
                        grilleDemineurAffichage[i][j] = 'x';
                    else
                        grilleDemineurAffichage[i][j] = demineur.getGrilleDemineurLogique()[i][j].getAffichage();
                } else {
                    grilleDemineurAffichage[i][j] = demineur.getGrilleDemineurLogique()[i][j].getAffichage();
                }
            }
        }
        return grilleDemineurAffichage;
    }
}
