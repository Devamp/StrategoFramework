package com.example.strategotest.Stratego;

import com.example.strategotest.Stratego.actionMessage.StrategoMoveAction;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

public class SmartHelper {
  private StrategoMoveAction movement;
  private String sent;
    public SmartHelper(GamePlayer p, int r, int c, int tr, int tc){
        movement = new StrategoMoveAction(p,r,c,tr,tc);
        sent = "\n\n\n\n\n\n\nmove sent: " + c + "," + r + "," + tc + "," + tr + "\n\n\n\n\n";
    }
    public StrategoMoveAction getMove(){
        return movement;
    }
    public String toString(){
        return sent;
    }




}
