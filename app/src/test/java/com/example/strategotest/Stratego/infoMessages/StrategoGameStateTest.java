package com.example.strategotest.Stratego.infoMessages;

import android.view.View;

import com.example.strategotest.R;
import com.example.strategotest.Stratego.MainActivity;
import com.example.strategotest.Stratego.Piece;
import com.example.strategotest.Stratego.SpecialPiece;
import com.example.strategotest.Stratego.StrategoLocalGame;
import com.example.strategotest.Stratego.actionMessage.StrategoMoveAction;
import com.example.strategotest.game.GameFramework.actionMessage.EndTurnAction;
import com.example.strategotest.game.GameFramework.actionMessage.MyNameIsAction;
import com.example.strategotest.game.GameFramework.actionMessage.ReadyAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class StrategoGameStateTest {

    public MainActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class).create().resume().get();
    }

    /**
     * @author Gareth Rice
     *
     */
    @Test
    public void playGame(){
        /*
        this was a hacky test for when robolectric wouldn't work
         */
       //First we need a GameState

//        StrategoGameState gameState = new StrategoGameState();
//
//        //Set up all the red and blue pieces non randomly
//        gameState.placeNotRandom(0);
//        gameState.placeNotRandom(1);
//
//        //do a quick test to make sure the blue flag has been placed correctly
//        Piece[][] board = gameState.getBoard();
//        Piece blueFlag = board[3][9]; //this should be the blue flag
//        Piece compBlueFlag = new SpecialPiece("Flag", 0, 1, false);
//
//        assertTrue(blueFlag.equals(compBlueFlag));

        //see if we can make 2 moves in a row
//        gameState.action()



        View view = activity.findViewById(R.id.playGameButton);
        activity.onClick(view);
        //Getting the created game
        StrategoLocalGame localGame = (StrategoLocalGame) activity.getGame();
        //Getting the players
        GamePlayer[] gamePlayers = localGame.getPlayers();
        //Sending the names of the players to the game
        for (GamePlayer gp : gamePlayers) {
            localGame.sendAction(new MyNameIsAction(gp, gp.getClass().toString()));
        }
        //Telling the game everyone is ready
        for (GamePlayer gp : gamePlayers) {
            localGame.sendAction(new ReadyAction(gp));
        }


        GamePlayer red = gamePlayers[0];
        GamePlayer blue = gamePlayers[1];

        //we need to place the pieces
        StrategoGameState toUse = (StrategoGameState) localGame.getGameState();
        toUse.placeNotRandom(0);
        toUse.placeNotRandom(1);

        //Can I make two moves in a row
        localGame.sendAction(new StrategoMoveAction(red, 6, 9, 5, 9));
        localGame.sendAction(new StrategoMoveAction(blue, 5, 9, 4, 9));

        //Setting the expected outcome of the two lines above
        StrategoGameState compare = new StrategoGameState();
        compare.placeNotRandom(0);
        compare.placeNotRandom(1);

        compare.setPiece(5, 9, new Piece("Captain", 5, 0, false));
        compare.removePiece(6, 9);

        //Testing that I couldn't make two moves in a row
        assertTrue("Game States were not equal", toUse.equals(compare));

        //finish playing a game by switching turns
        localGame.sendAction(new EndTurnAction(red));
        localGame.sendAction(new StrategoMoveAction(blue, 3, 8, 4, 8));
        localGame.sendAction(new EndTurnAction(blue));
        localGame.sendAction(new StrategoMoveAction(red, 5, 9, 4, 9));
        localGame.sendAction(new EndTurnAction(red));
        localGame.sendAction(new StrategoMoveAction(blue, 4, 8, 5, 8));
        localGame.sendAction(new EndTurnAction(blue));

        //next move should put piece on flag
        toUse.action(4, 9, 3, 9);
        localGame.sendAction(new StrategoMoveAction(red, 4, 9, 3, 9));
        //the game should now be over. Red (0) should have won
        int whoWon = localGame.getWhoWon();
//        assertEquals(0, whoWon);

    }

    /**
     * @author Gareth Rice
     */
    @Test
    public void saveBackup() {
        StrategoGameState gameState = new StrategoGameState();
        //place all the pieces on the board
        gameState.placeNotRandom(0);
        gameState.placeNotRandom(1);

        gameState.saveBackup();

        StrategoGameState backUp = gameState.getBackup();

        StrategoGameState gameStateCopy = new StrategoGameState(gameState);

        //back up should be equal
        assertTrue(backUp.equals(gameStateCopy));

        //we make a move and create a new copy
        gameState.action(6, 9, 5, 9);
        StrategoGameState gameStateNewCopy = new StrategoGameState(gameState);

        //the backup shouldn't be the same as the gameState since GS has been updated
        assertTrue(!backUp.equals(gameStateNewCopy));

    }

    /**
     * @author Gareth Rice
     */
    @Test
    public void getBackup() {
        StrategoGameState myState = new StrategoGameState();

        //place pieces
        myState.placeNotRandom(0);
        myState.placeNotRandom(1);

        myState.saveBackup();

        //Make a state to hold the backup
        StrategoGameState backup;
        backup = myState.getBackup();

        //Make a copy of the myState field
        StrategoGameState old = new StrategoGameState(myState);

        assertTrue(old.equals(backup));
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

        //Instanced pieces should be the same as ones I instantiated
        assertTrue(practicePiece.equals(practiceRed));
        //checks the last piece in the red list
        assertTrue(lastPiece.equals(practiceLast));
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
        assertEquals(s.setIcon(1, 0), R.drawable.marshr); // test for red player set
        assertEquals(s.setIcon(10, 0),R.drawable.bombr);

        assertEquals(s.setIcon(1, 1), R.drawable.marshb); // test for blue player set
        assertEquals(s.setIcon(10, 1),R.drawable.bombb);


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

        assertEquals(state.getRedCharacter()[0], 1); // character should be at 1 because there is 1 flag piece

        state.increaseCap(0,0); // increase the piece value by 1

        assertEquals(state.getRedCharacter()[0], 2); // now it should equal to 2

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
     * Caden - i did this
     */
    @Test
    public void getId() {
        StrategoGameState state = new StrategoGameState();
        //Make sure the turn is 0 to start
        assertEquals(state.getId(),0);
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