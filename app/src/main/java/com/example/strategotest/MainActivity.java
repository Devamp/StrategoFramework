package com.example.strategotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.strategotest.game.GameFramework.GameMainActivity;
import com.example.strategotest.game.GameFramework.LocalGame;
import com.example.strategotest.game.GameFramework.gameConfiguration.GameConfig;
import com.example.strategotest.game.GameFramework.infoMessage.GameState;
import com.example.strategotest.game.GameFramework.infoMessage.gamestate.StrategoGameState;

public class MainActivity extends GameMainActivity {

    // We need to override the abstract methods from GameMainActivity
    // We can see what that looks like in the TTT mainActivity.

    /**
     * stratego is for 2 human players, but one can also be a computer
     */
    @Override
    public GameConfig createDefaultConfig() {
        return null;
    }

    /**
     * createLocalGame
     *
     * Creates a new game that runs on the server tablet,
     * @param gameState
     * 				the gameState for this game or null for a new game
     *
     * @return a new, game-specific instance of a sub-class of the LocalGame
     *         class.
     */
    @Override
    public LocalGame createLocalGame(GameState gameState) {
        if(gameState == null){
            return new StrategoLocalGame();
        }
        return new StrategoLocalGame((StrategoGameState)gameState);
    }


}