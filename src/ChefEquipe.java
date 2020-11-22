public abstract class ChefEquipe extends Personnel {
    private Ouvrier [] equipe; // tableau de 4 ouvriers maximum
    private int salaire; // Ne pourait-ce pas etre un float?

    public ChefEquipe(){}

    public int getSalaire(){
        return salaire;
    }

}
