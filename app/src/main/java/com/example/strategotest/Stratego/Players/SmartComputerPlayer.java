package com.example.strategotest.Stratego.Players;

import android.view.View;

import com.example.strategotest.Stratego.Piece;
import com.example.strategotest.Stratego.actionMessage.StrategoPlaceAction;
import com.example.strategotest.Stratego.actionMessage.StrategoRandomPlace;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.infoMessage.GameInfo;
import com.example.strategotest.game.GameFramework.players.GameComputerPlayer;

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





}