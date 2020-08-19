package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class RuvikEngine {
    private Texture texture;
    private Camera camera;
    private ArrayList<Body> bodyArray = new ArrayList<>();
    float mass = 1;

    public RuvikEngine(Camera camera) {
        this.camera = camera;
        texture = new Texture("dot.png");
        bodyArray.add(new Body(new Vector2(1920 / 2, 1080 / 2), new Vector2(0, 0), 1000));
    }

    public void update(float deltaTime) {
        ArrayList<Vector2> forceArr = new ArrayList<>();
        for (int i = 0; i < bodyArray.size(); i++) {
            Vector2 totalForce = new Vector2(0, 0);
            for (int j = 0; j < bodyArray.size(); j++) {
                if (i != j) {
                    Vector2 distance = bodyArray.get(i).position.cpy().sub(bodyArray.get(j).position);
                    Vector2 normal = distance.nor();
                    Vector2 force = normal.scl(-4 * bodyArray.get(i).mass * bodyArray.get(j).mass / ( distance.len2()));
                    totalForce.add(force);
                }
            }
            forceArr.add(totalForce);
        }
        //System.out.println(forceArr);
        //apply forces / integrate
        for (int i = 0; i < bodyArray.size(); i++) {
            // System.out.println(forceArr);
            bodyArray.get(i).velocity.add(forceArr.get(i).scl(deltaTime / bodyArray.get(i).mass));
            bodyArray.get(i).position.add(bodyArray.get(i).velocity.cpy().scl(deltaTime));
            bodyArray.get(i).addTrail(bodyArray.get(i).position.cpy());
        }


    }

    public void addBody(Body body) {
        bodyArray.add(body);
    }

    public void clearBodyArray() {
        bodyArray.clear();
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Body body : bodyArray
        ) {
            batch.draw(texture, body.position.x, body.position.y);
        }
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        for (Body body : bodyArray
        ) {
            for (Vector2 point : body.trail
            ) {
                shapeRenderer.circle(point.x, point.y, 1);

            }
        }
        shapeRenderer.end();
    }

    public void dispose() {
        //clean up
    }
}
