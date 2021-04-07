package com.example.strategotest.Stratego.infoMessages;

import com.example.strategotest.R;
import com.example.strategotest.Stratego.Piece;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StrategoGameStateTest {



    @Test
    public void saveBackup() {
        StrategoGameState gameState = new StrategoGameState();

        gameState.saveBackup();

        StrategoGameState backUp = gameState.getBackup();

        StrategoGameState gameStateCopy = new StrategoGameState(gameState);

        assertEquals(backUp, gameStateCopy);

        gameState.action(6, 9, 5, 9);
        StrategoGameState gameStateNewCopy = new StrategoGameState(gameState);

        assertNotEquals(backUp, gameStateNewCopy);


    }

    @Test
    public void getBackup() {
        StrategoGameState myState = new StrategoGameState();

        myState.saveBackup();

        StrategoGameState backup = new StrategoGameState();
        backup = myState.getBackup();

        assertEquals(myState, backup);
    }

    @Test
    public void showBoard() {
    }

    @Test
    public void instancePieces() {
        StrategoGameState gameState = new StrategoGameState();
        //calling constructor should have already called instancePieces

//        gameState.instancePieces(0);

        ArrayList<Piece> redBench = gameState.getRedBench();
        Piece practicePiece = gameState.redBench.get(1);
        Piece practiceBlue = gameState.blueBench.get(1);

        assertEquals(practicePiece, practiceBlue);
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
        assertEquals(s.setIcon(1), R.drawable.marsh);
        assertEquals(s.setIcon(10), R.drawable.bomb);

    }

    /**
     *
     * Tested by: Devam Patel
     */
    @Test
    public void setInRedCharacter() {
        StrategoGameState state = new StrategoGameState();

        // get original number of flag pieces
        assertEquals(state.getRedCharacter()[0], 0); // should be 0

        state.setInRedCharacter(0,10); // set number of flags to 10

        assertEquals(state.getRedCharacter()[0], 10); // now it should be updated to 10
    }

    @Test
    public void setInBlueCharacter() {
    }

    @Test
    public void place() {
    }

    /**
     *
     * Tested by: Devam Patel
     */
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

        // place at the same spot again
        assertEquals(state.placeRemove(1,0,0), true); // new piece should now be placed after last one is picked up


    }

    @Test
    public void placeRemoveComputer() {
    }

    @Test
    public void action() {
        StrategoGameState state = new StrategoGameState();
        assertEquals(state.action(6,9,5,9), true);
        assertEquals(state.action(5,9,4,8), false);
        assertEquals(state.getBoard()[5][9].getValue(),5);
        assertEquals(state.action(5,9,5,8), true);
        assertEquals(state.action(5,8,5,7), false);

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

        assertEquals(state.getRedCharacter()[0], 0); // character should be at 0 because the flag should be placed
        state.increaseCap(0,0); // increase the piece value by 1

        assertEquals(state.getRedCharacter()[0], 1); // now it should equal to 1

        assertEquals(state.getBlueCharacter()[10], 0); // character should be at 0 because all bombs should be placed
        state.increaseCap(1, 10); // increase the piece value by 1
        assertEquals(state.getBlueCharacter()[10], 1); // now it should be 1


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