import org.junit.jupiter.api.Test;
import source.PieceMaison;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static source.PieceMaison.CHAMBRE;
import static source.PieceMaison.getPieceWhereNomIs;

class PieceMaisonTest {

    @Test
    void TestgetPieceWhereNomIs() {
        PieceMaison piece = CHAMBRE;
        assertEquals(CHAMBRE, getPieceWhereNomIs(piece.getNom()));
    }
}