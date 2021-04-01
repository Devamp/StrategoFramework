package com.example.strategotest.Stratego.Players;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.strategotest.R;
import com.example.strategotest.Stratego.MainActivity;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.GameMainActivity;
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
    }

    public void turnMsg(String msg){
        MessageBox.popUpMessage(msg, myActivity);
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
        }
        //setGameOver(true);
    }

    public void imageButtonOnClick(View v){

    }
}
