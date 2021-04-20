package com.example.strategotest.Stratego.infoMessages;

import android.widget.ImageButton;

import com.example.strategotest.R;
import com.example.strategotest.Stratego.Players.DumbComputerPlayer;
import com.example.strategotest.Stratego.Players.HumanPlayer;
import com.example.strategotest.game.GameFramework.infoMessage.GameState;
import com.example.strategotest.Stratego.Piece;
import com.example.strategotest.Stratego.SpecialPiece;
import com.example.strategotest.game.GameFramework.players.GameComputerPlayer;
import com.example.strategotest.game.GameFramework.players.GamePlayer;

import java.util.ArrayList;

/**
 * @author Gareth Rice
 * @author Devam Patel
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @version 3/21
 * <p>
 * Notes/Bugs:
 * The scout can move as far as it pleases, but it can't move and attack
 * when I think it should be able to
 * Make a red stratego tile instead of a blue one for when it's the blue players turn?
 * Capturing the flag doesn't end the game. (Haven't tried taking all the pieces)
 */

public class StrategoGameState extends GameState {

    //Number of characters of each color
    //0 - spy, 10 - bomb, 11 - flag
    private int[] blueCharacter;
    private int[] redCharacter;

    private int[] filledRedCharacters = new int[12];

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

    //use this for the undo move
    StrategoGameState backup = null;

    /**
     * constructor for gamestate
     */
    public StrategoGameState() {
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

        filledRedCharacters[0] = 1; //flag
        filledRedCharacters[1] = 1; //marshall
        filledRedCharacters[2] = 1; //general
        filledRedCharacters[3] = 2; //colonel
        filledRedCharacters[4] = 3; //major
        filledRedCharacters[5] = 4; //captain
        filledRedCharacters[6] = 4; //Lieutenant
        filledRedCharacters[7] = 4; //sergeant
        filledRedCharacters[8] = 5; //miner
        filledRedCharacters[9] = 8; //scout
        filledRedCharacters[10] = 6; //bomb
        filledRedCharacters[11] = 1; //spy

        //initialize other values
        turn = 0;

        //initialize board array with null in all empty spots
        board = new Piece[10][10];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //Make the lakes in the center of the board equal -2. Spaces with -2 can't
                //be crossed/moved into
                if ((i == 4 || i == 5) && (j == 2 || j == 3 || j == 6 || j == 7)) {
                    board[i][j] = new Piece("Lake", -1, -1, true);
                } else {
                    board[i][j] = null;
                }
            }
        }

        timer = 0;
        phase = 0; // 0 = placement, 1 = game, 2 = end play

        //when the game is first made, we need to instance pieces for player 1 and player 2
        instancePieces(0);
        instancePieces(1);

    }

    /**
     * copy constructor displays only the half of the pieces that belong to the given character
     *
     * @param original
     */
    public StrategoGameState(StrategoGameState original) {

        //make new array for the board
        board = new Piece[10][10];
        this.turn = original.getTurn();
        this.timer = original.getTimer();
        this.phase = original.getPhase();

        //set the new board state equal to original board state
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //assign board[i][j] before null check

                //create a deep copy of each piece
                if (original.board[i][j] != null) {
                    //need to create a special piece if it's a bomb or flag
                    if (original.board[i][j].getName().equalsIgnoreCase("Bomb") || original.board[i][j].getName().equalsIgnoreCase("Flag")) {
                        board[i][j] = new SpecialPiece(original.board[i][j].getName(), original.board[i][j].getValue(), original.board[i][j].getPlayer(), original.board[i][j].getWasSeen());
                        board[i][j].setVisible(original.board[i][j].getVisible());
                    }
                    //need to create normal piece otherwise
                    else {
                        board[i][j] = new Piece(original.board[i][j].getName(), original.board[i][j].getValue(), original.board[i][j].getPlayer(), original.board[i][j].getWasSeen());
                        board[i][j].setVisible(original.board[i][j].getVisible());
                    }
                } else {
                    board[i][j] = null;
                }

                if (board[i][j] != null) {
                    //If it's blue's turn and the piece is red, make red piece invisible
                    if (board[i][j].getPlayer() == 1 && turn == 0 && !board[i][j].getWasSeen()) {
                        board[i][j].setVisible(false);
                    }
                    //if it's red's turn and the piece is blue, make blue piece invisible
                    else if (board[i][j].getPlayer() == 0 && turn == 1 && !board[i][j].getWasSeen()) {
                        board[i][j].setVisible(false);
                    } else { //assign the rest of the board state
                        board[i][j].setVisible(true);
                    }
                }

            }
        }

        //copy over information (not deep copied)


        blueCharacter = new int[12];
        redCharacter = new int[12];
        filledRedCharacters = new int[12];

        for (int i = 0; i < blueCharacter.length; i++) {
            blueCharacter[i] = original.blueCharacter[i];
            redCharacter[i] = original.redCharacter[i];
            filledRedCharacters[i] = original.filledRedCharacters[i];
        }

        blueBench = original.blueBench;
        redBench = original.redBench;


    }

    public ArrayList getRedBench() {
        return redBench;
    }

    public void saveBackup() {
        backup = new StrategoGameState(this);
    }

    public StrategoGameState getBackup() {
        return backup;
    }

    /**
     * Show the game pieces on the board
     *
     * @param boardButtons: ImageButton[][]
     *                      array of the buttons
     */
    public void showBoard(ImageButton[][] boardButtons) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
