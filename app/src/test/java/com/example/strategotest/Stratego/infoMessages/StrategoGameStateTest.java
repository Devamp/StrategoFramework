package com.example.strategotest.Stratego.infoMessages;

import com.example.strategotest.R;
import com.example.strategotest.Stratego.Piece;

import org.junit.Test;

import static org.junit.Assert.*;

public class StrategoGameStateTest {

    @Test
    public void saveBackup() {
    }

    @Test
    public void getBackup() {
    }

    @Test
    public void showBoard() {
    }

    @Test
    public void instancePieces() {
    }

    @Test
    //Caden's Test
    public void setName() {
        StrategoGameState s = new StrategoGameState();
        assertEquals(s.setName(1),"Marshall");
        assertEquals(s.setName(10),"Bomb");
    }

    @Test
    //Caden's Test
    public void setIcon() {
        StrategoGameState s = new StrategoGameState();
        assertEquals(s.setIcon(1),R.drawable.marsh);
        assertEquals(s.setIcon(10),R.drawable.bomb);

    }

    @Test
    public void setInRedCharacter() {
    }

    @Test
    public void setInBlueCharacter() {
    }

    @Test
    public void place() {
    }

    @Test
    public void placeRemove() {
        StrategoGameState state = new StrategoGameState();

        // try place flag on a lake
        assertEquals(state.placeRemove(0, 4, 2), false); // this should be false because we cannot place on lake

        // try to place flag on empty spot
        assertEquals(state.placeRemove(0,4,1), true); // this should be true because flag can be placed at location [4][1]

        // place a piece
        state.placeRemove(1,0,0);
        // try placing another piece at the same spot
        assertEquals(state.placeRemove(1,0,0), true); // should return true because original piece will be taken off the board


    }

    @Test
    public void placeRemoveComputer() {
    }

    @Test
    public void action() {
    }

    @Test
    public void printBoard() {
    }

    @Test
    public void testToString() {
    }

    @Test
    //Caden's Test
    public void endTurn() {
        StrategoGameState s = new StrategoGameState();
        s.endTurn();
        assertEquals(s.getTurn(),1);
    }

    @Test
    public void setPhase() {
    }

    /**
     *
     * Tested by: Devam Patel
     */
    @Test
    public void increaseCap() {

        StrategoGameState state = new StrategoGameState();

        assertEquals(state.getRedCharacter()[0], 0); // character should be at 0 because they should all be placed

        state.increaseCap(0,0); // increase the piece value by 1

        assertEquals(state.getRedCharacter()[0], 1); // now it should equal to 1


    }

    @Test
    public void movePrint() {
    }

    @Test
    public void getBoard() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void getId() {
    }

    @Test
    public void getPhase() {
    }

    @Test
    public void testSetPhase() {
    }

    @Test
    public void getTimer() {
    }

    @Test
    public void setTimer() {
    }

    @Test
    public void getBlueCharacter() {
    }

    @Test
    public void getRedCharacter() {
    }

    @Test
    public void getTurn() {
    }

    @Test
    public void setTurn() {
    }

    @Test
    public void getFilledRedCharacters() {
    }

    @Test
    public void setFilledRedCharacters() {
    }
}