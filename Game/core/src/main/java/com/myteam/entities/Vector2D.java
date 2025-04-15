package com.myteam.entities;

public class Vector2D {
    public float x, y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void moveTowards(Vector2D target, float speed) {
        float dx = target.x - x;
        float dy = target.y - y;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length != 0) {
            x += dx / length * speed;
            y += dy / length * speed;
        }
    }
}
