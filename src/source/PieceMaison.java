package source;

import java.util.List;
import java.util.Random;

public enum PieceMaison {
    CUISINE("CUISINE"),
    CHAMBRE("CHAMBRE"),
    SALLEMANGER("SALLE A MANGER"),
    SALON("SALON"),
    SALLEBAIN("SALLE DE BAIN"),
    WC("WC");

    //The part below is for choosing a random value of PieceMaison
    private static final List<PieceMaison> VALUES =
            List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    private final String nom;

    PieceMaison(String nom) {
        this.nom = nom;
    }

    public static PieceMaison getPieceWhereNomIs(String nom) throws IllegalArgumentException {
        for (PieceMaison p : values()) {
            if (p.nom.equals(nom)) return p;
        }
        throw new IllegalArgumentException("\u001B[31mPieceMaison inexistante\u001B[0m");
    }

    public static PieceMaison randomPiece() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public String getNom() {
        return nom;
    }
}
