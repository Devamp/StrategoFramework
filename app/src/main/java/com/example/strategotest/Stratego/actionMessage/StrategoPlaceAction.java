package com.example.strategotest.Stratego.actionMessage;

import com.example.strategotest.game.GameFramework.actionMessage.GameAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

public class StrategoPlaceAction extends GameAction {

    private int value;
    private int row;
    private int col;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public StrategoPlaceAction(GamePlayer player, int value, int row, int col) {
        super(player);
        this.value = value;
        this.row = row;
        this.col = col;

    }

    public int getValue(){
        return value;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }


}
