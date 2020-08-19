package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class GameUIState {
    private static GameUIState instance = null;
    private boolean PAN_MODE = false;
    private boolean PAUSED = false;
    public int width;
    public int height;

    private GameUIState() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        // Exists only to defeat instantiation.
    }

    public static GameUIState getInstance() {
        if (instance == null) {
            instance = new GameUIState();
        }
        return instance;
    }

    public boolean isPAUSED() {
        return PAUSED;
    }

    public void setPAUSED(boolean PAUSED) {
        this.PAUSED = PAUSED;
    }

    public boolean isPAN_MODE() {
        return PAN_MODE;
    }

    public void setPAN_MODE(boolean PAN_MODE) {
        this.PAN_MODE = PAN_MODE;
    }
}
