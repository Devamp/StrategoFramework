package com.example.strategotest.Stratego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.strategotest.R;
import com.example.strategotest.Stratego.Players.DumbComputerPlayer;
import com.example.strategotest.Stratego.Players.HumanPlayer;
import com.example.strategotest.Stratego.Players.SmartComputerPlayer;
import com.example.strategotest.Stratego.infoMessages.StrategoGameState;
import com.example.strategotest.game.GameFramework.GameMainActivity;
import com.example.strategotest.game.GameFramework.LocalGame;
import com.example.strategotest.game.GameFramework.gameConfiguration.GameConfig;
import com.example.strategotest.game.GameFramework.gameConfiguration.GamePlayerType;
import com.example.strategotest.game.GameFramework.infoMessage.GameState;
import com.example.strategotest.game.GameFramework.players.GamePlayer;
import com.example.strategotest.game.GameFramework.utilities.Saving;

import java.util.ArrayList;

/**
 * @author Gareth Rice
 * @author Caden Deutscher
 * @author Hewlett De Lara
 * @author Devam Patel
 *
 * @version 3/21
 *
 * Notes:
 *
 * Change Captured Pieces field in GUI to be Place Pieces at appropriate time
 * Add field to show which teams pieces have been captured
 *
 *
 * ToDo:
 *
 */

public class MainActivity extends GameMainActivity {
    public static final int PORT_NUMBER = 4444;

    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // local human GUI - as player 0
        playerTypes.add(new GamePlayerType("Human Player") {
            public GamePlayer createPlayer(String name) {
                return new HumanPlayer(name, R.layout.activity_main, 0);
            }
        });

        // dumb computer player
        playerTypes.add(new GamePlayerType("Dumb Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new DumbComputerPlayer(name);
            }
        });

        // smarter computer player
        playerTypes.add(new GamePlayerType("Smart Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new SmartComputerPlayer(name);
            }
        });

        // Create a game configuration class for Stratego
        GameConfig defaultConfig = new GameConfig(playerTypes, 2,2, "Stratego", PORT_NUMBER);

        // Add the default players
        defaultConfig.addPlayer("Human", 0); // yellow-on-blue GUI
        defaultConfig.addPlayer("Computer", 1); // dumb computer player

        // Set the initial information for the remote player
        defaultConfig.setRemoteData("Remote Player", "", 1); // red-on-yellow GUI

        //done!
        return defaultConfig;
    }

    @Override
    public LocalGame createLocalGame(GameState gameState) {
        if(gameState == null) return new StrategoLocalGame();
        return new StrategoLocalGame((StrategoGameState) gameState);
    }

    /**
     * saveGame, adds this games prepend to the filename
     *
     * @param gameName
     * 				Desired save name
     * @return String representation of the save
     */

    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
    }

    /**
     * loadGame, adds this games prepend to the desire file to open and creates the game specific state
     * @param gameName
     * 				The file to open
     * @return The loaded GameState
     */
    @Override
    public GameState loadGame(String gameName){
        String appName = getGameString(gameName);
        super.loadGame(appName);
        return (GameState) new StrategoGameState((StrategoGameState) Saving.readFromFile(appName, this.getApplicationContext()));
    }


//    /**
//     * onCreate: setup the project
//     *
//     * @param savedInstanceState
//     */
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.id.layout.activity_main);
//
//        EditText mainText = (EditText) findViewById(R.id.mainText);
//
//        //Create a test piece
//        Piece spy = new Piece("Spy", 0, 1);
//
//        //Setup runTestBtn and it's onClick method
//        Button runTestBtn = (Button) findViewById(R.id.runTestButton);
//        runTestBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //clear the edit text
//                mainText.setText("");
//
//
//                StrategoGameState firstInstance = new StrategoGameState();
//
//                StrategoGameState secondInstance = new StrategoGameState(firstInstance);
//
//                StrategoGameState thirdInstance = new StrategoGameState();
//
//                StrategoGameState fourthInstance = new StrategoGameState(thirdInstance);
//
//                firstInstance.setPhase(firstInstance, 1); //set phase to game play phase
//
//                //call toString
//                mainText.append(firstInstance.movePrint(6,0,5,0));
//                mainText.append("\n\n");
//                mainText.append(firstInstance.printBoard());
//                mainText.append("\n");
//
//                mainText.append(firstInstance.movePrint(5,0,5,1));
//                mainText.append("\n\n");
//                mainText.append(firstInstance.printBoard());
//                mainText.append("\n");
//
//                mainText.append(firstInstance.movePrint(5,1,5,2));
//                mainText.append("\n\n");
//                mainText.append(firstInstance.printBoard());
//                mainText.append("\n");
//                //Diagonal test should never work
//                mainText.append(firstInstance.movePrint(6,9,5,8));
//                mainText.append("\n\n");
//                mainText.append(firstInstance.printBoard());
//                mainText.append("\n");
//                //Multi-space move test should work for scout
//                mainText.append(firstInstance.movePrint(6,8,4,8));
//                mainText.append("\n\n");
//                mainText.append(firstInstance.printBoard());
//                mainText.append("\n");
//                //take test(should work unless bomb or flag)
//                mainText.append(firstInstance.movePrint(6,5,5,5));
//                mainText.append("\n\n");
//                mainText.append(firstInstance.printBoard());
//                mainText.append("\n");
//                mainText.append(firstInstance.movePrint(5,5,4,5));
//                mainText.append("\n\n");
//                mainText.append(firstInstance.printBoard());
//                mainText.append("\n");
//                mainText.append(firstInstance.movePrint(4,5,3,5));
//                mainText.append("\n\n");
//                mainText.append(firstInstance.printBoard());
//                mainText.append("\n");
//
//                //print second instance
//                mainText.append("\n");
//                mainText.append("[SECOND INSTANCE]");
//                mainText.append("\n");
//                mainText.append(secondInstance.toString());
//                mainText.append("\n");
//                mainText.append(secondInstance.printBoard());
//
//                //print fourth instance
//                mainText.append("\n");
//                mainText.append("\n");
//                mainText.append("[FOURTH INSTANCE]");
//                mainText.append("\n");
//                mainText.append(fourthInstance.toString());
//                mainText.append("\n");
//                mainText.append(fourthInstance.printBoard());
//                mainText.append("\n");
//
//                //testing endTurn method
//                mainText.append("******* Player 1 ends their turn. Re-print the game state *******\n");
//                mainText.append("\n");
//                mainText.append("Before player 1 ends turn:\n");
//                mainText.append("\n");
//                mainText.append(secondInstance.printBoard());
//                mainText.append("\n");
//                secondInstance.endTurn(secondInstance);
//                mainText.append("After player 1 has ended their turn: \n");
//                mainText.append("\n");
//                StrategoGameState tester = new StrategoGameState(secondInstance);
//                mainText.append(tester.printBoard());
//                mainText.append("\n");
//                mainText.append(tester.toString());
//
//
//            }
//        });
//    }


}