public abstract class ChefEquipe extends Personnel {
    private Ouvrier[] equipe = new Ouvrier[4]; // tableau de 4 ouvriers maximum
    //private double salaire; // Ne pourait-ce pas etre un float?

    public ChefEquipe(String nom, String prenom){
        super(nom, prenom);
    }

    /*public double getSalaire(){
        return salaire;
    }*/

}
