package com.example.strategotest.Stratego.actionMessage;

import com.example.strategotest.game.GameFramework.actionMessage.GameAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

public class StrategoRandomPlace extends GameAction {
    private int pId;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public StrategoRandomPlace(GamePlayer player, int id) {
        super(player);
        pId = id;
    }

    public int getPId(){
        return pId;
    }
}
