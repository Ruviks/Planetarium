package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

public class Body {
    public LinkedList<Vector2> trail = new LinkedList<>();
    public Vector2 position;
    public Vector2 velocity;
    public int trailLength = 100;
    public float mass;

    public Body(Vector2 position, Vector2 velocity, float mass) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
    }

    public void addTrail(Vector2 point) {
        if (trail.size() > trailLength) trail.remove();
        trail.add(point);


    }

}
