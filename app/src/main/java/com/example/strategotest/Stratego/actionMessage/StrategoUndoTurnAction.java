package com.example.strategotest.Stratego.actionMessage;

import com.example.strategotest.game.GameFramework.actionMessage.GameAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

public class StrategoUndoTurnAction  extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public StrategoUndoTurnAction(GamePlayer player) {
        super(player);
    }
}
