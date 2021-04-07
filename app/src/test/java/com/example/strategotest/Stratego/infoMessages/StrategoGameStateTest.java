package com.example.strategotest.Stratego.infoMessages;

import com.example.strategotest.R;

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