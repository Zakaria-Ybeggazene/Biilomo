public abstract class ChefEquipe extends Personnel {
    private int numOuvriers = 0;
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

    public void addOuvrier(Ouvrier ouvrier) {
        if(numOuvriers < 4) equipe[numOuvriers++] = ouvrier;
        else System.out.println("Nombre maximum d'ouvriers atteint !");
    }

}
