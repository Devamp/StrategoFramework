package com.example.strategotest.Stratego.Players;

import android.view.View;

import com.example.strategotest.Stratego.actionMessage.StrategoPlaceAction;
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
    private boolean placed[][] = new boolean[10][10];
    private int pieces[][] = new int[10][10];

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
        StrategoGameState gameState = new StrategoGameState((StrategoGameState) info);

        //Check to make sure it your turn
        if (gameState.getTurn() != playerNum) {
            return;
        }

        //in placement phase
        if (gameState.getPhase() == 0) {
            initializePlace();
            //int r1 = (int)(Math.random()*3);

            //Place Flag first
            /**
             * flag placement here
             */


            //Place other pieces
            for (int p = 0; p < 11; p++) {
                for (int n = 0; n < gameState.getRedCharacter()[p]; n++) {
                    //Generate random values
                    int rr = (int) (Math.random() * 4) + 6;
                    int rc = (int) (Math.random() * 10);
                    int four = 0;
                    //Loop through to find an empty spot
                    while (placed[rr][rc]) {
                        four++;
                        rr += ((rr + 1) + 6) % 10;
                        if (four == 4) {
                            rc += (rc + 1) % 10;
                        }

                    }
                    //Place spy and 2
                    if (p == 0) {
                        game.sendAction(new StrategoPlaceAction(this, p, rr, rc));
                        game.sendAction(new StrategoPlaceAction(this, p, rr, rc));
                    }
                    //randomly place all other pieces
                    else {
                        game.sendAction(new StrategoPlaceAction(this, p, rr, rc));
                        placed[rr][rc] = true;
                    }
                }
            }
        } else if (gameState.getPhase() == 1) {

        }

    }

    /**
     * Initialize the the place variable to all false;
     * This will check if a spot has been placed already;
     */
    private void initializePlace() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                placed[row][col] = false;
            }
        }
    }

    private boolean isInBound(int r, int c) {
        if (r > 9 || c > 9 || r < 0 || c < 0) {
            return false;
        }
        return true;
    }


}