package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
    private RuvikEngine ruvikEngine;
    private Stage stageUI;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private float zoomSpeed = 1;
    private OrthographicCamera camera;
    private int width;
    private int height;

    public void create() {
        width = GameUIState.getInstance().width;
        height = GameUIState.getInstance().height;
        final int worldSize = 10;
        //configure camera
        camera = new OrthographicCamera(width * worldSize, height * worldSize);
        camera.position.set(width / 2, height / 2, 0);
        camera.zoom = 0.1f;
        camera.update();
        //start the engine
        ruvikEngine = new RuvikEngine(camera);
        //configure stageUI
        stageUI = new ConfigureUI(new ScreenViewport(), ruvikEngine, camera);
        //Configure Input handlers
        new KeyboardInputHandlerContinous(camera);
        GestureDetector stageMain = new GestureDetector(new PhysicsGestureListener(camera, ruvikEngine));
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        InputProcessor scroll = new InputProcessor() {

            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                KeyboardInputHandlerContinous.accelerator = 1;
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                camera.zoom += amount * zoomSpeed * 3.5 * Gdx.graphics.getDeltaTime();
                return false;
            }
        };
        inputMultiplexer.addProcessor(stageMain);
        inputMultiplexer.addProcessor(scroll);
        inputMultiplexer.addProcessor(stageUI);
        Gdx.input.setInputProcessor(inputMultiplexer);

        //Configure Batch and ShapeRenderer
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        spriteBatch = new SpriteBatch();
    }


    public void render() {
        //handle continuous input
        KeyboardInputHandlerContinous.handleInput();
        //update physics if not paused
        if (!GameUIState.getInstance().isPAUSED()) {
            ruvikEngine.update(Gdx.graphics.getDeltaTime());
        }
        //draw
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        camera.update();
        ruvikEngine.draw(shapeRenderer, spriteBatch);
        stageUI.act(Gdx.graphics.getDeltaTime());
        stageUI.draw();
    }

    public void resize(int width, int height) {
        stageUI.getViewport().update(width, height, true);
    }

    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        ruvikEngine.dispose();
    }
}
