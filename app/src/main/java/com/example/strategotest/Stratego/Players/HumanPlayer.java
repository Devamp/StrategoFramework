package com.example.strategotest.Stratego.Players;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.strategotest.R;
import com.example.strategotest.Stratego.MainActivity;
import com.example.strategotest.Stratego.actionMessage.PassTurnAction;
import com.example.strategotest.Stratego.actionMessage.StrategoBackupAction;
import com.example.strategotest.Stratego.actionMessage.StrategoMoveAction;
import com.example.strategotest.Stratego.actionMessage.StrategoPlaceAction;
import com.example.strategotest.Stratego.actionMessage.StrategoRandomPlace;
import com.example.strategotest.Stratego.actionMessage.StrategoUndoTurnAction;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.GameMainActivity;
import com.example.strategotest.game.GameFramework.actionMessage.EndTurnAction;
import com.example.strategotest.game.GameFramework.infoMessage.GameInfo;
import com.example.strategotest.game.GameFramework.infoMessage.GameOverInfo;
import com.example.strategotest.game.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.strategotest.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.strategotest.game.GameFramework.players.GameHumanPlayer;
import com.example.strategotest.game.GameFramework.utilities.MessageBox;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Gareth Rice
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @author Devam Patel
 * @version 4/21
 * <p>
 * Notes:
 * Make End turn and undo turn invisible until a move has been made
 * Lock out movement after a player has moved
 * Make opponents pieces invisible -- done
 * Can move the opponents pieces. Should probably fix that
 * If you click on an empty tile, and then try to click one of your pieces, it makes an illegal
 * move action. Trying to then move the just clicked piece doesn't work and is confusing
 * Maybe somehow highlight the currently selected piece
 * Gotta reveal the piece that the player interacted with. Maybe that will be in beta?
 * I think I mixed up the undo turn and undo move
 * Don't lock the player out if they make an invalid move. They should get a warning and be
 * allowed to keep making moves until a valid one is selected
 * Hitting null space, null space for movement causes app to crash
 * <p>
 * Placing piece works but is incredibly buggy. Can place negative amount of pieces,
 * can place over lakes and opponents pieces. Places the wrong pieces. Doesn't check
 * once we have placed all pieces. Many errors. But it works.
 * <p>
 * Make textView on top of captured box to be place pieces in the place phase (instead of captured
 * pieces)
 */
public class HumanPlayer extends GameHumanPlayer implements View.OnClickListener {

    private int layoutID;
    private int humanPlayerID;

    private GameMainActivity myActivity;

    private Button surrender = null;
    private Button endTurn = null;
    private Button undoTurn = null;

    //this will be invisible until Beta
    private Button ranPlace = null;

    // initialize the variables needed for the game Timer
    private TextView timerText = null;
    private Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    private ImageView whoseTurn = null;

    private TextView capturePiecesText;

    private ImageButton[][] boardButtons = new ImageButton[10][10];

    //create the buttons for placing pieces
    private ImageButton[] piecesRemain = new ImageButton[12];

    //create labels for number pieces remaining
    private TextView[] piecesRemainLabel = new TextView[12];

    //A move action is created when this is true because a piece has already been selected
    //to move
    private boolean selectedFirst = false;

    //Use this game state for undoing moves. Before a move, this state is created
    StrategoGameState revertState = null;

    //the state we are going to use
    StrategoGameState toUse = null;

    //keep track of where the human is moving to and from
    int fromX = -1;
    int fromY = -1;
    int toX = -1;
    int toY = -1;

    //if this is true, then no other moves can be made.
    private boolean hasMoved = false;

    //if this is true, we have a piece ready to place and the next click places it
    private boolean selectToPlace = false;

    private int myPhase = 0;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public HumanPlayer(String name, int layoutID, int playerID) {
        super(name);
        this.layoutID = layoutID;
        humanPlayerID = playerID;

    }


