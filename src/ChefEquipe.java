public abstract class ChefEquipe extends Personnel {
    /**
     * Tableau de 4 <code>Ouvriers</code> maximum.
     */
    private Ouvrier[] equipe = new Ouvrier[4];

    public ChefEquipe(String nom, String prenom){
        super(nom, prenom);
    }

    public Ouvrier[] getEquipe() {
        return equipe;
    }

}
