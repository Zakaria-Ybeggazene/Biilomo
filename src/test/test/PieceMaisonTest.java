package test;

import main.PieceMaison;
import org.junit.jupiter.api.Test;

import static main.PieceMaison.*;
import static org.junit.jupiter.api.Assertions.*;

class PieceMaisonTest {

    @Test
    void TestgetPieceWhereNomIs() {
        PieceMaison piece = CHAMBRE;
        assertEquals(CHAMBRE, getPieceWhereNomIs(piece.getNom()));
    }
}