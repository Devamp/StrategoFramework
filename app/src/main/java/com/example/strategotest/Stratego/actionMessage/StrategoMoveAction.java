package com.example.strategotest.Stratego.actionMessage;

import com.example.strategotest.game.GameFramework.actionMessage.GameAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

/**
 * @author Gareth Rice
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @author Devam Patel
 * @version 04/21
 */
public class StrategoMoveAction extends GameAction {

    private int fromX; // selected row index for original location (move from)
    private int fromY; // selected col index for original location (move from)
    private int toX; // selected row index for new location (move to)
    private int toY; // selected col index for new location (move to)

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public StrategoMoveAction(GamePlayer player, int fromX, int fromY, int toX, int toY) {
        super(player);
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    /**
     * getter method for getting the x-coordinate (row) of the piece's location
     *
     * @return fromX - the selected piece's row location
     */
    public int getFromX() {
        return fromX;
    }

    /**
     * getter method for getting the y-coordinate (column) of the piece's location
     *
     * @return fromY - the selected piece's column location
     */
    public int getFromY() {
        return fromY;
    }

    /**
     * getter method for getting the x-coordinate (row), where the piece wants to move
     *
     * @return toX - the selected piece's row location to be moved to
     */
    public int getToX() {
        return toX;
    }

    /**
     * getter method for getting the y-coordinate (column), where the piece wants to move
     *
     * @return toY - the selected piece's column location to be moved to
     */
    public int getToY() {
        return toY;
    }
}
