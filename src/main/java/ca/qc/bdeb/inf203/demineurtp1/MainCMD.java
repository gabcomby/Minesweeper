package ca.qc.bdeb.inf203.demineurtp1;

public class MainCMD {
    public static void main(String[] args) {
        //TODO :
        // 1 : Créer un objet "Démineur"
        // 2 : Afficher l'objet "Démineur"
        // 3 : Prendre les entrées du joueur

        Demineur demineur = new Demineur();
        demineur.demarrerPartie();
        char[][] grilleDemineurAffichage = demineur.getGrilleDemineurAffichage();
        afficherGrille(grilleDemineurAffichage, demineur);
    }

    static public void afficherGrille(char[][] grilleDemineurAffichage, Demineur demineur) {
        for(int i = 0; i<demineur.getHauteurDeGrille(); i++) {
            for(int j = 0; j< demineur.getLargeurDeGrille(); j++) {
                System.out.print(grilleDemineurAffichage[i][j]);
            }
            System.out.println();
        }
    }
}
