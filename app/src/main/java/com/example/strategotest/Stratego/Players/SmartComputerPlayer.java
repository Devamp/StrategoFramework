package com.example.strategotest.Stratego.Players;

import android.util.Log;
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
    private ArrayList<SmartHelper> moveAttacks = new ArrayList<SmartHelper>();
    private ArrayList<SmartHelper> moves = new ArrayList<SmartHelper>();
    private ArrayList<SmartHelper> worstCase = new ArrayList<SmartHelper>();
    private boolean[][] usedIndices = new boolean[10][10]; // 2D boolean array to help store used up indices

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
        gameState = new StrategoGameState((StrategoGameState) info);
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

            }
            if(shouldPass && gameState.getPhase() == 0){
                game.sendAction(pass);
            }
            if(gameState.getPhase() == 1){ // we are in play phase
                Piece[][] theBoard = gameState.getBoard();
                moveAttacks.clear();
                moves.clear();
                worstCase.clear();
                    checkPieces(theBoard);
                //Make Moves
                if (moveAttacks.size() > 0) {
                    int ran = (int) (Math.random() * moveAttacks.size());
                    game.sendAction(moveAttacks.get(ran).getMove());
                    Log.d(moveAttacks.get(ran).toString(), "attack");
                    moveAttacks.clear();
                    game.sendAction(pass);
                    shouldPass = false;
                } else if (moves.size() > 0) {
                    int ran = (int) (Math.random() * moves.size());
                    game.sendAction(moves.get(ran).getMove());
                    Log.d(moves.get(ran).toString(), "move");
                    moves.clear();
                    game.sendAction(pass);
                    shouldPass = false;

                } else if (worstCase.size() > 0) {
                    int ran = (int) (Math.random() * worstCase.size());
                    game.sendAction(worstCase.get(ran).getMove());
                    Log.d(worstCase.get(ran).toString(), "worst");
                    worstCase.clear();
                    game.sendAction(pass);
                    shouldPass = false;


                }

            }
        }


    }
    /**
     * Find all of your pieces and  call look around on them
     * @param board
     */
    public void checkPieces(Piece[][] board) {
        for(int row = 0; row < 10; row++){
            for(int col = 0; col< 10; col++){
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
        //check rows
      for(int i = -1; i < 2; i++) {
          if ((r + i) <= 9 && (r + i) >= 0 && !(i == 0)) {
              switch (value(board[r + i][c], board[r][c].getValue())) {
                  case -1:
                      break;
                  case 0:
                      moves.add(new SmartHelper(this, r, c, r + i, c));
                      break;
                  case 1:
                      moveAttacks.add(new SmartHelper(this, r, c, r + i, c));
                      break;
                  case 2:
                      if (board[r][c].getValue() >= 5) {
                          moveAttacks.add(new SmartHelper(this, r, c, r + i, c));
                      } else {
                          worstCase.add(new SmartHelper(this, r, c, r + i, c));
                      }

                      break;
                  case 3:
                      worstCase.add(new SmartHelper(this, r, c, r + i, c));
                      break;
              }

          }
      }
      //check cols
          for(int i = -1; i < 2; i++) {
              if ((c + i) <= 9 && (c + i) >= 0 && !(i == 0)) {
                  switch (value(board[r][c+i], board[r][c].getValue())) {
                      case -1:
                          break;
                      case 0:
                          moves.add(new SmartHelper(this, r, c, r, c+i));
                          break;
                      case 1:
                          moveAttacks.add(new SmartHelper(this, r, c, r, c+i));
                          break;
                      case 2:
                          if (board[r][c].getValue() >= 5) {
                              moveAttacks.add(new SmartHelper(this, r, c, r, c+i));
                          } else {
                              worstCase.add(new SmartHelper(this, r, c, r, c+i));
                          }

                          break;
                      case 3:
                          worstCase.add(new SmartHelper(this, r, c, r , c + i));
                          break;
                  }

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
        if(p.getPlayer() < 0 || p.getPlayer() == playerNum){
            return -1;
        }
        if(p.getVisible() == true){
            if(p.getValue() > v  && (p.getValue() != 10 || v == 8)){
                return 1;
            }
            else{
                return 3;
            }

        }
        if(p.getVisible()== false){
            return 2;
        }
        return  3;
    }

    public int getPlayerID() {
        return playerNum;
    }
}
