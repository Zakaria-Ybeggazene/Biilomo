import java.util.ArrayList;

public class Entrepot {
    private static int m; //nombre de rangees
    private static int n; //nombre d'intervalles

    private double tresorerie;

    private ArrayList<ChefEquipe> chefsEquipe = new ArrayList<>();
    private Rangee[] tabRangees = new Rangee[m];

    public Entrepot(double tresorerie) {
        this.tresorerie = tresorerie;
    }

    public static int getM() {
        return m;
    }

    public static int getN() {
        return n;
    }

    public static void setM(int m) {
        Entrepot.m = m;
    }

    public static void setN(int n) {
        Entrepot.n = n;
    }
}
