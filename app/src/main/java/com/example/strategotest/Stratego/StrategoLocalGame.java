package com.example.strategotest.Stratego;

import com.example.strategotest.game.GameFramework.LocalGame;
import com.example.strategotest.game.GameFramework.actionMessage.GameAction;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

public class StrategoLocalGame extends LocalGame {

    /**
     * Constructor for the StrategoLocalGame
     *
     */
    private StrategoGameState gameState;
    public StrategoLocalGame(){
    super();
    super.state = new StrategoGameState();
    gameState = new StrategoGameState();
    }

    /**
     * Constructor for the StrategoLocalGame with loaded StrategoGameState
     * @param stState
     */
    public StrategoLocalGame(StrategoGameState stState){
        super();
        super.state = new StrategoGameState(stState);
    }

    /**
     * Notify the given player that its state has changed. This should involve sending
     * a GameInfo object to the player. If the game is not a perfect-information game
     * this method should remove any information from the game that the player is not
     * allowed to know.
     *
     * @param p
     * 			the player to notify
     */

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    @Override
    protected boolean canMove(int playerIdx) {
        if(playerIdx == gameState.getId() ){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected String checkIfGameOver() {
        return null;
    }

    @Override
    protected boolean makeMove(GameAction action) {
        return false;
    }
}
