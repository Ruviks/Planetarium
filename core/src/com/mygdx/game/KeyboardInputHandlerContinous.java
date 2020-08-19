package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class KeyboardInputHandlerContinous {
    private static int moveSpeed = 450;
    public static float accelerator = 1;
    private static float zoomSpeed = 1;
    private static OrthographicCamera camera;

    public KeyboardInputHandlerContinous(OrthographicCamera camera) {
        KeyboardInputHandlerContinous.camera = camera;
    }

    public static void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            accelerator += 0.5;
            camera.translate(0, moveSpeed * accelerator * Gdx.graphics.getDeltaTime());
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            accelerator += 0.5;
            camera.translate(0, -moveSpeed * accelerator * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            accelerator += 0.5;
            camera.translate(-moveSpeed * accelerator * Gdx.graphics.getDeltaTime(), 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            accelerator += 0.5;
            camera.translate(moveSpeed * accelerator * Gdx.graphics.getDeltaTime(), 0);
        }

        // zoom camera
        if (Gdx.input.isKeyPressed((Input.Keys.UP))) {
            camera.zoom -= zoomSpeed * Gdx.graphics.getDeltaTime();
        } else if (Gdx.input.isKeyPressed((Input.Keys.DOWN))) {
            camera.zoom += zoomSpeed * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            GameUIState.getInstance().setPAUSED(!GameUIState.getInstance().isPAUSED());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
