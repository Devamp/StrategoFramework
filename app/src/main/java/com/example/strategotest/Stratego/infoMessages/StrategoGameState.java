package com.example.strategotest.Stratego.infoMessages;

import com.example.strategotest.Stratego.actions.StrategoMoveAction;
import com.example.strategotest.game.GameFramework.infoMessage.GameState;
import com.example.strategotest.Stratego.Piece;
import com.example.strategotest.Stratego.SpecialPiece;

import java.util.ArrayList;

/**
 *
 * @author Gareth Rice
 * @author Devam Patel
 * @author Caden Deutscher
 * @author Hewlett De Lara
 *
 * @version 3/21
 *
 * Notes:
 *
 */

public class StrategoGameState extends GameState {
    //Number of characters of each color
    //0 - spy, 10 - bomb, 11 - flag
    private int[] blueCharacter;
    private int[] redCharacter;
    //turn indicator // red = 0, blue = 1
    private int turn;
    //Board: -1 = empty space, -2 = impassable space (lake), -3 = invisible character (other army)
    private Piece[][] board;
    //Game Timer
    private float timer;
    //Phase Indicator
    private int phase;

    ArrayList<Piece> redBench = new ArrayList<Piece>();
    ArrayList<Piece> blueBench = new ArrayList<Piece>();

