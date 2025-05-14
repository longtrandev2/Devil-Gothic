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

    private int totalDeath = 0;
    private float spawnInterval;
    private float spawnTimer;
    private int maxEnemiesOnMap;
    private int enemiesPerWave;

    private int currentWave;
    private float waveTimer;
    private float timeBetweenWaves;

    private boolean prepareToNextStage;
    private boolean isPaused;

    public EnemySpawnController(Player player, TiledMap map) {
        this.player = player;
        this.spawnPointManager = new SpawnPointManager();
        this.spawnPointManager.loadFromMap(map);
        this.activeEnemy = new ArrayList<>();
        this.enemyAnimation = new AnimationForEnemy();

        this.spawnInterval = 2.0f;
        this.spawnTimer = 0;
        this.maxEnemiesOnMap = 100;
        this.enemiesPerWave = 5;

        this.currentWave = 1;
        this.waveTimer = 0;
        this.timeBetweenWaves = 30.0f;

        this.prepareToNextStage = false;
        this.isPaused = false;
    }

    public void update(float deltaTime) {
        if (isPaused) {
            return;
        }


        if (totalDeath >= enemiesPerWave) {
            System.out.println(totalDeath);
            prepareToNextStage = true;
            return;
        }

        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval && activeEnemy.size() < maxEnemiesOnMap) {
            spawnEnemy();
            spawnTimer = 0;
        }

        updateEnemy(deltaTime);
    }

    public void startNewWave() {
        ++currentWave;
        waveTimer = 0;
        spawnEnemy();
        spawnInterval = Math.max(0.5f, spawnInterval * 0.9f);
    }

    public void spawnEnemy() {
        Vector2 spawnPos = spawnPointManager.getRandomSpawnPosition();
        Enemy enemy = createRandomEnemy(spawnPos.x, spawnPos.y);
        activeEnemy.add(enemy);
    }

    public Enemy createRandomEnemy(float x, float y) {
        MonsterType[] types = MonsterType.values();
        MonsterType randomType = types[MathUtils.random(types.length - 1)];
        //MonsterType randomType = MonsterType.SKELETON;

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
                totalDeath++;
                if (totalDeath >= enemiesPerWave) {
                    prepareToNextStage = true;
                }
            }
        }
    }

    public void render(SpriteBatch batch, float deltaTime) {
        for (Enemy enemy : activeEnemy) {
            enemy.render(batch, deltaTime);
        }
    }

    public boolean isPrepareToNextStage() {
        return prepareToNextStage;
    }

    public void pauseSpawning() {
        isPaused = true;
    }

    public void resumeSpawning() {
        prepareToNextStage = false;
        isPaused = false;
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

    public float getTimeBetweenWaves() {
        return timeBetweenWaves;
    }

    public int getMaxEnemiesOnMap() {
        return maxEnemiesOnMap;
    }

    public float getSpawnInterval() {
        return spawnInterval;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getEnemiesPerWave() {
        return enemiesPerWave;
    }

    public void setEnemiesPerWave(int enemiesPerWave) {
        this.enemiesPerWave = enemiesPerWave;
    }

    public int getTotalDeaths() {
        return totalDeath;
    }

    public void setTotalDeaths(int totalDeath) {
        this.totalDeath = totalDeath;
    }
}