    /**
     * returns the GUI's top view
     *
     * @return the GUI's top view
     */
    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.mainLayout);
    }

    /**
     * Callback method, called when player gets a message
     *
     * @param info the message
     */
    @Override
    public void receiveInfo(GameInfo info) {

        if ((info instanceof IllegalMoveInfo)) {
            hasMoved = false;
        }

        if (!(info instanceof StrategoGameState)) {
            int myColor = Color.rgb(255, 0, 0);
            flash(myColor, 10000);
            return;
        }

        humanPlayerID = playerNum;

        //get working gameState
        toUse = new StrategoGameState((StrategoGameState) info);

        myPhase = toUse.getPhase();

        //see if the player has placed all their pieces. Make pass visible if so
        allPiecesPlaced(myPhase);

        //When the turn is passed, display whose turn in the upper right corner
        setTurnColor(toUse);

        if (toUse.getTurn() == humanPlayerID) {
            toUse.showBoard(boardButtons);
        }

        //Set visibility of ranPlace button
        if (myPhase == 0) {
            ranPlace.setVisibility(View.VISIBLE);
        } else {
            capturePiecesText.setText("Captured Pieces");
            ranPlace.setVisibility(View.INVISIBLE);
        }

        setEndUndoVisibility(myPhase);

        int[] troopNumbers;
        if (humanPlayerID == 0) {
            //if the player is 0, they are red
            troopNumbers = toUse.getRedCharacter();
        } else {
            troopNumbers = toUse.getBlueCharacter();
        }

        //set the number of captured pieces
        for (int i = 0; i < piecesRemainLabel.length; i++) {
            String multi = "x" + troopNumbers[i];
            piecesRemainLabel[i].setText(multi);
        }
    }

    /**
     * check if all the players pieces have been placed. If they have, make pass turn available
     *
     * @param myPhase
     */
    public void allPiecesPlaced(int myPhase) {
        //check to see if all the players pieces have been placed
        if (myPhase == 0) {
            boolean blueP = true;
            boolean redP = true;
            for (int i = 0; i < 12; i++) {
                if (toUse.getBlueCharacter()[i] != 0) {
                    blueP = false;

                }
                if (toUse.getRedCharacter()[i] != 0) {
                    redP = false;

                }
            }
            if (humanPlayerID == 1 && blueP) {
                endTurn.setVisibility(View.VISIBLE);
            } else if (humanPlayerID == 0 && redP) {
                endTurn.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * sets the visibility of the endTurn and undoTurn buttons when appropriate
     *
     * @param myPhase
     */
    public void setEndUndoVisibility(int myPhase) {
        //if the player has made a move, undoTurn and endTurn become available
        if (myPhase != 0) {
            if (hasMoved) {
                endTurn.setVisibility(View.VISIBLE);
                undoTurn.setVisibility(View.VISIBLE);
            } else {
                //backup the current board so we can revert to it if we want to undo
                game.sendAction(new StrategoBackupAction(this)); //this works. Not sure if it's the best way to do it, but it works!!

                endTurn.setVisibility(View.INVISIBLE);
                undoTurn.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * setTurnColor makes the Turn indicator the appropriate color
     *
     * @param state
     */
    public void setTurnColor(StrategoGameState state) {
        //set turn color to whatever players turn it is
        if (toUse.getTurn() == 0) {
            //red players turn
            whoseTurn.setImageResource(R.drawable.redsquare);
        } else if (toUse.getTurn() == 1) {
            //blue player is turn 1
            whoseTurn.setImageResource(R.drawable.bluesquare);
        } else {
            whoseTurn.setImageResource(R.drawable.redsquare);
        }
    }


    /**
     * sets the current player as the activity's GUI
     */
    @Override
    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;

        activity.setContentView(R.layout.activity_main);

        //find views of buttons
        surrender = (Button) activity.findViewById(R.id.surrenderButton);
        surrender.setOnClickListener(this);
        endTurn = (Button) activity.findViewById(R.id.endTurnButton);
        endTurn.setOnClickListener(this);
        undoTurn = (Button) activity.findViewById(R.id.undoTurnButton);
        undoTurn.setOnClickListener(this);
        ranPlace = (Button) activity.findViewById(R.id.randomPlace);
        ranPlace.setOnClickListener(this);
        //we're not using undoMove button yet. I also mixed up undo move and undo turn
        ranPlace.setVisibility(View.INVISIBLE);

        capturePiecesText = (TextView) activity.findViewById(R.id.capturePiecesText);
        capturePiecesText.setText("Place Pieces");

        //set end and undo turn to invisible by default
        endTurn.setVisibility(View.INVISIBLE);
        undoTurn.setVisibility(View.INVISIBLE);

        //get timer view
        timerText = (TextView) activity.findViewById(R.id.timerTextView);
        timer = new Timer();
        startTimer();

        whoseTurn = (ImageView) activity.findViewById(R.id.whoseTurnImage);

        //connect all the buttons.
        //https://www.technotalkative.com/android-findviewbyid-in-a-loop/
        //God bless ^^^^
        //Saved hours of tedious coding
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String spaceID = "space" + (i) + (j);

                int resID = myActivity.getResources().getIdentifier(spaceID, "id", myActivity.getPackageName());
                boardButtons[i][j] = (ImageButton) activity.findViewById(resID);
                boardButtons[i][j].setOnClickListener(this);
            }
        }

        //hardcode references to all the bin buttons
        piecesRemain[0] = (ImageButton) activity.findViewById(R.id.flagTracker);
        piecesRemain[1] = (ImageButton) activity.findViewById(R.id.marshallTracker);
        piecesRemain[2] = (ImageButton) activity.findViewById(R.id.generalTracker);
        piecesRemain[3] = (ImageButton) activity.findViewById(R.id.colonelTracker);
        piecesRemain[4] = (ImageButton) activity.findViewById(R.id.majorTracker);
        piecesRemain[5] = (ImageButton) activity.findViewById(R.id.captainTracker);
        piecesRemain[6] = (ImageButton) activity.findViewById(R.id.lieutenantTracker);
        piecesRemain[7] = (ImageButton) activity.findViewById(R.id.sergeantTracker);
        piecesRemain[8] = (ImageButton) activity.findViewById(R.id.minerTracker);
        piecesRemain[9] = (ImageButton) activity.findViewById(R.id.scoutTracker);
        piecesRemain[10] = (ImageButton) activity.findViewById(R.id.bombTracker);
        piecesRemain[11] = (ImageButton) activity.findViewById(R.id.spyTracker);

        for (int i = 0; i < piecesRemain.length; i++) {
            piecesRemain[i].setOnClickListener(this);
        }

        //hardcode references to labels for number pieces remaining
        piecesRemainLabel[0] = (TextView) activity.findViewById(R.id.flagMult);
        piecesRemainLabel[1] = (TextView) activity.findViewById(R.id.marshallMultiplier);
        piecesRemainLabel[2] = (TextView) activity.findViewById(R.id.generalMult);
        piecesRemainLabel[3] = (TextView) activity.findViewById(R.id.colonelMult);
        piecesRemainLabel[4] = (TextView) activity.findViewById(R.id.majorMult);
        piecesRemainLabel[5] = (TextView) activity.findViewById(R.id.captainMult);
        piecesRemainLabel[6] = (TextView) activity.findViewById(R.id.lieutenantMult);
        piecesRemainLabel[7] = (TextView) activity.findViewById(R.id.sergentMult);
        piecesRemainLabel[8] = (TextView) activity.findViewById(R.id.minerMult);
        piecesRemainLabel[9] = (TextView) activity.findViewById(R.id.scoutMult);
        piecesRemainLabel[10] = (TextView) activity.findViewById(R.id.bombMult);
        piecesRemainLabel[11] = (TextView) activity.findViewById(R.id.spyMult);

    }

    /**
     * determines if a button was pressed, and calls appropriate method if imageButton or Button
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            buttonOnClick(v);
        } else if (v instanceof ImageButton && !hasMoved) { //deleted && !hasMoved
            imageButtonOnClick(v);
        }
    }

    /**
     * If a normal button was clicked it sends appropriate action
     *
     * @param v
     */
    public void buttonOnClick(View v) {
        if (v.getId() == R.id.surrenderButton) {
            sendInfo(new GameOverInfo("Player has surrendered"));
        } else if (v.getId() == R.id.endTurnButton) {
            PassTurnAction newPass = new PassTurnAction(this);
            game.sendAction(newPass);
            endTurn.setVisibility(View.INVISIBLE);
            //reset whether they've moved or not
            hasMoved = false;
        } else if (v.getId() == R.id.undoTurnButton) {
            hasMoved = false;
            game.sendAction(new StrategoUndoTurnAction(this));
        } else if (v.getId() == R.id.randomPlace) {
            game.sendAction(new StrategoRandomPlace(this, this.getHumanPlayerID()));
        }

    }

    /**
     * If an image button was clicked get clicked row and col. Then call move or place method
     * depending on what we need
     *
     * @param v
     */
    public void imageButtonOnClick(View v) {
        int clickedRow = -1;
        int clickedCol = -1;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (v.getId() == boardButtons[row][col].getId()) {
                    clickedRow = row;
                    clickedCol = col;
                }
            }
        }

        //If we are in the movement phase, we want to call a method to deal with the movement
        if (myPhase == 1 && clickedRow != -1 && clickedCol != -1) {
            buttonClickMove(v, clickedRow, clickedCol);
        } else if (myPhase == 0) {
            //If we are in the placement phase, we want to call a method to deal with the placement
            buttonClickPlace(v, clickedRow, clickedCol);
        } else {

        }


    }

    /**
     * if we have an image button moved, need to send appropriate move action
     *
     * @param v
     * @param clickedRow
     * @param clickedCol
     */
    public void buttonClickMove(View v, int clickedRow, int clickedCol) {
        if (selectedFirst) {
            toX = clickedRow;
            toY = clickedCol;
            boardButtons[fromX][fromY].setBackgroundColor(Color.rgb(50, 100, 80));
            game.sendAction(new StrategoMoveAction(this, fromX, fromY, toX, toY));
            selectedFirst = false;

            //set the player to have moved so they can't move again
            hasMoved = true;
        } else {
            fromX = clickedRow;
            fromY = clickedCol;

            //highlight the piece
            boardButtons[fromX][fromY].setBackgroundColor(Color.rgb(0, 255, 0));
            selectedFirst = true;
        }
    }

    //use this variable to hold what piece is selected to place
    int placePieceVal = -1;

    /**
     * send place action when we have piece loaded in register
     *
     * @param v
     * @param clickedRow
     * @param clickedCol
     */
    public void buttonClickPlace(View v, int clickedRow, int clickedCol) {
        if (selectToPlace) {
            toX = clickedRow;
            toY = clickedCol;
            if (placePieceVal != -1) {
                game.sendAction(new StrategoPlaceAction(this, placePieceVal, clickedRow, clickedCol));
            }
            selectToPlace = false;
        } else {


            //load the value of the piece we want to place. Will use given value to find correct
            //piece in instantiated pieces ArrayList
            placePieceVal = getTheValue(v);
            selectToPlace = true;
        }
    }

    /**
     * return the value of button in the captured pieces bin for placing
     *
     * @param v
     * @return
     */
    private int getTheValue(View v) {
        //seriously, what's the way to do this without the switch statement? Hash table?
        switch (v.getId()) {
            case R.id.flagTracker:
                return 0;
//                break;
            case R.id.marshallTracker:
                return 1;
//                break;
            case R.id.generalTracker:
                return 2;
//                break;
            case R.id.colonelTracker:
                return 3;
//                break;
            case R.id.majorTracker:
                return 4;
//                break;
            case R.id.captainTracker:
                return 5;
//                break;
            case R.id.lieutenantTracker:
                return 6;
//                break;
            case R.id.sergeantTracker:
                return 7;
//                break;
            case R.id.minerTracker:
                return 8;
//                break;
            case R.id.scoutTracker:
                return 9;
//                break;
            case R.id.bombTracker:
                return 10;
//                break;
            case R.id.spyTracker:
                return 11;
//                break;
            default:
                return -1;
//                break;
        }
    }

    /**
     * @return
     */
    public int getHumanPlayerID() {
        return humanPlayerID;
    }

    /**
     * Starts the timer by creating a TimerTask object and have it increment and change the
     * timerText during runtime.
     * <p>
     * Resources: https://stackoverflow.com/questions/33979132/cannot-resolve-method-runonuithread
     * Problem: (1.) The app kept crashing within the first couple of seconds that the timer starts to tick
     * (2.) Cannot resolve method .runOnUiThread
     * Solution:(1.) Added 'runOnUiThread' so that the TimerTask object would run its specified action on the UI thread
     * (2.) added 'getActivity().' right before runOnUiThread
     */
    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        // Start the timer with no delay upon launch of the main game and
        // change it every 1000 milliseconds (1 second)
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    /**
     * Handles the calculations for seconds and minutes to be displayed on TimerText
     *
     * @return formatTime(seconds, minutes)
     */
    private String getTimerText() {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;

        return formatTime(seconds, minutes);
    }

    /**
     * Properly formats the time so it would return TIME: minutes : seconds
     *
     * @param seconds the number of seconds that have passed
     * @param minutes the number of minutes that have passed
     * @return "TIME: " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds)
     */
    private String formatTime(int seconds, int minutes) {
        return "TIME: " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}
