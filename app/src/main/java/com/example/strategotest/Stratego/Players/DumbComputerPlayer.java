/**
 *
 * DumbComputerPlayer -
 *
 * @Author Devam Patel
 * @Version 4/2/21
 */
package com.example.strategotest.Stratego.Players;

import android.view.inputmethod.CorrectionInfo;

import com.example.strategotest.Stratego.actionMessage.StrategoPlaceAction;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.infoMessage.GameInfo;
import com.example.strategotest.game.GameFramework.players.GameComputerPlayer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DumbComputerPlayer extends GameComputerPlayer {

    ArrayList<Coords> usedIndices = new ArrayList<>(); // arraylist to help store the coordinates of the indices that have already been used (i.e placed)

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DumbComputerPlayer(String name) {
        super(name);
    }


    /**
     * Called when the player receives a game-state (or other info) from the
     * game.
     *
     * @param info the message from the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        StrategoGameState gameState = new StrategoGameState((StrategoGameState) info);

        if (gameState.getTurn() != playerNum) { //not the computer's turn
            return;

        } else {

            // if we are in placement phase
            if (gameState.getPhase() == 0) {

                game.sendAction(placeSpecialPiece(playerNum, 0)); // place flag randomly in the front row

                game.sendAction(placeRandomPiece(playerNum, 1)); // place marshall randomly
                game.sendAction(placeRandomPiece(playerNum, 2)); // place general randomly
                game.sendAction(placeRandomPiece(playerNum, 3)); // place colonel 1 randomly
                game.sendAction(placeRandomPiece(playerNum, 3)); // place colonel 2 randomly

                for (int i = 0; i < 3; i++){
                    game.sendAction(placeRandomPiece(playerNum, 4)); // place 3 majors randomly
                }

                for (int i = 0; i < 4; i++){
                    game.sendAction(placeRandomPiece(playerNum, 5)); // place 4 captains randomly
                    game.sendAction(placeRandomPiece(playerNum, 6)); // place 4 lieutenants randomly
                    game.sendAction(placeRandomPiece(playerNum, 7)); // place 4 sergeants randomly
                }

                for (int i = 0; i < 5; i++){
                    game.sendAction(placeRandomPiece(playerNum, 8)); // place 5 miners randomly
                }

                for(int i = 0; i < 8; i++){
                    game.sendAction(placeRandomPiece(playerNum, 9)); // place 8 scouts randomly
                }

                for(int i = 0; i < 6; i++){ // place 6 bombs
                    game.sendAction(placeSpecialPiece(playerNum, 10)); // place 6 bombs randomly in the last row
                }

                game.sendAction(placeRandomPiece(playerNum, 11)); // place spy randomly

            } else if(gameState.getPhase() == 1){

                 // ATTACKING PHASE CODE // ...
            }

        }
    }

    /**
     *
     * placeRandomPiece - this helper method will help the computer place the other (non-special) pieces randomly on its side of the board
     *
     * @param playerID - computers player id
     * @param value - value of the piece to be placed
     * @return returns a place game action which is to be sent to the game
     */
    public StrategoPlaceAction placeRandomPiece(int playerID, int value){
        Random gen = new Random();

        if(playerID == 0 ){

            int row = gen.nextInt(10-6) + 6; // generate random row value between 6 - 9
            int col = gen.nextInt(10); // generate random col value between 0 - 9

            Coords myCoords = new Coords(row, col); // create new coords with current row col values
            for (Coords list : usedIndices) {
                if(list == myCoords){ // check to see if indices arent already in use
                    row = gen.nextInt(10-6) + 6; // generate a new random row value between 6 - 9
                    col = gen.nextInt(10); // generate a new random col value between 0 - 9
                    myCoords = new Coords(row, col); // create new coords object with new row and col values // QUESTION: WILL THE LOOP CHECK THIS NEW COORDS WITH THE PAIR OF COORDS IT ALREADY PASSED OVER ON THE FIRST LOOP?
                }
            }

            StrategoPlaceAction placeAction = new StrategoPlaceAction(this, value, myCoords.getX(), myCoords.getY()); // create the place action to be sent
            usedIndices.add(myCoords); // add the used up indices to the arraylist

            return placeAction; // return action

        } else if (playerID == 1){

            int row = gen.nextInt(4); // generate random row value between 0 - 3
            int col = gen.nextInt(10); // generate random col value between 0 - 9

            Coords myCoords = new Coords(row, col); // create new coords with current row col values
            for (Coords list : usedIndices) {
                if(list == myCoords){ // check to see if indices arent already in use
                    row = gen.nextInt(10-6) + 6; // generate a new random row value between 6 - 9
                    col = gen.nextInt(10); // generate a new random col value between 0 - 9
                    myCoords = new Coords(row, col); // create new coords object with new row and col values // QUESTION: WILL THE LOOP CHECK THIS NEW COORDS WITH THE PAIR OF COORDS IT ALREADY PASSED OVER ON THE FIRST LOOP?
                }
            }

            StrategoPlaceAction placeAction = new StrategoPlaceAction(this, value, myCoords.getX(), myCoords.getY()); // create the place action to be sent
            usedIndices.add(myCoords); // add the used up indices to the arraylist

            return placeAction; // return action

        }

        return null; // error
    }


    /**
     *
     *  Place the bomb and flag pieces randomly according to which side of the board the computer player is on
     *
     * @param playerID - id of the computer player
     * @param value - rank of the piece
     * @return StrategoPlaceAction - returns the place action to be sent to the game
     */
    public StrategoPlaceAction placeSpecialPiece(int playerID, int value) {
        Random gen = new Random(); //random generator to make random calls

        if (playerID == 0) { // if computer is player 1 - place pieces on the bottom half of the board

            if (value == 0) {
                // place the flag randomly in the first row
                int flagRow = 6; // set to 6 because player 1's front row would be row 6
                int flagCol = gen.nextInt(10); // get a random column value from 0 - 9

                StrategoPlaceAction placeFlag = new StrategoPlaceAction(this, 0, flagRow, flagCol);
                usedIndices.add(new Coords(flagRow, flagCol)); // add used indices to storage arraylist

                return placeFlag;

            } else if (value == 10) {

                ArrayList<Integer> colList = new ArrayList<>(); // array list to hold the already placed column index for the bombs

                // place the bombs randomly in the back row
                int bombRow = 9; // default back row for player 1 on the bottom of the screen
                int bombCol = gen.nextInt(10); // get a random column value from 0 - 9

                colList.add(bombCol); // add column index to array list

                for (int idx : colList) { // loop array list
                    if(idx == bombCol){ // check if that column index is already in use
                        bombCol = gen.nextInt(10); // if already in use, then generate a new index
                        colList.add(bombCol); // add that index back into the list to verify it is not used
                    }
                }


                StrategoPlaceAction placeBomb = new StrategoPlaceAction(this, 10, bombRow, bombCol); // create new place action with valid indices
                usedIndices.add(new Coords(bombRow, bombCol)); // add used indices to storage arraylist
                return placeBomb;

            }

        } else if (playerID == 1) { // if computer is player 2 - place pieces on the top half of the board

            if (value == 0) {
                // place the flag randomly in the first row
                int flagRow = 3; // set to 6 because player 1's front row would be row 6
                int flagCol = gen.nextInt(10); // get a random column value from 0 - 9

                StrategoPlaceAction placeFlag = new StrategoPlaceAction(this, 0, flagRow, flagCol);
                usedIndices.add(new Coords(flagRow, flagCol)); // add used indices to storage arraylist

                return placeFlag;

            } else if (value == 10) {

                ArrayList<Integer> colList = new ArrayList<>(); // array list to hold the already placed column index for the bombs

                // place the bombs randomly in the back row
                int bombRow = 0; // default back row for player 1 on the bottom of the screen
                int bombCol = gen.nextInt(10); // get a random column value from 0 - 9

                colList.add(bombCol); // add column index to array list

                for (int idx : colList) { // loop array list
                    if(idx == bombCol){ // check if that column index is already in use
                        bombCol = gen.nextInt(10); // if already in use, then generate a new index
                        colList.add(bombCol); // add that index back into the list to verify it is not used
                    }
                }

                StrategoPlaceAction placeBomb = new StrategoPlaceAction(this, 10, bombRow, bombCol);
                usedIndices.add(new Coords(bombRow, bombCol)); // add used indices to storage arraylist

                return placeBomb;
            }
        }

        return null;
    }
}

/**
 *
 * Coords - Helper class to store used indices on the board
 *
 * @author  Devam Patel
 */
class Coords{
    int x;
    int y;

    public Coords(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
