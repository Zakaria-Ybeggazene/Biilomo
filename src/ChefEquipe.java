public abstract class ChefEquipe extends Personnel {
    /**
     * Nombre d'ouvriers associes au chef d'equipe
     */
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

    public int getNumOuvriers() {
        return numOuvriers;
    }

    public void addOuvrier(Ouvrier ouvrier) {
        if(ouvrier != null && numOuvriers < 4) {
            boolean placed = false;
            int i = 0;
            while (!placed && i < 4) {
                if(equipe[i] == null) {
                    equipe[i] = ouvrier;
                    numOuvriers++;
                    placed = true;
                }
                i++;
            }
        }
        else System.out.println("Nombre maximum d'ouvriers atteint !");
    }

    public void removeOuvrier(int position) {
        if(position >= 0 && position < 4) {
            equipe[position] = null;
            numOuvriers--;
        } else System.out.println("ChefEquipe : removeOuvrier(int position) : position >= 4 ou position < 0");
    }
}