    /**
     * ctor
     *
     */
    public StrategoGameState(){
        //character array for each color
        redCharacter = new int[12];
        blueCharacter = new int[12];

        //Initialize red values
        redCharacter[0] = 1; //flag
        redCharacter[1] = 1; //marshall
        redCharacter[2] = 1; //general
        redCharacter[3] = 2; //colonel
        redCharacter[4] = 3; //major
        redCharacter[5] = 4; //captain
        redCharacter[6] = 4; //Lieutenant
        redCharacter[7] = 4; //sergeant
        redCharacter[8] = 5; //miner
        redCharacter[9] = 8; //scout
        redCharacter[10] = 6; //bomb
        redCharacter[11] = 1; //spy

        //Initialize blue values
        blueCharacter[0] = 1;
        blueCharacter[1] = 1;
        blueCharacter[2] = 1;
        blueCharacter[3] = 2;
        blueCharacter[4] = 3;
        blueCharacter[5] = 4;
        blueCharacter[6] = 4;
        blueCharacter[7] = 4;
        blueCharacter[8] = 5;
        blueCharacter[9] = 8;
        blueCharacter[10] = 6;
        blueCharacter[11] = 1;

        //initialize other values
        turn = 0;

        //initialize board array with null in all empty spots
        board = new Piece[10][10];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                //Make the lakes in the center of the board equal -2. Spaces with -2 can't
                //be crossed/moved into
                if((i == 4 || i == 5) && (j == 2 || j == 3 || j == 6 || j == 7)){
                    board[i][j] = new Piece("Lake",-1,-1);
                }else{
                    board[i][j] = null;
                }
            }
        }

        timer = 0;
        phase = 0; // 0 = placement, 1 = game, 2 = end play

        //when the game is first made, we need to instance pieces for player 1 and player 2
        instancePieces(0);
        instancePieces(1);

        place(0);
        place(1);


    }

    /**
     * copy constructor displays only the half of the pieces that belong to the given character
     *
     * @param original
     */
    public StrategoGameState(StrategoGameState original){

        //make new array for the board
        board = new Piece[10][10];
        turn = original.turn;

        //set the new board state equal to original board state
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                //assign board[i][j] before null check

                //create a deep copy of each piece
                if(original.board[i][j] != null) {
                    //need to create a special piece if it's a bomb or flag
                    if(original.board[i][j].getName().equalsIgnoreCase("Bomb") || original.board[i][j].getName().equalsIgnoreCase("Flag")) {
                        board[i][j] = new SpecialPiece(original.board[i][j].getName(), original.board[i][j].getValue(), original.board[i][j].getPlayer());
                        board[i][j].setVisible(original.board[i][j].getVisible());
                    }
                    //need to create normal piece otherwise
                    else {
                        board[i][j] = new Piece(original.board[i][j].getName(), original.board[i][j].getValue(), original.board[i][j].getPlayer());
                        board[i][j].setVisible(original.board[i][j].getVisible());
                    }
                }else{
                    board[i][j] = null;
                }

                if(board[i][j] != null) {
                    //If it's blue's turn and the piece is red, make red piece invisible
                    if (board[i][j].getPlayer() == 1 && turn == 0) {
                        board[i][j].setVisible(false);
                    }
                    //if it's red's turn and the piece is blue, make blue piece invisible
                    else if (board[i][j].getPlayer() == 0 && turn == 1) {
                        board[i][j].setVisible(false);
                    } else { //assign the rest of the board state
                        board[i][j].setVisible(true);
                    }
                }

            }
        }

        //copy over information (not deep copied)
        timer = original.timer;
        phase = original.phase;

        blueCharacter = new int[12];
        redCharacter = new int[12];

        for(int i = 0; i < blueCharacter.length; i++){
            blueCharacter[i] = original.blueCharacter[i];
            redCharacter[i] = original.redCharacter[i];
        }

        blueBench = original.blueBench;
        redBench = original.redBench;

    }

    /**
     * Add instantiated pieces to array lists for both red and blue
     *
     * @param player
     * @return
     */
    public boolean instancePieces(int player){
        ArrayList<Piece> assign;
        int[] numberPieces;
        String name;

        if(player == 0){
            assign = redBench;
            numberPieces = redCharacter;
        }else{
            assign = blueBench;
            numberPieces = blueCharacter;
        }

        //iterate over the 12 types of pieces
        for(int i = 0; i < 12; i++){
            //need to instance all of the pieces
            name = setName(i);

            //go over the number of each particular piece and add an instanced piece to
            //an array list
            while(numberPieces[i] > 0){
                //if the piece is flag or bomb, create special piece
                if(i == 0 || i == 10){
                    assign.add(new SpecialPiece(name, i, player));
                }else{
                    //we can add conditions to add spy, miner, and scout special pieces
                    assign.add(new Piece(name, i, player));
                }

                numberPieces[i]--;
            }
        }

        return true;
    }

    /**
     * setName sets the name of the piece about to be instantiated
     *
     * @param whichName
     * @return
     */
    public String setName(int whichName){
        String returnName;

        //probably a poor way to get the name? Can use hashtable?
        switch(whichName){
            case 0:
                returnName = "Flag";
                break;
            case 1:
                returnName = "Marshall";
                break;
            case 2:
                returnName = "General";
                break;
            case 3:
                returnName = "Colonel";
                break;
            case 4:
                returnName = "Major";
                break;
            case 5:
                returnName = "Captain";
                break;
            case 6:
                returnName = "Lieutenant";
                break;
            case 7:
                returnName = "Sergeant";
                break;
            case 8:
                returnName = "Miner";
                break;
            case 9:
                returnName = "Scout";
                break;
            case 10:
                returnName = "Bomb";
                break;
            case 11:
                returnName = "Spy";
                break;
            default:
                returnName = "";
                break;
        }

        return returnName;
    }

    /**
     * place: Place the pieces from their respective arrayLists
     *
     * @return
     */

    public boolean place(int player){
        int start;
        int randomIndex;
        ArrayList<Piece> currentArmy;

        //place red pieces on bottom of board
        if(player == 0){
            start = 6;
            currentArmy = redBench;
        } else{
            start = 0;
            currentArmy = blueBench;
        }

        //iterate over the first 4, or last 4 rows depending on blue or red player
        for(int i = start; i < start + 4; i++){
            for(int j = 0; j < board[i].length; j++){

                //place the flag in the last possible place
                if(j == board[i].length - 1 && i == start + 3){
                    board[i][j] = currentArmy.get(0);
                    currentArmy.remove(0);
                }else{
                    board[i][j] = currentArmy.get(1);
                    currentArmy.remove(1);
                }


                /*
                //set the index i j to a random piece from specific players arrayList of
                //instantiated pieces
                randomIndex = (int) (Math.random() * currentArmy.size());

                //set that board index to the random index
                board[i][j] = currentArmy.get(randomIndex);
                //once placed from bench it should be removed as its now on the board
                currentArmy.remove(randomIndex);

                 */

            }
        }

        return true;
    }

    /**
     * placeRemove: places a single piece or removes a single piece
     *
     * @param value - what the piece value is
     * @param row - row to place piece
     * @param col- col to place piece
     * @return returns true if piece is removed or placed, false if failure
     */
    public boolean placeRemove(int value, int row, int col){
        if(phase == 0){
            if(board[row][col] == null){
                String returnName = setName(value);
                //Put piece in that spot
                board[row][col] = new Piece(returnName,value, turn);
                return true;
            }
            else if(board[row][col].getValue() < 0|| board[row][col].getPlayer()  <0 ){
                //don't mess with lake
                return false;

            }
            else{
                //Remove piece if one is already there
                board[row][col] = null;
                return true;
            }
        }
        else{
            return false;
        }

    }

    /**
     * action: Preforms the attack and move methods depending on the situation
     *
     * @param fromX - row of starting piece
     * @param fromY - col of starting piece
     * @param toX - row of where you are placing piece
     * @param toY- col of where you are placing piece
     * @return boolean - true if success, false if failure
     */
    public boolean action(int fromX, int fromY, int toX, int toY) {
        int whoseE = (turn +1)%2;
        boolean success = false;

        if(board[fromX][fromY] == null){
            return false;
        }
        //check to make sure movement is not greater than 1 and not diagonal
        if(Math.abs(fromY-toY) >= 1 && Math.abs(fromX - toX) >= 1) {
            return false;
        }
        //If not 9
        if((Math.abs(fromX - toX) > 1 ||  Math.abs(fromY- toY) > 1) && (board[fromX][fromY].getValue() != 9 || board[toX][toY] != null)){
            return false;
        }

            if (board[fromX][fromY].move(board[toX][toY])) {
                //Move(prevents null pointer exception)
                if(board[toX][toY] == null){
                    board[toX][toY] = new Piece(board[fromX][fromY].getName(), board[fromX][fromY].getValue(), board[fromX][fromY].getPlayer());
                    board[fromX][fromY] = null;

                    success = true;
                }
                //Attack
                else if(board[fromX][fromY].getPlayer() != board[toX][toY].getPlayer() && board[toX][toY].getPlayer() != -1) {
                    //If pieces are the same value
                    if(board[fromX][fromY].getValue() == board[toX][toY].getValue()){
                        //Increase captured by both
                        increaseCap(whoseE,board[toX][toY].getValue());
                        increaseCap(turn,board[fromX][fromY].getValue());
                        board[fromX][fromY] = null;
                        board[toX][toY] = null;
                        return true;
                    }
                    //If piece attacking is successful
                    else if (board[fromX][fromY].attack(board[toX][toY])) {
                        //Increase num captured by attacker
                        increaseCap(whoseE,board[toX][toY].getValue());
                        board[toX][toY] = new Piece(board[fromX][fromY].getName(), board[fromX][fromY].getValue(), board[fromX][fromY].getPlayer());
                        board[fromX][fromY] = null;

                    }
                    //If piece defending is successful
                    else {
                        //Increase num captured by defender
                        increaseCap(turn,board[fromX][fromY].getValue());
                        board[fromX][fromY] = null;
                    }
                    success = true;
                }
                //Move
                else {
                    //Check to make sure 9 only goes through empty spaces
                    if(board[fromX][fromY].getValue() == 9){
                        if(fromX > toX){
                            for(int i = fromX - 1; i >= toX; i--){
                                if(board[i][fromY] != null){
                                    //Invalid
                                    return false;
                                }
                            }

                        }
                        else if(fromY > toY){
                            for(int i = fromY - 1; i >= toY; i--){
                                if(board[fromX][i] != null){
                                    //Invalid
                                    return false;
                                }

                            }

                        }
                        else if(fromY < toY){
                            for(int i = fromY + 1; i <= toY; i++){
                                if(board[fromX][i] != null){
                                    //Invalid
                                    return false;
                                }
                            }
                        }
                        else if(fromX < toX){
                            for(int i = fromX + 1; i <= toX; i++){
                                if(board[i][fromY] != null){
                                    //Invalid
                                    return false;
                                }
                            }


                        }
                    }
                    board[toX][toY] = new Piece(board[fromX][fromY].getName(), board[fromX][fromY].getValue(), board[fromX][fromY].getPlayer());
                    board[fromX][fromY] = null;
                    success = true;
                }
            }
            return success;
    }

    /**
     * printBoard prints the pieces on the board with piece information and visibility
     *
     * @return
     */
    public String printBoard(){
        String boardS = "";
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] != null){
                    boardS += board[i][j];

                } else {
                    boardS += "[null]";
                }
            }
            boardS += "\n";
        }
        return boardS;
    }

    //toString method print current state of the game as a String
    @Override
    public String toString(){
        String finalMessage;

        //print whos turn
        String player;
        if(turn == 0){
            player = "Red";
        } else {
            player = "Blue";
        }

        //print game phase
        String gamePhase;
        if(phase == 0){
            gamePhase = "Placement";
        } else if (phase == 1){
            gamePhase = "Play Phase";
        } else if(phase == 2){
            gamePhase = "End Game";
        } else {
            gamePhase = ""; //error
        }

        //print blueCharacter count
        finalMessage =
                "********** GAME STATE INFO ********** " +
                "\n Player Turn: " + player +
                "\n Game Time: " + timer +
                "\n Current Game Phase: " + gamePhase +
                "\n\n" + "Red Player Characters: " +
                "\n" + "Flag: " + redCharacter[0] +
                "\n" + "Bomb: " + redCharacter[10] +
                "\n" + "Spy: " + redCharacter[11] +
                "\n" + "Scout: " + redCharacter[9] +
                "\n" + "Miner: " + redCharacter[8] +
                "\n" + "Sergeant: " + redCharacter[7] +
                "\n" + "Lieutenant: " + redCharacter[6] +
                "\n" + "Captain: " + redCharacter[5] +
                "\n" + "Major: " + redCharacter[4] +
                "\n" + "Colonel: " + redCharacter[3] +
                "\n" + "General: " + redCharacter[2] +
                "\n" + "Marshall: " + redCharacter[1] +

                "\n\n" + "Blue Player Characters: " +
                "\n" + "Flag: " + blueCharacter[0] +
                "\n" + "Bomb: " + blueCharacter[10] +
                "\n" + "Spy: " + blueCharacter[11] +
                "\n" + "Scout: " + blueCharacter[9] +
                "\n" + "Miner: " + blueCharacter[8] +
                "\n" + "Sergeant: " + blueCharacter[7] +
                "\n" + "Lieutenant: " + blueCharacter[6] +
                "\n" + "Captain: " + blueCharacter[5] +
                "\n" + "Major: " + blueCharacter[4] +
                "\n" + "Colonel: " + blueCharacter[3] +
                "\n" + "General: " + blueCharacter[2] +
                "\n" + "Marshall: " + blueCharacter[1] +
                        "\n";

        return finalMessage;
    }

    public boolean endTurn(StrategoGameState gameState) {
        boolean isTrue = false;
        // Player 1 (represented by 0) ended turn
         if (gameState.turn == 0) {
             gameState.turn = 1;
             isTrue = true;
         }
         // Player 2 (represented by 1) ended turn
         else if (gameState.turn == 1){
             gameState.turn = 0;
             isTrue = true;
        }
         // Handle Errors
         // Return false, so the action is invalid
         else {
             isTrue = false;
         }
         return isTrue;
    }

    /**
     * setPhase: set the state of the Game State
     *
     * @param gameState - the state of the game
     * @param newPhase - the phase to be set onto the Game State
     *
     * @return returns isTrue, TRUE if phase is 0, 1, or 2; FALSE otherwise
     */
    public boolean setPhase(StrategoGameState gameState, int newPhase) {
        boolean isTrue = false;

        if (gameState.phase == 0 || gameState.phase == 1 || gameState.phase == 2) {
            gameState.phase = newPhase;
            isTrue = true;
        }
        else {
            isTrue = false;
        }

        return isTrue;
    }

    /**
     *increaseCap - increase the captured pieces of the pieceValue type
     *
     * @param player - player whose playing
     * @param pieceValue - value of piece being removed
     */
    public void increaseCap(int player, int pieceValue){
        switch (player){
            case 0:
                        redCharacter[pieceValue] += 1;
                break;
            case 1:
                            blueCharacter[pieceValue] += 1;
                break;
        }
    }

    public String movePrint(int row, int col, int frow, int fcol){

        boolean attacking = false;
        if(board[row][col] == null) {
            return "No piece to move at this location";
        }

        String piece1 = setName(board[row][col].getValue());

        if(board[frow][fcol] != null){
            attacking = true;
        }

        if(this.action(row,col,frow,fcol)){
            if(attacking){
                return piece1 + " at (" + row + "," + col + ") attacked piece at (" + frow + "," + fcol + ") and the movement was successful!";
            }
            else{
                return piece1 + " at (" + row + "," + col + ") moved to (" + frow + "," + fcol + ") and the movement was successful!";

            }
        }
        else{
            return piece1 + " at (" + row + "," + col + ") attempted to move to (" + frow + "," + fcol + ") and the movement was unsuccessful!";

        }
    }

    public Piece[][] getBoard(){
        return board;
    }

    /**
     * equals - Checks if two GameStates are equal
     *
     * @param g - GameState to check agaist
     * @return true if the gamestates are the same and false if not
     */
    public boolean equals(StrategoGameState g){
        for(int row = 0; row < 10; row++){
            for(int col = 0; col< 10; col++){
                if(this.board[row][col] == null && g.getBoard()[row][col] != null || this.board[row][col] != null && g.getBoard()[row][col] == null){
                    return false;
                }
                if(!(board[row][col].isEqual(g.getBoard()[row][col]))){
                    return false;
                }
            }
        }
        return true;
    }

    public int getPlayerID(){
        return turn;
    }

    public int getPhase(){
        return phase;
    }

    public float getTimer(){
        return timer;
    }



}
