package com.example.strategotest.Stratego.actions;

import com.example.strategotest.game.GameFramework.actionMessage.GameAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

public class StrategoAttackAction extends GameAction {

    private int fromX; // selected row index for original location (move from)
    private int fromY; // selected col index for original location (move from)
    private int toX; // selected row index for new location (move to)
    private int toY; // selected col index for new location (move to)

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public StrategoAttackAction(GamePlayer player, int fromX, int fromY, int toX, int toY) {
        super(player);
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public int getToX() {
        return toX;
    }

    public int getToY(){
        return toY;
    }
}
