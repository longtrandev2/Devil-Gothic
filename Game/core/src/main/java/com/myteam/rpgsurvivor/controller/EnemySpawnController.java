package com.myteam.rpgsurvivor.controller;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
import com.myteam.rpgsurvivor.controller.spawn.SpawnPointManager;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.impl.Creep.Goblin;
import com.myteam.rpgsurvivor.model.impl.Creep.Rat;
import com.myteam.rpgsurvivor.model.impl.Creep.Skeleton;
import java.util.ArrayList;

public class EnemySpawnController {
    private SpawnPointManager spawnPointManager;
    private ArrayList<Enemy> activeEnemy;
    private Player player;
    private AnimationForEnemy enemyAnimation;


    private float spawnTimer;
    private float spawnInterval;
    private int maxEnemiesOnMap;
    private int enemiesPerWave;

    private int currentWave;
    private float waveTimer;
    private float timeBetweenWaves;

    public EnemySpawnController(Player player, TiledMap map) {
        this.player = player;
        this.spawnPointManager = new SpawnPointManager();
        this.spawnPointManager.loadFromMap(map);
        this.activeEnemy = new ArrayList<>();
        this.enemyAnimation = new AnimationForEnemy();

        this.spawnInterval = 2.0f;
        this.spawnTimer = 0;
        this.maxEnemiesOnMap = 40;
        this.enemiesPerWave = 1;

        this.currentWave = 1;
        this.waveTimer = 0;
        this.timeBetweenWaves = 30.0f;
    }

    public void update(float deltaTime)
    {
        waveTimer += deltaTime;
        if(waveTimer >= timeBetweenWaves)
        {
            startNewWave();
        }

        if(activeEnemy.size() < maxEnemiesOnMap)
        {
            spawnTimer += deltaTime;

            if(spawnTimer >= spawnInterval)
            {
                spawnTimer = 0;
                spawnEnemy();
            }
        }
        updateEnemy(deltaTime);
    }

    public void startNewWave()
    {
        ++currentWave;
        waveTimer = 0;

        for (int i = 0 ; i < enemiesPerWave ; ++i)
        {
            if(activeEnemy.size() < maxEnemiesOnMap)
            {
                spawnEnemy();
            }
        }

        spawnInterval = Math.max(0.5f, spawnInterval * 0.9f);
        enemiesPerWave = Math.min(maxEnemiesOnMap / 2, enemiesPerWave + 1);
    }

    public void spawnEnemy()
    {
        Vector2 spawnPos = spawnPointManager.getRandomSpawnPosition();
        Enemy enemy = createRandomEnemy(spawnPos.x, spawnPos.y);
        activeEnemy.add(enemy);
    }

    public Enemy createRandomEnemy (float x, float y)
    {
        MonsterType[] types = MonsterType.values();
        //MonsterType randomType = types[MathUtils.random(types.length - 1)];
        MonsterType randomType = MonsterType.GOBLIN;

        Enemy randomEnemy = createEnemyByType(randomType, x, y);
        return randomEnemy;
    }

    private Enemy createEnemyByType(MonsterType type, float x, float y) {
        switch (type) {
            case GOBLIN:
                return new Goblin(x, y, player, enemyAnimation);
            case SKELETON:
                return new Skeleton(x, y, player, enemyAnimation);
            case RAT:
                return new Rat(x, y, player, enemyAnimation);
            default:
                return new Goblin(x, y, player, enemyAnimation);
        }
    }

    private void updateEnemy(float deltaTime) {
        for (int i = activeEnemy.size() - 1; i >= 0; i--) {
            Enemy enemy = activeEnemy.get(i);

            enemy.update(deltaTime);

            if (enemy.isDead()) {
                activeEnemy.remove(i);
            }
        }
    }


    public void render(SpriteBatch batch, float deltaTime) {
        for (Enemy enemy : activeEnemy) {
            enemy.render(batch, deltaTime);
        }
    }

    public ArrayList<Enemy> getActiveEnemies() {
        return activeEnemy;
    }

    public void setMaxEnemiesOnMap(int maxEnemiesOnMap) {
        this.maxEnemiesOnMap = maxEnemiesOnMap;
    }

    public void setSpawnInterval(float spawnInterval) {
        this.spawnInterval = spawnInterval;
    }

    public void setTimeBetweenWaves(float timeBetweenWaves) {
        this.timeBetweenWaves = timeBetweenWaves;
    }

    public int getCurrentWave() {
        return currentWave;
    }
}

