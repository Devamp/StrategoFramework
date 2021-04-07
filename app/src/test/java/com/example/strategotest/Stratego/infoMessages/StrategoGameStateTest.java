package com.example.strategotest.Stratego.infoMessages;

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

        assertEquals(backUp, gameState);

        gameState.action(6, 9, 5, 9);

        assertNotEquals(backUp, gameState);


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
        Piece practicePiece = redBench.get(1);

        Piece flag = new Piece("Flag", 0, 0);

        assertEquals(practicePiece, flag);
    }

    @Test
    public void setName() {
    }

    @Test
    public void setIcon() {
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
    public void endTurn() {
    }

    @Test
    public void setPhase() {
    }

    @Test
    public void increaseCap() {
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