//                boardButtons[i][j].setImageResource(R.drawable.bluetile);
                if (board[i][j] == null) {
                    boardButtons[i][j].setImageResource(R.drawable.test);
                } else if (board[i][j].getValue() == -1) {
                    boardButtons[i][j].setImageResource(R.drawable.water);
                } else if (turn == 0 && board[i][j].getPlayer() == 1 && !board[i][j].getWasSeen()) {
                    //if it's the red players turn and the piece is blue, make it invisible blue
                    boardButtons[i][j].setImageResource(R.drawable.bluetile);
                } else if (turn == 1 && board[i][j].getPlayer() == 0 && !board[i][j].getWasSeen()) {
                    boardButtons[i][j].setImageResource(R.drawable.bluetile); //make this a red stratego tile
                } else {
                    boardButtons[i][j].setImageResource(setIcon(board[i][j].getValue(), board[i][j].getPlayer()));
                }

            }
        }
    }

    /**
     * Add instantiated pieces to array lists for both red and blue
     *
     * @param player
     * @return
     */
    public boolean instancePieces(int player) {
        ArrayList<Piece> assign;
        int[] numberPieces = new int[12];
        String name;


        if (player == 0) {
            assign = redBench;

            //deep copies number pieces so the official counts don't decrease until the piece
            //is placed on the board
            for (int i = 0; i < numberPieces.length; i++) {
                numberPieces[i] = redCharacter[i];
            }
        } else {
            assign = blueBench;
            for (int i = 0; i < numberPieces.length; i++) {
                numberPieces[i] = blueCharacter[i];
            }
        }

        //iterate over the 12 types of pieces
        for (int i = 0; i < 12; i++) {
            //need to instance all of the pieces
            name = setName(i);
//            int theIcon = setIcon(i); //this doesn't work like it's supposed to. Maybe fix later?

            //go over the number of each particular piece and add an instanced piece to
            //an array list
            while (numberPieces[i] > 0) {
                //if the piece is flag or bomb, create special piece
                if (i == 0 || i == 10) {
                    assign.add(new SpecialPiece(name, i, player, false));
                } else {
                    //we can add conditions to add spy, miner, and scout special pieces
                    assign.add(new Piece(name, i, player, false));
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
    public String setName(int whichName) {
        String returnName;

        //probably a poor way to get the name? Can use hashtable?
        switch (whichName) {
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

    public int setIcon(int whichPiece, int playerID) {
        int returnDrawID;

        switch (whichPiece) {
            case 0:
                if(playerID == 0){
                    returnDrawID = R.drawable.flagr;
                } else {
                    returnDrawID = R.drawable.flagb;
                }

                break;
            case 1:
                if(playerID == 0){
                    returnDrawID = R.drawable.marshr;
                } else {
                    returnDrawID = R.drawable.marshb;
                }
                break;
            case 2:
                if(playerID == 0){
                    returnDrawID = R.drawable.genr;
                } else {
                    returnDrawID = R.drawable.genb;
                }
                break;
            case 3:
                if(playerID == 0){
                    returnDrawID = R.drawable.colr;
                } else {
                    returnDrawID = R.drawable.colb;
                }
                break;
            case 4:
                if(playerID == 0){
                    returnDrawID = R.drawable.majr;
                } else {
                    returnDrawID = R.drawable.majb;
                }
                break;
            case 5:
                if(playerID == 0){
                    returnDrawID = R.drawable.captr;
                } else {
                    returnDrawID = R.drawable.captb;
                }
                break;
            case 6:
                if(playerID == 0){
                    returnDrawID = R.drawable.ltr;
                } else {
                    returnDrawID = R.drawable.ltb;
                }
                break;
            case 7:
                if(playerID == 0){
                    returnDrawID = R.drawable.sergr;
                } else {
                    returnDrawID = R.drawable.sergb;
                }
                break;
            case 8:
                if(playerID == 0){
                    returnDrawID = R.drawable.minerr;
                } else {
                    returnDrawID = R.drawable.minerb;
                }
                break;
            case 9:
                if(playerID == 0){
                    returnDrawID = R.drawable.scoutr;
                } else {
                    returnDrawID = R.drawable.scoutb;
                }
                break;
            case 10:
                if(playerID == 0){
                    returnDrawID = R.drawable.bombr;
                } else {
                    returnDrawID = R.drawable.bombb;
                }
                break;
            case 11:
                if(playerID == 0){
                    returnDrawID = R.drawable.spyr;
                } else {
                    returnDrawID = R.drawable.spyb;
                }
                break;
            default:
                returnDrawID = R.drawable.bluetile;
                break;
        }

        return returnDrawID;
    }

    /**
     * Change an individual element of the red character array
     *
     * @param p - piece #
     * @param v - number of pieces
     */
    public void setInRedCharacter(int p, int v) {
        redCharacter[p] = v;
    }

    /**
     * Change an individual element of the blue character array
     *
     * @param p - piece #
     * @param v - number of pieces
     */
    public void setInBlueCharacter(int p, int v) {
        blueCharacter[p] = v;

    }


    /**
     * place: Place the pieces from their respective arrayLists
     *
     * @return
     */

    public boolean place(int player) {
        int start;
        int randomIndex;
        ArrayList<Piece> currentArmy;

        //place red pieces on bottom of board
        if (player == 0) {
            start = 6;
            currentArmy = redBench;
        } else {
            start = 0;
            currentArmy = blueBench;
        }

        //iterate over the first 4, or last 4 rows depending on blue or red player
        for (int i = start; i < start + 4; i++) {
            for (int j = 0; j < board[i].length; j++) {

                //place the flag in the last possible place
                /*if (j == board[i].length - 1 && i == start + 3) {
                    board[i][j] = currentArmy.get(0);
                    currentArmy.remove(0);
                } else {
                    board[i][j] = currentArmy.get(1);
                    currentArmy.remove(1);
                }
*/


                //set the index i j to a random piece from specific players arrayList of
                //instantiated pieces
                if (board[i][j] == null) {
                    randomIndex = (int) (Math.random() * currentArmy.size());

                    //set that board index to the random index
                    board[i][j] = currentArmy.get(randomIndex);
                    //once placed from bench it should be removed as its now on the board
                    currentArmy.remove(randomIndex);
                }


            }
        }
        if (player == 0) {
            for (int i = 0; i < 12; i++) {
                redCharacter[i] = 0;
            }
        } else {
            for (int i = 0; i < 12; i++) {
                blueCharacter[i] = 0;
            }
        }

        return true;
    }

    /**
     * This is a method to test the action method
     * @param player
     * @return
     */
    public boolean placeNotRandom(int player) {
        int start;
        int randomIndex;
        ArrayList<Piece> currentArmy;

        //place red pieces on bottom of board
        if (player == 0) {
            start = 6;
            currentArmy = redBench;
        } else {
            start = 0;
            currentArmy = blueBench;
        }

        //iterate over the first 4, or last 4 rows depending on blue or red player
        for (int i = start; i < start + 4; i++) {
            for (int j = 0; j < board[i].length; j++) {

                //place the flag in the last possible place
                if (j == board[i].length - 1 && i == start + 3) {
                    board[i][j] = currentArmy.get(0);
                    currentArmy.remove(0);
                } else {
                    board[i][j] = currentArmy.get(1);
                    currentArmy.remove(1);
                }



                //set the index i j to a random piece from specific players arrayList of
                //instantiated pieces
               // if (board[i][j] == null) {
                //    randomIndex = (int) (Math.random() * currentArmy.size());

                    //set that board index to the random index
                   // board[i][j] = currentArmy.get(randomIndex);
                    //once placed from bench it should be removed as its now on the board
                 //   currentArmy.remove(randomIndex);
                //}


            }
        }
        if (player == 0) {
            for (int i = 0; i < 12; i++) {
                redCharacter[i] = 0;
            }
        } else {
            for (int i = 0; i < 12; i++) {
                blueCharacter[i] = 0;
            }
        }

        return true;
    }

    /**
     * placeRemove: places a single piece or removes a single piece
     *
     * @param value - what the piece value is
     * @param row   - row to place piece
     * @param col-  col to place piece
     * @return returns true if piece is removed or placed, false if failure
     */
    public boolean placeRemove(int value, int row, int col) {
        if (phase == 0) {
            if (board[row][col] == null) {
                String returnName = setName(value);
                //Put piece in that spot
                board[row][col] = new Piece(returnName, value, turn, false);
                return true;
            } else if (board[row][col].getValue() < 0 || board[row][col].getPlayer() < 0) {
                //don't mess with lake
                return false;

            } else {
                // Remove piece if one is already there
                board[row][col] = null;
                return true;
            }
        } else {
            return false;
        }

    }

    /**
     * Notes
     * Right now this does no error checking for if the given row and col have anything on them
     * or if the player is placing in the right area.
     * Also need to check if all the players pieces have been placed
     *
     * @param player
     * @param value
     * @param row
     * @param col
     * @return
     */
    public boolean placeChosenPiece(GamePlayer player, int value, int row, int col) {
        int myId;

        if (player instanceof GameComputerPlayer) {
            //place the computers piece
            //first, we need to decrement
            myId = ((DumbComputerPlayer) player).getPlayerID();
        } else if (player instanceof HumanPlayer) {
            //place the human players piece
            //first, we need to get a reference to the passed in player. Using their ID decrement
            //either red (0) or blue (1)
            myId = ((HumanPlayer) player).getHumanPlayerID();

        } else {
            return false;
        }

        //decrement the counter for number of pieces left
        if (myId == 0) {
            //check to make sure the row and column is in correct territory
            if (row < 6) {
                //red must place in rows 6-9
                return false;
            } else {
                return checkPlace(myId, value, row, col);
            }

        } else {
            if (row > 3) {
                //blue must place in rows 0-3
                return false;
            } else {
                return checkPlace(myId, value, row, col);
            }
        }

    }

    public boolean checkPlace(int id, int value, int row, int col) {
        //if the id is 0, we are red, and need to stick on reds side
        int[] numTroops;
        ArrayList<Piece> toUsePieces;

        if (id == 0) {
            numTroops = redCharacter;
            toUsePieces = redBench;
        } else {
            numTroops = blueCharacter;
            toUsePieces = blueBench;
        }


        //then, using the instanced pieces, we need to actually place the piece on the baord
        int loop = 0;
        //loop through instantiated pieces till we find the one with the right value
        do {
            if (numTroops[value] <= 0) {
                return false;
            }
//            try {
            if (toUsePieces.get(loop).getValue() == value) {
                if (board[row][col] != null) {
                    //re increase the number of troops
                    numTroops[board[row][col].getValue()]++;
                    Piece returnToBin = new Piece(board[row][col].getName(), board[row][col].getValue(), board[row][col].getPlayer(), board[row][col].getWasSeen());
                    toUsePieces.add(returnToBin);

                }
                board[row][col] = toUsePieces.get(loop);
                break;
            }
//            } catch (Exception ex) {
//                //will probably throw an out of bounds exception...
//                //but I just fixed that in the while loop itself...
//                return false;
//            }
            loop++;
            //} while (board[row][col].getValue() != value && !(loop > toUsePieces.size()));
        } while (!(loop > toUsePieces.size()));

        //dec
        numTroops[value]--;

        toUsePieces.remove(loop);
        loop = 0;

        return true;
    }


    /**
     * action: Preforms the attack and move methods depending on the situation
     *
     * @param fromX - row of starting piece
     * @param fromY - col of starting piece
     * @param toX   - row of where you are placing piece
     * @param toY-  col of where you are placing piece
     * @return boolean - true if success, false if failure
     */
    public boolean action(int fromX, int fromY, int toX, int toY) {
        int whoseE = (turn + 1) % 2;
        boolean success = false;
        if (board[fromX][fromY] == null) {
            return false;
        }
        //Make sure you can't move opponent piece
        if (board[fromX][fromY].getPlayer() != turn) {
            return false;
        }
        //Make sure you can't move lake
        if (board[fromX][fromY].getValue() < 0) {
            return false;
        }
        if (board[fromX][fromY] == null) {
            return false;
        }
        //check to make sure movement is not greater than 1 and not diagonal
        if (Math.abs(fromY - toY) >= 1 && Math.abs(fromX - toX) >= 1) {
            return false;
        }
        //If not 9
        if ((Math.abs(fromX - toX) > 1 || Math.abs(fromY - toY) > 1) && (board[fromX][fromY].getValue() != 9 || board[toX][toY] != null)) {
            return false;
        }

        if (board[fromX][fromY].move(board[toX][toY])) {
            //Move(prevents null pointer exception)
            if (board[toX][toY] == null) {
                board[toX][toY] = new Piece(board[fromX][fromY].getName(), board[fromX][fromY].getValue(), board[fromX][fromY].getPlayer(), board[fromX][fromY].getWasSeen());
                board[fromX][fromY] = null;
                success = true;
            }
            //Attack
            else if (board[fromX][fromY].getPlayer() != board[toX][toY].getPlayer() && board[toX][toY].getPlayer() != -1) {
                //there's been an attack so set both pieces to wasSeen
                board[fromX][fromY].setWasSeen(true);
                board[toX][toY].setWasSeen(true);

                //If pieces are the same value
                if (board[fromX][fromY].getValue() == board[toX][toY].getValue()) {
                    //Increase captured by both
                    increaseCap(whoseE, board[toX][toY].getValue());
                    increaseCap(turn, board[fromX][fromY].getValue());
                    board[fromX][fromY] = null;
                    board[toX][toY] = null;
                    return true;
                }
                //If piece attacking is successful
                else if (board[fromX][fromY].attack(board[toX][toY])) {
                    //Increase num captured by attacker
                    increaseCap(whoseE, board[toX][toY].getValue());
                    board[toX][toY] = new Piece(board[fromX][fromY].getName(), board[fromX][fromY].getValue(), board[fromX][fromY].getPlayer(), board[fromX][fromY].getWasSeen());
                    board[fromX][fromY] = null;

                }
                //If piece defending is successful
                else {
                    //Increase num captured by defender
                    increaseCap(turn, board[fromX][fromY].getValue());
                    board[fromX][fromY] = null;
                }
                success = true;
            }
            //Move
            else {
                //Check to make sure 9 only goes through empty spaces
                if (board[fromX][fromY].getValue() == 9) {
                    if (fromX > toX) {
                        for (int i = fromX - 1; i >= toX; i--) {
                            if (board[i][fromY] != null) {
                                //Invalid
                                return false;
                            }
                        }

                    } else if (fromY > toY) {
                        for (int i = fromY - 1; i >= toY; i--) {
                            if (board[fromX][i] != null) {
                                //Invalid
                                return false;
                            }

                        }

                    } else if (fromY < toY) {
                        for (int i = fromY + 1; i <= toY; i++) {
                            if (board[fromX][i] != null) {
                                //Invalid
                                return false;
                            }
                        }
                    } else if (fromX < toX) {
                        for (int i = fromX + 1; i <= toX; i++) {
                            if (board[i][fromY] != null) {
                                //Invalid
                                return false;
                            }
                        }


                    }
                }
                board[toX][toY] = new Piece(board[fromX][fromY].getName(), board[fromX][fromY].getValue(), board[fromX][fromY].getPlayer(), board[fromX][fromY].getWasSeen());
                board[fromX][fromY] = null;
                success = true;
            }
        }
        return success;
    }


    //toString method print current state of the game as a String
    @Override
    public String toString() {
        String finalMessage;

        //print whos turn
        String player;
        if (turn == 0) {
            player = "Red";
        } else {
            player = "Blue";
        }

        //print game phase
        String gamePhase;
        if (phase == 0) {
            gamePhase = "Placement";
        } else if (phase == 1) {
            gamePhase = "Play Phase";
        } else if (phase == 2) {
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

    public boolean endTurn() {
        if (this.phase == 0 && this.turn == 1) {
            this.phase = 1;
        }

        boolean isTrue = false;
        // Player 1 (represented by 0) ended turn
//         if (gameState.turn == 0) {
        if (this.turn == 0) {
//             gameState.turn = 1;
            this.turn = 1;

            isTrue = true;
        }
        // Player 2 (represented by 1) ended turn
//         else if (gameState.turn == 1){
        else if (this.turn == 1) {
//             gameState.turn = 0;
            this.turn = 0;
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
     * increaseCap - increase the captured pieces of the pieceValue type
     *
     * @param player     - player whose playing
     * @param pieceValue - value of piece being removed
     */
    public void increaseCap(int player, int pieceValue) {
        switch (player) {
            case 0:
                redCharacter[pieceValue] += 1;
                break;
            case 1:
                blueCharacter[pieceValue] += 1;
                break;
        }
    }

    public String movePrint(int row, int col, int frow, int fcol) {

        boolean attacking = false;
        if (board[row][col] == null) {
            return "No piece to move at this location";
        }

        String piece1 = setName(board[row][col].getValue());

        if (board[frow][fcol] != null) {
            attacking = true;
        }

        if (this.action(row, col, frow, fcol)) {
            if (attacking) {
                return piece1 + " at (" + row + "," + col + ") attacked piece at (" + frow + "," + fcol + ") and the movement was successful!";
            } else {
                return piece1 + " at (" + row + "," + col + ") moved to (" + frow + "," + fcol + ") and the movement was successful!";

            }
        } else {
            return piece1 + " at (" + row + "," + col + ") attempted to move to (" + frow + "," + fcol + ") and the movement was unsuccessful!";

        }
    }


    /**
     * equals - Checks if two GameStates are equal
     *
     * @param g - GameState to check agaist
     * @return true if the gamestates are the same and false if not
     */
    public boolean equals(StrategoGameState g) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (this.board[row][col] == null && g.getBoard()[row][col] != null || this.board[row][col] != null && g.getBoard()[row][col] == null) {
                    return false;
                }
                if (!(board[row][col].isEqual(g.getBoard()[row][col]))) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Getters and setters for our instance variables within the gamestate class.
     *
     */
    public int getId() {
        return turn;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int p) {
        this.phase = p;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float t) {
        this.timer = t;
    }

    public int[] getBlueCharacter() {
        return blueCharacter;
    }

    public void setBlueCharacterValue(int index, int value) {
        blueCharacter[index] = value;
    }

    public int[] getRedCharacter() {
        return redCharacter;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int[] getFilledRedCharacters() {
        return filledRedCharacters;
    }

    public void setFilledRedCharacters(int index, int value) {
        filledRedCharacters[index] = value;
    }


}
