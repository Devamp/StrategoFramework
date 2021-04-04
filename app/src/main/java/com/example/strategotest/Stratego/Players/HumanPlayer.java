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
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.GameMainActivity;
import com.example.strategotest.game.GameFramework.actionMessage.EndTurnAction;
import com.example.strategotest.game.GameFramework.infoMessage.GameInfo;
import com.example.strategotest.game.GameFramework.infoMessage.GameOverInfo;
import com.example.strategotest.game.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.strategotest.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.strategotest.game.GameFramework.players.GameHumanPlayer;
import com.example.strategotest.game.GameFramework.utilities.MessageBox;

public class HumanPlayer extends GameHumanPlayer implements View.OnClickListener {

    private int layoutID;
    private int humanPlayerID;

    private GameMainActivity myActivity;

    private Button surrender = null;
    private Button endTurn = null;

    private TextView time = null;
    private ImageView whoseTurn = null;

    private ImageButton[][] boardButtons = new ImageButton[10][10];


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
        StrategoGameState toUse = new StrategoGameState((StrategoGameState) info);

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

        //I CAN'T GET INTO THIS METHOD
        toUse.showBoard(boardButtons);
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

        //get timer view
        time = (TextView) activity.findViewById(R.id.timerTextView);

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

        //example set
        boardButtons[0][0].setImageResource(R.drawable.capt);

    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button){
            buttonOnClick(v);
        }else if(v instanceof ImageButton){
            imageButtonOnClick(v);
        }
    }

    public void buttonOnClick(View v){
            if(v.getId() == R.id.surrenderButton){
                sendInfo(new GameOverInfo("Player has surrendered"));
            }else if(v.getId() == R.id.endTurnButton){
                PassTurnAction newPass = new PassTurnAction(this);
                game.sendAction(newPass);
            }else{}


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


    }
}
