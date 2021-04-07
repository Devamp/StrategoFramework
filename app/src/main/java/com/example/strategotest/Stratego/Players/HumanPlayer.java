package com.example.strategotest.Stratego.Players;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
 *
 * @version 4/21
 *
 * Notes:
 * Make End turn and undo turn invisible until a move has been made
 * Lock out movement after a player has moved
 * Make opponents pieces invisible -- done
 * Can move the opponents pieces. Should probably fix that
 * If you click on an empty tile, and then try to click one of your pieces, it makes an illegal
 *      move action. Trying to then move the just clicked piece doesn't work and is confusing
 *      Maybe somehow highlight the currently selected piece
 * Gotta reveal the piece that the player interacted with. Maybe that will be in beta?
 * I think I mixed up the undo turn and undo move
 * Don't lock the player out if they make an invalid move. They should get a warning and be
 *      allowed to keep making moves until a valid one is selected
 */
public class HumanPlayer extends GameHumanPlayer implements View.OnClickListener {

    private int layoutID;
    private int humanPlayerID;

    private GameMainActivity myActivity;

    private Button surrender = null;
    private Button endTurn = null;
    private Button undoTurn = null;

    //this will be invisible until Beta
    private Button undoMove = null;

    private TextView timerText = null;
    private Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    private ImageView whoseTurn = null;

    private ImageButton[][] boardButtons = new ImageButton[10][10];

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
     * @return
     * 		the GUI's top view
     */
    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.mainLayout);
    }

    /**
     * Callback method, called when player gets a message
     *
     * @param info
     * 		the message
     */
    @Override
    public void receiveInfo(GameInfo info) {

        if(!(info instanceof StrategoGameState)){
            int myColor = Color.rgb(255, 0, 0);
            flash(myColor, 1000);
            return;
        }

        //get working gameState
//        StrategoGameState toUse = new StrategoGameState((StrategoGameState) info);
        toUse = new StrategoGameState((StrategoGameState) info);

        //set reversion gameState
//        revertState = new StrategoGameState((StrategoGameState) info); //this might not work?

//        setTurnColor(t)

        //set turn color to whatever players turn it is
        if(toUse.getTurn() == 0){
            //red players turn
            whoseTurn.setImageResource(R.drawable.redsquare);
        }else if(toUse.getTurn() == 1){
            //blue player is turn 1
            whoseTurn.setImageResource(R.drawable.bluetile);
        }else{
            whoseTurn.setImageResource(R.drawable.redsquare);
        }

        toUse.showBoard(boardButtons);

        //if the player has made a move, undoTurn and endTurn become available
        if(hasMoved){
            endTurn.setVisibility(View.VISIBLE);
            undoTurn.setVisibility(View.VISIBLE);
        }else{
            //backup the current board so we can revert to it if we want to undo
//            toUse.saveBackup();

            game.sendAction(new StrategoBackupAction(this)); //this works. Not sure if it's the best way to do it, but it works!!
            endTurn.setVisibility(View.INVISIBLE);
            undoTurn.setVisibility(View.INVISIBLE);
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
        undoMove = (Button) activity.findViewById(R.id.undoMoveButton);

        //we're not using undoMove button yet. I also mixed up undo move and undo turn
        undoMove.setVisibility(View.INVISIBLE);

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
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                String spaceID = "space" + (i) + (j);

                int resID = myActivity.getResources().getIdentifier(spaceID, "id", myActivity.getPackageName());
                boardButtons[i][j] = (ImageButton) activity.findViewById(resID);
                boardButtons[i][j].setOnClickListener(this);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button){
            buttonOnClick(v);
        }else if(v instanceof ImageButton && !hasMoved){
            imageButtonOnClick(v);
        }
    }

    public void buttonOnClick(View v){
            if(v.getId() == R.id.surrenderButton){
                sendInfo(new GameOverInfo("Player has surrendered"));
            }else if(v.getId() == R.id.endTurnButton){
                PassTurnAction newPass = new PassTurnAction(this);
                game.sendAction(newPass);
            }else if(v.getId() == R.id.undoTurnButton){
                hasMoved = false;
                game.sendAction(new StrategoUndoTurnAction(this));
            } else{}

    }

    public void imageButtonOnClick(View v){
        int clickedRow = -1;
        int clickedCol = -1;
        for(int row = 0; row < 10; row++){
            for(int col= 0; col < 10; col++){
                if(v.getId() == boardButtons[row][col].getId()){
                    clickedRow = row;
                    clickedCol = col;
                }
            }
        }

        if(selectedFirst){
            toX = clickedRow;
            toY = clickedCol;
            game.sendAction(new StrategoMoveAction(this, fromX, fromY, toX, toY));
            selectedFirst = false;

            //set the player to have moved so they can't move again
            hasMoved = true;
        }else{
            fromX = clickedRow;
            fromY = clickedCol;
            selectedFirst = true;
        }


    }

    /**
     * Starts the timer by creating a TimerTask object and have it increment and change the
     * timerText during runtime.
     *
     * Resources: https://stackoverflow.com/questions/33979132/cannot-resolve-method-runonuithread
     * Problem: (1.) The app kept crashing within the first couple of seconds that the timer starts to tick
     *          (2.) Cannot resolve method .runOnUiThread
     * Solution:(1.) Added 'runOnUiThread' so that the TimerTask object would run its specified action on the UI thread
     *          (2.) added 'getActivity().' right before runOnUiThread
     */
    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        // Start the timer with no delay upon launch of the main game and
        // change it every 1000 milliseconds (1 second)
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
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
     * @param seconds
     * @param minutes
     * @return "TIME: " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds)
     */
    private String formatTime(int seconds, int minutes) {
        return "TIME: " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}
