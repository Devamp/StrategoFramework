package com.example.strategotest.Stratego;

/**
 * @author Gareth Rice
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @author Devam Patel
 *
 * @version 3/21
 */
public class Piece {

    //player number (0 = red, 1 = blue)
    //Lake = -1
    private int player;

    private String name;

    //if the value is 0, it is a flag
    //if the value is 10, it is a bomb
    //if the value is 11, it is the spy
    private int value;

    private boolean isVisible = true;

    public Piece(String name, int val, int player){
        this.name = name;
        this.value = val;

        this.player = player;
    }

    public String toString(){
        String toReturn;
        if(isVisible){
//            toReturn = "P:" + player + ", N:" + name + ", V:" + value;
            toReturn = " (" + name.substring(0, 2) + ", " + value + ") ";
        }else{
            toReturn = " (INV) ";
        }

        return toReturn;
    }

    public void setVisible(boolean visible){
        isVisible = visible;
    }

    public boolean getVisible(){
        return isVisible;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public void setPlayer(int player){
        this.player = player;
    }

    public int getPlayer(){
        return player;
    }

    /**
     * move gives move checks to make sure a move is legal.
     *
     * @param toPlace
     * @return
     */
    public boolean move(Piece toPlace){
        boolean toReturn = true;
        //prevent null point exception
        if(toPlace == null){
            return true;
        }
        //don't move bomb or flag
        if(this.getValue() == 0 || this.getValue() == 10 || this.getPlayer() < 0){
            return false;
        }
        //Don't move on lake
        if(toPlace.getPlayer() < 0){
            return false;
        }
        //if a piece tries to move onto a space that is occupied by a friendly piece, can't move
        if(this.getPlayer() == toPlace.getPlayer()){
            toReturn = false;
        }
        //If lake cannot move there
        else if(toPlace.getPlayer() == -1){
            toReturn = false;
        }


        return toReturn;
    }

    /**
     * Attack compares values to determine legal attacks.
     *
     * @param toAttack
     * @return
     */
    public boolean attack(Piece toAttack){
        if(toAttack.getValue() == 0){
            return true;
        }
        else if(this.getValue() == 8 && toAttack.getValue() == 10){
            return true;
        }
        else if(toAttack.getValue() == 10){
           return false;
        }
        else if(this.getValue() == 11  && toAttack.getValue() == 1){
            return true;
        }
        else if(this.getValue() < toAttack.getValue()){
            return true;
        }
        else if(this.getValue() > toAttack.getValue()){
           return false;
        }
        else{
            return true;
        }
    }

    /**
     * Determines if two pieces are equal
     *
     * @param p - the piece to check against
     * @return true if the pieces are the same or false if the pieces are not
     */
    public boolean isEqual(Piece p) {
        if(p.getValue() != this.getValue()){
            return false;
        }
        if(p.getPlayer() != this.getPlayer()){
            return false;
        }
        return true;
    }



}

