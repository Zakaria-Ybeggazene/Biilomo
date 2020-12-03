public enum PieceMaison {
    CUISINE("Cuisine"),
    CHAMBRE("Chambre"),
    SALLEMANGER("SalleManger"),
    SALON("Salon"),
    SALLEBAIN("SalleBain"),
    WC("WC");

    private final String nom;

    PieceMaison(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}
