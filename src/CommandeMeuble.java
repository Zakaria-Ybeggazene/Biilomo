public class CommandeMeuble extends Consigne {
    private Meuble meuble;

    public CommandeMeuble(Meuble meuble) {
        super();
        this.meuble = meuble;
    }

    public Meuble getMeuble() {
        return meuble;
    }
}
