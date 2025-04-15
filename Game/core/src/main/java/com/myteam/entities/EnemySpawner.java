package com.myteam.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner {
    private float mapWidth, mapHeight;
    private Random rand = new Random();

    public EnemySpawner(float mapWidth, float mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public List<Enemy> spawnEnemies(int count) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float x = rand.nextFloat() * mapWidth;
            float y = rand.nextFloat() * mapHeight;
            enemies.add(new Enemy(x, y));
        }
        return enemies;
    }
}
