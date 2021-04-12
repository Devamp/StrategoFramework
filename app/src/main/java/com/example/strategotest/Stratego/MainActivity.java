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
 * @version 3/21
 * <p>
 * Notes:
 * <p>
 * Change Captured Pieces field in GUI to be Place Pieces at appropriate time
 * Add field to show which teams pieces have been captured
 * <p>
 * <p>
 * ToDo:
 */

public class MainActivity extends GameMainActivity {

    private static final String TAG = "MainActivity";
    public static final int PORT_NUMBER = 4444;

    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // local human GUI - as player 0
        playerTypes.add(new GamePlayerType("Local Human Player") {
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
        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 2, "Stratego", PORT_NUMBER);

        // Add the default players
        defaultConfig.addPlayer("Human", 0);
        defaultConfig.addPlayer("Computer", 1);

        // Set the initial information for the remote player
        defaultConfig.setRemoteData("Remote Player", "", 1);

        //done!
        return defaultConfig;
    }

    @Override
    public LocalGame createLocalGame(GameState gameState) {
        if (gameState == null) return new StrategoLocalGame();
        return new StrategoLocalGame((StrategoGameState) gameState);
    }

    /**
     * saveGame, adds this games prepend to the filename
     *
     * @param gameName Desired save name
     * @return String representation of the save
     */

    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
    }

    /**
     * loadGame, adds this games prepend to the desire file to open and creates the game specific state
     *
     * @param gameName The file to open
     * @return The loaded GameState
     */
    @Override
    public GameState loadGame(String gameName) {
        String appName = getGameString(gameName);
        super.loadGame(appName);
        return (GameState) new StrategoGameState((StrategoGameState) Saving.readFromFile(appName, this.getApplicationContext()));
    }
}