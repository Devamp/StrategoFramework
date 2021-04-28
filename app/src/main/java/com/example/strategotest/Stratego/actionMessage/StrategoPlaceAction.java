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

    /**
     * getter method for getting the value of the piece
     *
     * @return value - the value of the piece
     */
    public int getValue() {
        return value;
    }

    /**
     * getter method for getting the board row
     *
     * @return row - the row number on the board
     */
    public int getRow() {
        return row;
    }

    /**
     * getter method for getting the board column
     *
     * @return col - the col number on the board
     */
    public int getCol() {
        return col;
    }


}
