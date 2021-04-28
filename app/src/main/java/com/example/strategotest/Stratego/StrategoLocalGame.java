package com.example.strategotest.Stratego;

import android.util.Log;

import com.example.strategotest.Stratego.Players.DumbComputerPlayer;
import com.example.strategotest.Stratego.Players.HumanPlayer;
import com.example.strategotest.Stratego.actionMessage.PassTurnAction;
import com.example.strategotest.Stratego.actionMessage.StrategoBackupAction;
import com.example.strategotest.Stratego.actionMessage.StrategoMoveAction;
import com.example.strategotest.Stratego.actionMessage.StrategoPlaceAction;
import com.example.strategotest.Stratego.actionMessage.StrategoRandomPlace;
import com.example.strategotest.Stratego.actionMessage.StrategoUndoTurnAction;
import com.example.strategotest.game.GameFramework.LocalGame;
import com.example.strategotest.game.GameFramework.actionMessage.GameAction;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

/**
 * @author Gareth Rice
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @author Devam Patel
 * @version 04/21
 */
public class StrategoLocalGame extends LocalGame {

    private int whoWon = -1; // used to help store id of the player who won the current game

    /**
     * Constructor for the StrategoLocalGame
     */
    public StrategoLocalGame() {
        super();
        super.state = new StrategoGameState();

    }

    /**
     * Constructor for the StrategoLocalGame with loaded StrategoGameState
     *
     * @param stState
     */
    public StrategoLocalGame(StrategoGameState stState) {
        super();
        super.state = new StrategoGameState(stState);
    }

    /**
     * Notify the given player that its state has changed. This should involve sending
     * a GameInfo object to the player. If the game is not a perfect-information game
     * this method should remove any information from the game that the player is not
     * allowed to know.
     *
     * @param p the player to notify
     */

    /**
     * sendUpdatedStateTo - send updated gamestate to a player
     *
     * @param p
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        p.sendInfo(new StrategoGameState((StrategoGameState) super.state));

    }

    /**
     * canMove -Check if the player can move
     *
     * @param playerIdx the player's player-number (ID)
     * @return
     */
    @Override
    protected boolean canMove(int playerIdx) {
        return playerIdx == ((StrategoGameState) state).getTurn();
    }

    /**
     * checkIfGameOver - check if the game is over and return the appropriate string
     *
     * @return
     */
    @Override
    protected String checkIfGameOver() {

        if (((StrategoGameState) state).getPhase() != 0) { // make sure game phase is not in placement

            //check win by checking if flag has been captured
            if ((((StrategoGameState) state).getBlueCharacter())[0] == 1) { // if flag counter is 1, that means a flag has been captured
                whoWon = 0;
                return "Congratulations! " + playerNames[0] + " has captured the enemy flag and has won the game! "; // blue flag has been captured, so red won the game

            } else if ((((StrategoGameState) state).getRedCharacter())[0] == 1) {
                whoWon = 1;
                return "Congratulations! " + playerNames[1] + " has captured the enemy flag and has won the game! "; // red flag has been captured, so blue won the game
            }

            //check win by checking if either array of pieces has maxed out, indicating all troops have been captured
            boolean redL = true;
            boolean blueL = true;
            //Loop through all the movable pieces
            for (int i = 0; i < 12; i++) {
                if (i > 0 && i < 10 || i == 11) {
                    if (((StrategoGameState) state).getBlueCharacter()[i] != ((StrategoGameState) state).getFilledRedCharacters()[i]) {
                        blueL = false;
                    }
                    if (((StrategoGameState) state).getRedCharacter()[i] != ((StrategoGameState) state).getFilledRedCharacters()[i]) {
                        redL = false;
                    }
                }
            }
            //Return the result
            if (redL) {
                whoWon = 1;
                return ("Blue wins!");
            }
            if (blueL) {
                whoWon = 0;
                return ("Red wins!");
            }
        }
        return null;
    }

    /**
     * so we can see who won the game in testing
     *
     * @return
     */
    public int getWhoWon() {
        return whoWon;
    }

    /**
     * makeMove-make the move based on received action
     *
     * @param action The move that the player has sent to the game
     * @return
     */
    @Override
    protected boolean makeMove(GameAction action) {

        StrategoGameState gameState = (StrategoGameState) super.state;

        //Pass turn to next player
        if (action instanceof PassTurnAction) {
//            officialState.endTurn();
            if (gameState.endTurn()) {
                return true;
            }

            return false;

        } else if (action instanceof StrategoMoveAction) {

            boolean toReturn = false;
            toReturn = gameState.action(((StrategoMoveAction) action).getFromX(),
                    ((StrategoMoveAction) action).getFromY(), ((StrategoMoveAction) action).getToX(),
                    ((StrategoMoveAction) action).getToY());

            //after move is made, see if flag has been captured
            String endGameString = checkIfGameOver();
            if (endGameString != null) {
                //need to somehow see if game is over. Maybe put the checkIfGameOver method in the
                //human player class and pass in StrategoGameState toUse as an argument. Should
                //it also check if all the pieces have been captured, or did we do that in the
                //action method?
            }

            return toReturn;
            //Undo turn
        } else if (action instanceof StrategoUndoTurnAction) {
            super.state = ((StrategoGameState) state).getBackup();
            return true;
            //Back up the board so you can undo turn
        } else if (action instanceof StrategoBackupAction) {
            gameState.saveBackup();
            return true;
            //Place a piece
        } else if (action instanceof StrategoPlaceAction) {
            if (gameState.placeChosenPiece(((StrategoPlaceAction) action).getPlayer(), ((StrategoPlaceAction) action).getValue(), ((StrategoPlaceAction) action).getRow(), ((StrategoPlaceAction) action).getCol())) {
                return true;
            }
            return false;
            //randomly place pieces
        } else if (action instanceof StrategoRandomPlace) {
            return ((StrategoGameState) state).place(((StrategoRandomPlace) action).getPId());
        }

        return false;
    }
}
