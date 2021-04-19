package com.example.strategotest.Stratego;

import com.example.strategotest.Stratego.Piece;

/**
 * @author Gareth Rice
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @author Devam Patel
 * @version 3/21
 */


public class SpecialPiece extends Piece {


    public SpecialPiece(String name, int value, int player, boolean wasSeen) {
        super(name, value, player, wasSeen);
    }

    public SpecialPiece(String name, int value, int player, int icon) {
        super(name, value, player, icon);
    }

    /**
     * move-sets movement to false for special pieces
     * @param toPlace
     * @return
     */
    @Override
    public boolean move(Piece toPlace) {
        return false;
    }

    /**
     * attack-sets attack to false for special pieces
     * @param toAttack
     * @return
     */
    @Override
    public boolean attack(Piece toAttack) {
        return false;
    }


}
