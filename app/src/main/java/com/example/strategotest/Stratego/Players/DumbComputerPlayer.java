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

    //ArrayList<Coords> usedIndices = new ArrayList<>(); // arraylist to help store the coordinates of the indices that have already been used (i.e placed)
    private boolean[][] usedIndices = new boolean[10][10]; // 2D boolean array to help store used up indices

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

            // make sure we are in placement phase
            if (gameState.getPhase() == 0) {

                for(int piece = 0; piece < 12; piece++){ // loop through each of the 12 types of pieces
                    for(int numberOfPieces = 0; numberOfPieces < gameState.getFilledRedCharacters()[piece]; numberOfPieces++){ // loop to max number of each single piece (Ex. 6 times for 6 bombs)
                        game.sendAction(getPlaceAction(playerNum, piece)); // get and send the place action for each piece to be placed randomly
                    }
                }

                gameState.setPhase(1); // after placement, update the game phase to play phase


            } else if (gameState.getPhase() == 1) { // we are in play phase

                // ATTACKING PHASE CODE // ...
            }

        }
    }

    /**
     *
     * getPlaceAction - this is a helper method which helps the computer place its pieces randomly on their side of the board
     *
     * @param playerID - player id of the computer
     * @param value - rank of the piece to be placed
     * @return - returns the place action that will be sent to the game
     */
    public StrategoPlaceAction getPlaceAction(int playerID, int value){
        Random gen = new Random(); // random generator to help with randomize row and column values

        int row = 0; // initially set to 0

        if(playerID == 0) { //computer is on the bottom side of the board (Player 1)
             row = gen.nextInt(10-6) + 6; // generate random row value between 6 - 9
        } else if ( playerID == 1){ // computer is on the top side of the board (Player 2)
             row = gen.nextInt(4); // generate random row value between 0 - 3
        }

        int col = gen.nextInt(10); // generate random col value between 0 - 9

        // verify that the generated row and column indices aren't already in use
        if(usedIndices[row][col]){ // if same row or column values are found, generate new ones

            // save original row and column values
            int oldRow = row;
            int oldCol = col;

            while(usedIndices[oldRow][oldCol]){ // loop until the loop is broken by finding an empty spot

                // generate new row and column values
                row = gen.nextInt(10-6) + 6;
                col = gen.nextInt(10);

                if(!usedIndices[row][col]){ //if empty spot is found, break the loop!
                    break;

                } else { // otherwise, update old variables and keep generating
                    oldRow = row;
                    oldCol = col;
                }
            }
        }

        usedIndices[row][col] = true; // set the used row and col indices to true
        return new StrategoPlaceAction(this, value, row, col); // return the place action

    }

}
