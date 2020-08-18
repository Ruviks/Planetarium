package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import javafx.animation.PauseTransition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    private  RuvikEngine ruvikEngine;
    private Stage stage;
    private ShapeRenderer sr;
    SpriteBatch batch;
    private int moveSpeed = 450;
    private boolean PAN_MODE=false;
    private boolean PAUSED=false;
    private float zoomSpeed = 1;
    private float accelerator = 1;
    Texture img;
    //array for our three bodies
    private static ArrayList<Body> bodyArray = new ArrayList<>();
    //some variables
    float mass = 1;
    private OrthographicCamera camera;


    public void create() {
        ruvikEngine = new RuvikEngine();
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        GestureDetector stageMain = new GestureDetector(new GestureListener() {
            ArrayList<Vector2> paninfo = new ArrayList<>();

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {

                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                if(PAN_MODE){
                    camera.translate(new Vector2(-deltaX*camera.zoom*10,deltaY*camera.zoom*10));
                    return false;
                }else{
                Vector3 worldCoordinates = new Vector3(x, y, 0);
                camera.unproject(worldCoordinates);
                paninfo.add(new Vector2(worldCoordinates.x, worldCoordinates.y));
                return false;
                }
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                if(PAN_MODE) return  false;
                Vector3 worldCoordinates = new Vector3(x, y, 0);
                camera.unproject(worldCoordinates);

                Vector2 lastpos = new Vector2(worldCoordinates.x, worldCoordinates.y);
                Vector2 firstpos = paninfo.get(0).cpy();
                Vector2 distance = firstpos.cpy().sub(lastpos);

                bodyArray.add(new Body(firstpos, distance, RuvikUtil.range(1, 5)));
                paninfo.clear();
                // System.out.println(new Vector2(deltaX,deltaY));
                return true;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

            @Override
            public void pinchStop() {

            }
        });
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        InputProcessor scroll = new InputProcessor() {

            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                accelerator = 1;
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
        camera = new OrthographicCamera(width * 10, height * 10);
        camera.position.set(width / 2, height / 2, 0);
        camera.zoom=0.1f;
        camera.update();
        stage = new Stage(new ScreenViewport());
        //
        // set up random control points
        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        batch = new SpriteBatch();
        img = new Texture("dot.png");
        TextureActor actor = new TextureActor("move.png");
        TextureActor reset = new TextureActor("reset.jpg");
        Image image = new Image(actor.texture);
        Image resetimg= new Image(reset.texture);
        image.setPosition(225,0);
        stage.addActor(image);
        stage.addActor(resetimg);
        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PAN_MODE=!PAN_MODE;
                super.clicked(event, x, y);
            }
        });
        resetimg.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bodyArray.clear();
                bodyArray.add(new Body(new Vector2(1920 / 2, 1080 / 2), new Vector2(0, 0), 1000));
                super.clicked(event, x, y);
            }
        });

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }


    public void render() {
        stage.getActors().first().act(100);
        handleInput();
        if(!PAUSED){ruvikEngine.update(Gdx.graphics.getDeltaTime());}
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
        batch.begin();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
       /* for (Body body : bodyArray
        ) {
            batch.draw(img, body.position.x, body.position.y);


        }*/
            ruvikEngine.draw(sr,batch);
        batch.end();
       /* for (Body body : bodyArray
        ) {
            for (Vector2 point : body.trail
            ) {
                sr.circle(point.x, point.y, 1);

            }
        }*/
        sr.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void handleInput() {
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
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            PAUSED=!PAUSED;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

  

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
