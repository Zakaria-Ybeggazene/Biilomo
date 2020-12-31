import main.Lot;
import main.Rangee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RangeeTest {

    @Test
    void TestindiceRanger() {
        Rangee.setN(5);
        Rangee rangee = new Rangee();
        int[] tableau= rangee.getTabLotId();
        tableau[1]=0;
        Lot lot = new Lot ("Vis", 3, 3,1);
        int result = rangee.indiceRanger(lot);
        assertEquals(2, result);
    }


    @Test
    void TestlotInitial() {
        Rangee.setN(10);
        Rangee rangee = new Rangee();
        Lot lot = new Lot ("Vis", 2, 1,1);
        assertTrue(rangee.lotInitial(lot, 2));
    }


    @Test
    void TestpeutDeplacer(){
        Rangee.setN(5);
        Rangee rangee = new Rangee();
        Lot lot = new Lot ("Vis", 2, 1,1);
        assertFalse(rangee.peutDeplacer(lot, 4));
    }
}