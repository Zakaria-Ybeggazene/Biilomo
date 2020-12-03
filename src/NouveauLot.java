public class NouveauLot extends Consigne {
    private Lot lot;

    public NouveauLot(Lot lot) {
        super();
        this.lot = lot;
    }

    public Lot getLot() {
        return lot;
    }
}
