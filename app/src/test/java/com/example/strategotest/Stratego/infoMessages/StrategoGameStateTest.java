package com.example.strategotest.Stratego.infoMessages;

import com.example.strategotest.R;
import com.example.strategotest.Stratego.Piece;
import com.example.strategotest.Stratego.SpecialPiece;

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

    /**
     * @author Gareth Rice
     */
    @Test
    public void getBackup() {
        StrategoGameState myState = new StrategoGameState();

        myState.saveBackup();

        //Make a state to hold the backup
        StrategoGameState backup = new StrategoGameState();
        backup = myState.getBackup();

        //Make a copy of the myState field
        StrategoGameState old = new StrategoGameState(myState);

        assertEquals(old, backup);
    }

    @Test
    public void showBoard() {
    }

    /**
     * @author Gareth
     *
     * Makes sure the pieces that will be used to place are instanced correctly
     * Comparison fails, but it says the two values are the same?
     */
    @Test
    public void instancePieces() {
        StrategoGameState gameState = new StrategoGameState();
        //calling constructor should have already called instancePieces

        Piece practicePiece = gameState.redBench.get(0);
        //the first instanced piece of red should be flag with value zero for player 0
        Piece practiceRed = new SpecialPiece("Flag", 0, 0, false);

        Piece lastPiece = gameState.redBench.get(39);
        Piece practiceLast = new Piece("Spy", 11, 0, false);

        //write equals method
        assertEquals(practicePiece, practiceRed);
        assertEquals(lastPiece, practiceLast);
    }

    @Test
    /**
     * Caden-I did this
     */
    public void setName() {
        StrategoGameState s = new StrategoGameState();
        assertEquals(s.setName(1),"Marshall");
        assertEquals(s.setName(10),"Bomb");
    }


    @Test
    /**
     * Caden - I did this
     */
    public void setIcon() {
        StrategoGameState s = new StrategoGameState();
        //Make sure two icons are set properly
        assertEquals(s.setIcon(1, 0),R.drawable.marsh);
        assertEquals(s.setIcon(10, 0),R.drawable.bomb);

    }

    /**
     *
     * Tested by: Devam Patel
     */
    @Test
    public void setInRedCharacter() {
        StrategoGameState state = new StrategoGameState();

        // get original number of flag pieces
        assertEquals(state.getRedCharacter()[0], 1); // should be 1 for 1 flag piece

        state.setInRedCharacter(0,10); // set number of flags to 10

        assertEquals(state.getRedCharacter()[0], 10); // now it should be updated to 10
    }

    /**
     *
     * Tested by: Devam Patel
     */
    @Test
    public void setInBlueCharacter() {
        StrategoGameState state = new StrategoGameState();

        // get original number of flag pieces
        assertEquals(state.getBlueCharacter()[0], 1); // should be 1 for 1 flag piece

        state.setInBlueCharacter(0,10); // set number of flags to 10

        assertEquals(state.getBlueCharacter()[0], 10); // now it should be updated to 10
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


    /**
     * Caden- I did this
     */
    @Test
    public void action() {
        StrategoGameState state = new StrategoGameState();
        //Place the pieces in a non-random fashion
        state.placeNotRandom(0);
        state.placeNotRandom(1);
        //Move to a valid spot
        assertEquals(state.action(6,9,5,9), true);
        //Try to make a diagonal move, this should fail
        assertEquals(state.action(5,9,4,8), false);
        //Make sure the piece did not move
        assertEquals(state.getBoard()[5][9].getValue(),5);
        //Make another valid move
        assertEquals(state.action(5,9,5,8), true);
        //Try to move into a lake this should fail
        assertEquals(state.action(5,8,5,7), false);
        //Make sure piece did not move
        assertEquals(state.getBoard()[5][8].getValue(),5);
        //Move forward again this should work
        assertEquals(state.action(5,8,4,8), true);
        //Move and take a piece
        assertEquals(state.action(4,8,3,8), true);
        //make sure the piece is there
        assertEquals(state.getBoard()[3][8].getValue(),5);
        //try and move opponents piece
        assertEquals(state.action(3,9,4,9), false);
        //Try to move your piece multiple spaces
        assertEquals(state.action(3,8,5,8), false);
        //make sure the piece is still there
        assertEquals(state.getBoard()[3][8].getValue(),5);


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

    /**
     * Tested by: Caden Deutscher
     */
    @Test
    public void setPhase() {
        StrategoGameState state = new StrategoGameState();
        //set phase
        state.setPhase(1);
        //check phase
       assertEquals(state.getPhase(),1);
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

        assertEquals(state.getBlueCharacter()[10], 0); // character should be at 0 because all bombs should be placed
        state.increaseCap(1, 10); // increase the piece value by 1
        assertEquals(state.getBlueCharacter()[10], 1); // now it should be 1


    }

    @Test
    public void movePrint() {
    }

    /**
     * Tested by: Devam Patel
     */
    @Test
    public void getBoard() {
        StrategoGameState state = new StrategoGameState();
        Piece[][] testBoard = state.getBoard(); // get the board from current state


        //default piece at [4][2] should be a lake, i.e value "-1"
        assertEquals(testBoard[4][2].getValue(), -1); // check to see if board has correct value at that position

    }

    /**
     * Tested by: Devam Patel
     *
     */
    @Test
    public void Equals() {
        StrategoGameState stateOne = new StrategoGameState();
        StrategoGameState stateTwo = new StrategoGameState();


    }

    /**
     * Caden - i did this
     */
    @Test
    public void getId() {
        StrategoGameState state = new StrategoGameState();
        //Make sure the turn is 0 to start
        assertEquals(state.getId(),0);
    }

    @Test
    public void getPhase() {

    }

    @Test
    public void testSetPhase() {
    }

    @Test
    /**
     * Hewlett
     *
     * Get the timer in the game state. Check if the getter method is properly
     * getting the time (float number) using the setter method to set the time
     * and test if the timer returns the expected value through the getTimer in assertEquals.
     */
    public void getTimer() {
        StrategoGameState state = new StrategoGameState();
        state.setTimer(600);
        assertEquals(state.getTimer(), 600, 1);
    }

    @Test
    /**
     * Hewlett
     *
     * Sets the timer in the game state. Check if the setter method is properly
     * setting the time (float number) using the setter method to set the time
     * and test if the timer returns the actual value through the getTimer in assertEquals.
     */
    public void setTimer() {
        StrategoGameState state = new StrategoGameState();
        state.setTimer(300);
        assertEquals(300, state.getTimer(), 1);
    }

    @Test
    /**
     * Hewlett
     *
     * Returns the number of Blue pieces for a certain Piece class. Check if getter method
     * is properly returning the number of pieces using the setter method to create an array
     * and test if the array returns the actual value in assertEquals
     */
    public void getBlueCharacter() {
        StrategoGameState state = new StrategoGameState();
        state.setInBlueCharacter(9, 5);
        assertEquals(state.getBlueCharacter()[9], 5);
    }

    @Test
    /**
     * Hewlett
     *
     * Returns the number of Red pieces for a certain Piece class. Check if getter method
     * is properly returning the number of pieces using the setter method to create an array
     * and test if the array returns the actual value in assertEquals
     */
    public void getRedCharacter() {
        StrategoGameState state = new StrategoGameState();
        state.setInRedCharacter(10, 7);
        assertEquals(state.getRedCharacter()[10], 7);
    }


    /**
     * Tested by: Devam Patel
     */
    @Test
    public void getTurn() {
        StrategoGameState state = new StrategoGameState();
        assertEquals(state.getTurn(), 0); // default turn is 0
        state.setTurn(1); // set turn to 1
        assertEquals(state.getTurn(), 1);

    }

    /**
     * Tested by: Devam Patel
     *
     */
    @Test
    public void setTurn() {
        StrategoGameState state = new StrategoGameState();
        state.setTurn(1); // set turn to player 1
        assertEquals(state.getTurn(), 1);
    }

    @Test
    /**
     * Hewlett
     *
     * Creates a new GameState object and checks if method is setting properly
     * using setFilledRedCharacters in assertEquals.
     */
    public void getFilledRedCharacters() {
        StrategoGameState state = new StrategoGameState();
        state.setFilledRedCharacters(5, 6);
        assertEquals(state.getFilledRedCharacters()[5], 6);
    }

    @Test
    /**
     * Hewlett
     *
     * Creates a new GameState object and checks if method is setting properly
     * using getFilledRedCharacters in assertEquals.
     */
    public void setFilledRedCharacters() {
        StrategoGameState state = new StrategoGameState();
        state.setFilledRedCharacters(3, 2);
        assertEquals(state.getFilledRedCharacters()[3], 2);
    }
}