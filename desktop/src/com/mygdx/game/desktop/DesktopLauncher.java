package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title="Planetarium ";
        config.width = 1920;
        config.height = 1080;

        // fullscreen
        //config.fullscreen = true;
        new LwjglApplication(new MyGdxGame(), config);
    }
}
