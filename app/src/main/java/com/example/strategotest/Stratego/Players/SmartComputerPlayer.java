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
 * The computer now works. I have not yet seen a bug in the hard computer, but I just
 * finished it so I did not have a long tim to test.
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
                int backline;
                if(playerNum == 0){
                    backline = 9;
                }
                else{
                    backline = 0;
                }
                int fran = (int)(Math.random() * 10);
                game.sendAction(new StrategoPlaceAction(this,0,backline, fran));
                game.sendAction(new StrategoPlaceAction(this,10,Math.abs(backline-1), fran));
                if(fran == 9 || fran == 0){
                    game.sendAction(new StrategoPlaceAction(this,10,backline, Math.abs(fran-1)));
                }
                else{
                    game.sendAction(new StrategoPlaceAction(this,10,backline, fran+1));
                    game.sendAction(new StrategoPlaceAction(this,10,backline, fran-1));

                }
                game.sendAction(new StrategoRandomPlace(this,playerNum));
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
        //Loop through all the pieces
        for(int row = 0; row < 10; row++){
            for(int col = 0; col< 10; col++){
                if(board[row][col] != null) {
                    if(board[row][col].getValue() != 10 && board[row][col].getValue() != 0) {
                        if (board[row][col].getPlayer() == playerNum) {
                            lookAround(board, row, col);
                        }
                    }
                }
            }
        }
    }


    /**
     * Depending on what is around you add the appropriate action to a arraylist
     * @param board
     * @param r-Piece row we are checking for
     * @param c-piece col we are checking for
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
     * @param p - piece we are comparing our piece to
     * @return - what to do
     * 1 - attack
     * 2 - move
     * 3 - do in the worst case
     * -1 - unperformable
     */
    public int value( Piece p, int v){
        //Regular move
        if(p == null){
            return 0;
        }
        //Impossible to move on water or your piece
        if(p.getPlayer() < 0 || p.getPlayer() == playerNum){
            return -1;
        }
        //enemy piece visible
        if(p.getVisible() == true){
            if(p.getValue() > v  && (p.getValue() != 10 || v == 8)){
                return 1;
            }
            else{
                return 3;
            }

        }
        //attack invisible piece
        if(p.getVisible()== false){
            return 2;
        }
        return  3;
    }

    public int getPlayerID() {
        return playerNum;
    }
}
