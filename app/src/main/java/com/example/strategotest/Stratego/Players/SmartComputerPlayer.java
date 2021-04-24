package com.example.strategotest.Stratego.Players;

import android.view.View;

import com.example.strategotest.Stratego.Piece;
import com.example.strategotest.Stratego.SmartHelper;
import com.example.strategotest.Stratego.actionMessage.PassTurnAction;
import com.example.strategotest.Stratego.actionMessage.StrategoMoveAction;
import com.example.strategotest.Stratego.actionMessage.StrategoPlaceAction;
import com.example.strategotest.Stratego.actionMessage.StrategoRandomPlace;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.infoMessage.GameInfo;
import com.example.strategotest.game.GameFramework.players.GameComputerPlayer;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Gareth Rice
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @author Devam Patel
 * @version 04/21
 * <p>
 * bugs/notes:
 * The hard computer is not functional yet
 */
public class SmartComputerPlayer extends GameComputerPlayer {
    private boolean[][] usedIndices = new boolean[10][10]; // 2D boolean array to help store used up indices
    private ArrayList<SmartHelper> moveAttacks = new ArrayList<SmartHelper>();
    private ArrayList<SmartHelper> moves = new ArrayList<SmartHelper>();
    private ArrayList<SmartHelper> worstCase = new ArrayList<SmartHelper>();
    StrategoGameState gameState = null;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public SmartComputerPlayer(String name) {
        super(name);
    }

    /**
     * Called when the player receives a game-state (or other info) from the
     * game.
     *

    /**
     * @param info the message from the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        if (!(info instanceof StrategoGameState)) {
            return;
        }
        theState = new StrategoGameState((StrategoGameState) info);
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

                    if (playerNum == 0) {
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
                Piece[][] theBoard = gameState.getBoard();
                checkPieces(theBoard);
                //Make Moves
                if(!moveAttacks.isEmpty()){
                    int ran = (int)(Math.random() * moveAttacks.size());
                    int row = moveAttacks.get(ran).getRow();
                    int col = moveAttacks.get(ran).getCol();
                    int trow = moveAttacks.get(ran).getTrow();
                    int tcol = moveAttacks.get(ran).getTcol();
                    game.sendAction(new StrategoMoveAction(this,col,row,tcol, trow));
                }

            else if(!moves.isEmpty()){
                    int ran = (int)(Math.random() * moves.size());
                    int row = moves.get(ran).getRow();
                    int col = moves.get(ran).getCol();
                    int trow = moves.get(ran).getTrow();
                    int tcol = moves.get(ran).getTcol();
                    game.sendAction(new StrategoMoveAction(this,col,row,tcol, trow));

            }
            else if(!worstCase.isEmpty()){
                    int ran = (int)(Math.random() * worstCase.size());
                    int row = worstCase.get(ran).getRow();
                    int col = worstCase.get(ran).getCol();
                    int trow = worstCase.get(ran).getTrow();
                    int tcol = worstCase.get(ran).getTcol();
                    game.sendAction(new StrategoMoveAction(this,col,row,tcol, trow));


            }
            else{
                shouldPass = false;
            }
            moveAttacks.clear();
            moves.clear();
            worstCase.clear();
            }

            if (shouldPass) {
                sleep(0.5);
                game.sendAction(pass); // end turn after an action
            }
        }
    }
    /**
     * Find all of your pieces and  call look around on them
     * @param board
     */
    public void checkPieces(Piece[][] board) {
        for(int row = 0; row < 9; row++){
            for(int col = 0; col< 9; col++){
                if(board[row][col] != null) {
                    if (board[row][col].getPlayer() == playerNum) {
                        lookAround(board, row, col);
                    }
                }
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
     * Depending on what is around you add the appropriate action to a arraylist
     * @param board
     * @param r
     * @param c
     */
    public void lookAround( Piece[][] board, int r, int c){
        if(c < 9) {
            switch (value(board[r][c + 1], board[r][c].getValue())) {
                case 0:
                    moves.add(new SmartHelper(r,c,r,c+1));
                    break;
                case 1:
                    moveAttacks.add(new SmartHelper(r,c,r,c+1));
                    break;
                case 2:
                    if(board[r][c].getValue() >= 5){
                        moveAttacks.add(new SmartHelper(r,c,r,c+1));
                    }
                    else{
                        worstCase.add(new SmartHelper(r,c,r,c+1));
                    }
                    break;
                case 3:
                    worstCase.add(new SmartHelper(r,c,r,c+1));
                    break;
                default:
                    break;
            }
        }
        if(c > 0) {
            switch (value(board[r][c - 1], board[r][c].getValue())) {
                case 0:
                    moves.add(new SmartHelper(r,c,r,c-1));
                    break;
                case 1:
                    moveAttacks.add(new SmartHelper(r,c,r,c-1));
                    break;
                case 2:
                    if(board[r][c].getValue() >= 5){
                        moveAttacks.add(new SmartHelper(r,c,r,c-1));
                    }
                    else{
                        worstCase.add(new SmartHelper(r,c,r,c-1));
                    }
                    break;
                case 3:
                    worstCase.add(new SmartHelper(r,c,r,c-1));
                    break;
                default:
                    break;
            }
        }
        if(r > 0) {
            switch (value(board[r - 1][c], board[r][c].getValue())) {
                case 0:
                    moves.add(new SmartHelper(r-1,c,r,c));
                    break;
                case 1:
                    moveAttacks.add(new SmartHelper(r-1,c,r,c));
                    break;
                case 2:
                    if(board[r][c].getValue() >= 5){
                        moveAttacks.add(new SmartHelper(r-1,c,r,c));
                    }
                    else{
                        worstCase.add(new SmartHelper(r-1,c,r,c));
                    }
                    break;
                case 3:
                    worstCase.add(new SmartHelper(r-1,c,r,c));
                    break;
                default:
                    break;
            }
        }
        if(r < 9) {
            switch (value(board[r + 1][c], board[r][c].getValue())) {
                case 0:
                    moves.add(new SmartHelper(r+1,c,r,c));
                    break;
                case 1:
                    moveAttacks.add(new SmartHelper(r+1,c,r,c));
                    break;
                case 2:
                    if(board[r][c].getValue() >= 5){
                        moveAttacks.add(new SmartHelper(r+1,c,r,c));
                    }
                    else{
                        worstCase.add(new SmartHelper(r+1,c,r,c));
                    }

                    break;
                case 3:
                    worstCase.add(new SmartHelper(r+1,c,r,c));
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * Determines the scenario the spot in question is.
     * @param p
     * @return
     */
    public int value( Piece p, int v){
        if(p == null){
            return 0;
        }
        if(p.getPlayer() < 0){
            return -1;
        }
        if(p.getVisible() == true && p.getPlayer() != playerNum){
            if(p.getValue() > v  && (p.getValue() != 10 || v == 8)){
                return 1;
            }
            if(p.getValue() < v  && (p.getValue() != 10 || v == 8)){
                return 3;
            }
            return 3;
        }
        if(p.getVisible()== false && p.getPlayer() != playerNum){
            return 2;
        }
        return  3;
    }

    public int getPlayerID() {
        return playerNum;
    }
}
