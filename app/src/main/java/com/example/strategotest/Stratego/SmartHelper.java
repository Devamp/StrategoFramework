/**
 * @author Caden Deutscher
 * @author Devam Patel
 * @author Gareth Rice
 * @authro Hewlett De Lara
 * This class is designed to help the smart computer
 * by prepackaging possible actions
 */
package com.example.strategotest.Stratego;

import com.example.strategotest.Stratego.actionMessage.StrategoMoveAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

public class SmartHelper {
    private StrategoMoveAction movement;
    private String sent;

    /**
     * Constructor
     *
     * @param p  - player making action
     * @param r  - starting row
     * @param c- starting col
     * @param tr - ending row
     * @param tc - ending col
     */
    public SmartHelper(GamePlayer p, int r, int c, int tr, int tc) {
        movement = new StrategoMoveAction(p, r, c, tr, tc);
        sent = "\n\n\n\n\n\n\nmove sent: " + c + "," + r + "," + tc + "," + tr + "\n\n\n\n\n";
    }

    /**
     * returns the move action
     *
     * @return
     */
    public StrategoMoveAction getMove() {
        return movement;
    }

    /**
     * returns a string of the action for debuging
     *
     * @return
     */
    public String toString() {
        return sent;
    }


}
