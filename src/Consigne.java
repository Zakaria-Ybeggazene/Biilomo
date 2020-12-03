public abstract class Consigne {
    private static int last_id = 0;

    private int consigneId;

    public Consigne() {
        this.consigneId = last_id++;
    }

    public int getConsigneId() {
        return consigneId;
    }
}
