package com.myteam.entities;

public class Enemy {
    public Vector2D position;
    public float speed = 1.5f;

    public Enemy(float x, float y) {
        position = new Vector2D(x, y);
    }

    public void update(Vector2D playerPosition) {
        position.moveTowards(playerPosition, speed);
    }
}
