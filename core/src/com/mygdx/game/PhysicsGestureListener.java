package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class PhysicsGestureListener implements GestureDetector.GestureListener {
    private OrthographicCamera camera;
    private RuvikEngine ruvikEngine;

    public PhysicsGestureListener(OrthographicCamera camera, RuvikEngine engine) {
        this.camera = camera;
        this.ruvikEngine = engine;
    }

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
        if (GameUIState.getInstance().isPAN_MODE()) {
            camera.translate(new Vector2(-deltaX * camera.zoom * 10, deltaY * camera.zoom * 10));
        } else {
            Vector3 worldCoordinates = new Vector3(x, y, 0);
            camera.unproject(worldCoordinates);
            paninfo.add(new Vector2(worldCoordinates.x, worldCoordinates.y));
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (GameUIState.getInstance().isPAN_MODE()) return false;
        Vector3 worldCoordinates = new Vector3(x, y, 0);
        camera.unproject(worldCoordinates);

        Vector2 lastPos = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Vector2 firstPos = paninfo.get(0).cpy();
        Vector2 distance = firstPos.cpy().sub(lastPos);

        ruvikEngine.addBody(new Body(firstPos, distance, RuvikUtil.range(1, 5)));
        paninfo.clear();
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
}
