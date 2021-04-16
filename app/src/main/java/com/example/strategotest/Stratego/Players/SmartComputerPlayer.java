package com.example.strategotest.Stratego.Players;

import android.view.View;

import com.example.strategotest.Stratego.Piece;
import com.example.strategotest.Stratego.SmartHelper;
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
    private StrategoGameState theState = null;
    private ArrayList<SmartHelper> moveAttacks = new ArrayList<SmartHelper>();
    private ArrayList<SmartHelper> moves = new ArrayList<SmartHelper>();
    private ArrayList<SmartHelper> worstCase = new ArrayList<SmartHelper>();
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public SmartComputerPlayer(String name) {
        super(name);
    }

    /**
     * @param info the message from the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        if (!(info instanceof StrategoGameState)) {
            return;
        }

        if(theState.getPhase() == 0){
            //Place flag
            int rand = (int)(Math.random()*10);
            int side;
            //Determine which side to place things on
            if(playerNum == 1){
                side = 9;
            }
            else{
                side = 0;
            }
            //Place flag with bombs around flag
            switch(flagBoundaries(rand)){
                case 0:
                    game.sendAction(new StrategoPlaceAction(this, 0 , rand, side ));
                    game.sendAction(new StrategoPlaceAction(this, 10 , rand + 1, side ));
                    game.sendAction(new StrategoPlaceAction(this, 10 , rand - 1, side ));
                    game.sendAction(new StrategoPlaceAction(this, 10 , rand - 1, Math.abs(side-1)));
                    break;
                case 1:
                    game.sendAction(new StrategoPlaceAction(this, 0 , rand, side ));
                    game.sendAction(new StrategoPlaceAction(this, 10 , rand - 1, side ));
                    game.sendAction(new StrategoPlaceAction(this, 10 , rand - 1, Math.abs(side-1)));
                    break;
                case 2:
                    game.sendAction(new StrategoPlaceAction(this, 0 , rand, side ));
                    game.sendAction(new StrategoPlaceAction(this, 10 , rand + 1, side ));
                    game.sendAction(new StrategoPlaceAction(this, 10 , rand - 1, Math.abs(side-1)));
                    break;
            }
            //Place the rest of the pieces
            game.sendAction(new StrategoRandomPlace(this,playerNum));
        }
        else if(theState.getPhase() == 1){
            Piece[][] theBoard = theState.getBoard();
            checkPieces(theBoard);
            if(!moveAttacks.isEmpty()){

                }

            }
            else if(!moves.isEmpty()){

            }
            else if(!worstCase.isEmpty()){

            }
            else{
                ///Game Should have ended
            }


        }



    /**
     * Checks to make sure bombs wont be out of bounds.
     * @param row
     * @return
     * 1 if bomb would be greater
     * 2 of bomb would be less than bounds
     * 0 if bombs fit on both sides
     */
  public int flagBoundaries( int row){
        if(row + 1 > 9){
            return 1;

        }
        if(row - 1 < 0){
            return 2;
        }
        return 0;
  }

    /**
     * Find all of your pieces and  call look around on them
     * @param board
     */
  public void checkPieces(Piece[][] board) {
      for(int row = 0; row < 9; row++){
          for(int col = 0; col< 9; col++){
            if(board[row][col].getPlayer() == playerNum){
                lookAround(board,row,col);
            }
          }
      }
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
          if(p.getValue() > v  && (p.getValue() != 10 || v == 8)){
              return 3;
          }
      }
        if(p.getVisible() == false && p.getPlayer() != playerNum){
            return 2;
        }
      return  -1;
    }






}