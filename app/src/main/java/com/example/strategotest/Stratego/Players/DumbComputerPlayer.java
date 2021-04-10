/**
 * DumbComputerPlayer -
 *
 * @Author Devam Patel
 * @Version 4/2/21
 */
package com.example.strategotest.Stratego.Players;

import android.view.inputmethod.CorrectionInfo;

import com.example.strategotest.Stratego.Piece;
import com.example.strategotest.Stratego.actionMessage.PassTurnAction;
import com.example.strategotest.Stratego.actionMessage.StrategoMoveAction;
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

    private boolean[][] usedIndices = new boolean[10][10]; // 2D boolean array to help store used up indices
    StrategoGameState gameState = null;

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

        if (!(info instanceof StrategoGameState)) {
            return;
        }

        boolean shouldPass = true;

        gameState = new StrategoGameState((StrategoGameState) info);
        PassTurnAction pass = new PassTurnAction(this);
        int[] temp;

        if (gameState.getTurn() != playerNum) { //not the computer's turn
            return;

        } else {

            // make sure we are in placement phase
            if (gameState.getPhase() == 0) {

                int piece;
                for (piece = 0; piece < 12; piece++) {

                    if(playerNum == 0){
                        temp = gameState.getRedCharacter();
                    } else {
                        temp = gameState.getBlueCharacter();
                    }

                    if (temp[piece] != 0) {
                        game.sendAction(getPlaceAction(playerNum, piece)); // get and send the place action for each piece to be placed randomly
                        if (piece != 11) {
                            shouldPass = false;
                        }

                        break;
                    }
                }

            } else { // we are in play phase
                game.sendAction(getMoveAction(gameState));
            }

            if (shouldPass) {
                game.sendAction(pass); // end turn after an action
            }
        }
    }

    /**
     * getPlaceAction - this is a helper method which helps the computer place its pieces randomly on their side of the board
     *
     * @param playerID - player id of the computer
     * @param value    - rank of the piece to be placed
     * @return - returns the place action that will be sent to the game
     */
    public StrategoPlaceAction getPlaceAction(int playerID, int value) {
        Random gen = new Random(); // random generator to help with randomize row and column values

        int row = 0; // initially set to 0

        if (playerID == 0) { //computer is on the bottom side of the board (Player 1)
            row = gen.nextInt(10 - 6) + 6; // generate random row value between 6 - 9
        } else if (playerID == 1) { // computer is on the top side of the board (Player 2)
            row = gen.nextInt(4); // generate random row value between 0 - 3
        }

        int col = gen.nextInt(10); // generate random col value between 0 - 9

        // verify that the generated row and column indices aren't already in use
        if (usedIndices[row][col]) { // if same row or column values are found, generate new ones

            // save original row and column values
            int oldRow = row;
            int oldCol = col;

            while (usedIndices[oldRow][oldCol]) { // loop until the loop is broken by finding an empty spot

                // generate new row and column values
                if (playerID == 0) { //computer is on the bottom side of the board (Player 1)
                    row = gen.nextInt(10 - 6) + 6; // generate random row value between 6 - 9
                } else if (playerID == 1) { // computer is on the top side of the board (Player 2)
                    row = gen.nextInt(4); // generate random row value between 0 - 3
                }

                col = gen.nextInt(10);

                if (!usedIndices[row][col]) { //if empty spot is found, break the loop!
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


    /**
     * getMoveAction - this helper method will help the computer decide which piece to move. It will randomly
     * select a valid peice and if it can make a valid move, it will make that move. Movement direction is
     * randomly chosen.
     *
     * @param state
     * @return move action to be send to the game
     */
    public StrategoMoveAction getMoveAction(StrategoGameState state) {
        Random gen = new Random();
        Piece[][] myBoard = state.getBoard();


        int row = gen.nextInt(10); // generate random row value between 0 - 9
        int col = gen.nextInt(10); // generate random col value between 0 - 9

        while (myBoard[row][col] == null || myBoard[row][col].getPlayer() != playerNum) { // loop until a non-null square is found belonging to the computer
            row = gen.nextInt(10);
            col = gen.nextInt(10);
        }

        // HERE WE SHOULD HAVE PIECE THAT ONLY BELONGS TO THE COMPUTER

        while ((myBoard[row][col] != null && myBoard[row][col].getPlayer() == playerNum)) { // while the piece is computer owned and not null

            int picker = gen.nextInt(4) + 1; // pick between 1 - 4 to decide on random move to one of four sides

            // call helper method to see which side is open to move and return a move action
            switch (picker) { // randomly select one move

                case 1: // start with move down one
                    if (checkSurrounding(myBoard, row, col, "Below")) {
                        return new StrategoMoveAction(this, row, col, row + 1, col);
                    } else if (checkSurrounding(myBoard, row, col, "Right")) {
                        return new StrategoMoveAction(this, row, col, row, col + 1);
                    } else if (checkSurrounding(myBoard, row, col, "Left")) {
                        return new StrategoMoveAction(this, row, col, row, col - 1);
                    } else if (checkSurrounding(myBoard, row, col, "Above")) {
                        return new StrategoMoveAction(this, row, col, row - 1, col);
                    }
                    break; // else break the loop

                case 2: //start with move to the right one
                    if (checkSurrounding(myBoard, row, col, "Right")) {
                        return new StrategoMoveAction(this, row, col, row, col + 1);
                    } else if (checkSurrounding(myBoard, row, col, "Below")) {
                        return new StrategoMoveAction(this, row, col, row, col + 1);
                    } else if (checkSurrounding(myBoard, row, col, "Left")) {
                        return new StrategoMoveAction(this, row, col, row, col - 1);
                    } else if (checkSurrounding(myBoard, row, col, "Above")) {
                        return new StrategoMoveAction(this, row, col, row - 1, col);
                    }
                    break; // else break the loop

                case 3: //start with move to the left one
                    if (checkSurrounding(myBoard, row, col, "Left")) {
                        return new StrategoMoveAction(this, row, col, row, col - 1);
                    } else if (checkSurrounding(myBoard, row, col, "Right")) {
                        return new StrategoMoveAction(this, row, col, row, col + 1);
                    } else if (checkSurrounding(myBoard, row, col, "Below")) {
                        return new StrategoMoveAction(this, row, col, row, col - 1);
                    } else if (checkSurrounding(myBoard, row, col, "Above")) {
                        return new StrategoMoveAction(this, row, col, row - 1, col);
                    }
                    break; // else break the loop

                case 4: // start with move up one
                    if (checkSurrounding(myBoard, row, col, "Above")) {
                        return new StrategoMoveAction(this, row, col, row - 1, col);
                    } else if (checkSurrounding(myBoard, row, col, "Right")) {
                        return new StrategoMoveAction(this, row, col, row, col + 1);
                    } else if (checkSurrounding(myBoard, row, col, "Left")) {
                        return new StrategoMoveAction(this, row, col, row, col - 1);
                    } else if (checkSurrounding(myBoard, row, col, "Below")) {
                        return new StrategoMoveAction(this, row, col, row - 1, col);
                    }
                    break; // else break the loop

                default:
                    break; // break the loop
            }

            // the piece was not moveable, we must rerun the loop with new values and find another piece
            row = gen.nextInt(10);
            col = gen.nextInt(10);

            // in case the new generate indices are lakes, empty squares, or enemy pieces
            while (myBoard[row][col] == null || myBoard[row][col].getPlayer() != playerNum) { // loop until a non-null square is found belonging to the computer
                row = gen.nextInt(10);
                col = gen.nextInt(10);
            }

        }

        return null;
    }


    /**
     * checkSurrounding - will check to see if a given piece at the current index can make a valid
     * move to a new location.
     *
     * @param board   - board to make the move on
     * @param fromX   - original row index
     * @param fromY   - original col index
     * @param toWhere - string indication on which direction to move in
     * @return boolean depending on where the new location is moveable or not
     */

    public boolean checkSurrounding(Piece[][] board, int fromX, int fromY, String toWhere) {

        if (board[fromX][fromY].getValue() == 10 || board[fromX][fromY].getValue() == 0) { // if bomb or flag - cannot move
            return false;

        } else if (toWhere.equalsIgnoreCase("Below")) {

            //if(board[fromX+1][fromY].getPlayer() == playerNum){ // piece below is a friendly piece
           //     return false;
            //} else if (board[fromX+1][fromY] != null && board[fromX+1][fromY].)

            if (fromX != 9 && board[fromX + 1][fromY] == null && (fromX+1 >= 0 && fromX < 10)) { // if spot below is empty
                return true;
            }
        } else if (toWhere.equalsIgnoreCase("Right")) {
            if (fromY != 9 && board[fromX][fromY + 1] == null && (fromY+1 >= 0 && fromY < 10)) { // if spot right is empty
                return true;
            }
        } else if (toWhere.equalsIgnoreCase("Left")) {
            if (fromY != 0 && board[fromX][fromY - 1] == null  && (fromY-1 >= 0 && fromY < 10)) { // if spot to left is empty
                return true;
            }

        } else if (toWhere.equalsIgnoreCase("Above")) {
            if (fromX != 0 && board[fromX - 1][fromY] == null && (fromX-1 >= 0 && fromX < 10)) { // if spot above is empty
                return true;
            }
        }

        return false;
    }

    public int getPlayerID() {
        return playerNum;
    }
}
