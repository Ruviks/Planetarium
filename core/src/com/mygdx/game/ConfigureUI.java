package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class ConfigureUI extends Stage {
    private RuvikEngine ruvikEngine;
    private OrthographicCamera camera;

    public ConfigureUI(ScreenViewport viewport, RuvikEngine engine, OrthographicCamera camera) {
        super(viewport);
        this.ruvikEngine = engine;
        this.camera = camera;
        configure();
    }

    private void configure() {
        //TODO make a scalable UI class system,so we can load each UI component independently
        TextureActor actor = new TextureActor("move.png");
        TextureActor reset = new TextureActor("reset.jpg");
        Image image = new Image(actor.texture);
        Image resetimg = new Image(reset.texture);
        image.setPosition(225, 0);
        this.addActor(image);
        this.addActor(resetimg);
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameUIState.getInstance().setPAN_MODE(!GameUIState.getInstance().isPAN_MODE());
                super.clicked(event, x, y);
            }
        });
        resetimg.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ruvikEngine.clearBodyArray();

                ruvikEngine.addBody(new Body(new Vector2(1920 / 2, 1080 / 2), new Vector2(0, 0), 1000));
                camera.position.set(GameUIState.getInstance().width / 2, GameUIState.getInstance().height / 2, 0);
                camera.zoom = 0.1f;
                camera.update();
                super.clicked(event, x, y);
            }
        });

    }

}
