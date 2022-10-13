package demineurtp1;

public abstract class Tuiles {
    public Tuiles(char affichage) {
        this.estOuvert = false;
        this.affichage = affichage;
    }

    boolean estOuvert;
    char affichage;

    public boolean isEstOuvert() {
        return estOuvert;
    }

    public void setEstOuvert(boolean estOuvert) {
        this.estOuvert = estOuvert;
    }

    public char getAffichage() {
        return affichage;
    }

    public void setAffichage(char affichage) {
        this.affichage = affichage;
    }
}
