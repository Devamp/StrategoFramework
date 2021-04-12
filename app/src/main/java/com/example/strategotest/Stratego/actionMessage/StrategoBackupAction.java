package com.example.strategotest.Stratego.actionMessage;

import com.example.strategotest.game.GameFramework.actionMessage.GameAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

/**
 * @author Gareth Rice
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @author Devam Patel
 *
 * @version 04/21
 */
public class StrategoBackupAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public StrategoBackupAction(GamePlayer player) {
        super(player);
    }
}
