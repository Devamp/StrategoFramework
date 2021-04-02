package com.example.strategotest.Stratego.Players;

import com.example.strategotest.Stratego.actionMessage.StrategoPlaceAction;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.infoMessage.GameInfo;
import com.example.strategotest.game.GameFramework.players.GameComputerPlayer;

import java.util.Random;

public class DumbComputerPlayer extends GameComputerPlayer {
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
     * @param info
     * 		the message from the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        StrategoGameState gameState = new StrategoGameState((StrategoGameState) info);

       if(gameState.getTurn() != playerNum){ //not the computer's turn
           return ;

       } else {
            Random gen = new Random(); //random generator to make random calls

           //in placement phase
           if(gameState.getPhase() == 0){

               if(playerNum == 0){ //computer is player 1

                   int row = 6; // set to 6 because player 1's front row would be row 6
                   int col = gen.nextInt(10); // get a random column value from 0 - 9

                   StrategoPlaceAction placeAction = new StrategoPlaceAction(this, 0 , row, col);
                   // place the flag randomly in the first row
                   //gameState.placeRemove(0, );

               } else { //computer is player 2

                   //...

               }

           }
       }
    }
}
