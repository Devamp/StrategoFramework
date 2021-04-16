package com.example.strategotest.Stratego;

public class SmartHelper {
    private int row;
   private  int col;
    private int trow;
    private int tcol;
    public SmartHelper(int r,int c,int tr, int tc){
        row = r;
        col = c;
        trow = tr;
        tcol = tr;
    }
    public int getRow(){
        return row;
    }

    public int getCol() {
        return col;
    }
    public int getTrow(){
        return trow;
    }
    public int getTcol(){
        return tcol;
    }


}
