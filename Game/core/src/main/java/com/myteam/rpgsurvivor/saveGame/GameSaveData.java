package com.myteam.rpgsurvivor.saveGame;

import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.screens.MapScreen;

import java.util.ArrayList;


public class GameSaveData {
    //Attributes Hero
    public String heroName;
    public float currentHealth;
    public float maxHealth;
    public float damage;
    public float speed;
    public float atkspeed;
    public float playerX;
    public float playerY;
    public int skillPoints;
    public InputHandle inputHandle;
    public HeroMovement heroMovement;


    //Attributes SystemController
    public int currentWave;
    public boolean isWaitingForNextStage;

    //Attributes EnemySpawn
    public int maxEnemiesOnMap;
    public float spawnInterval;
    public float timeBetweenWaves;
    public ArrayList activeEnemies;
    public ArrayList activeBosses;



    public GameSaveData(MapScreen mapScreen) {
        if(mapScreen.getChosenHero() != null)
        {
            this.heroName = mapScreen.getChosenHero().getHeroType().name();
            this.currentHealth = mapScreen.getChosenHero().getCurrentHealth();
            this.maxHealth = mapScreen.getChosenHero().getMaxHealth();
            this.damage = mapScreen.getChosenHero().getDamage();
            this.speed = mapScreen.getChosenHero().getMoveSpeed();
            this.atkspeed = mapScreen.getChosenHero().getAttackSpeed();
            this.playerX = mapScreen.getChosenHero().getEntityX();
            this.playerY = mapScreen.getChosenHero().getEntityY();
            //this.skillPoints = mapScreen.getChosenHero().getSkillPoints();
//            this.inputHandle = mapScreen.getChosenHero().getInputHandle();
//            this.heroMovement = mapScreen.getChosenHero().getHeroMovement();
        }

        if(mapScreen.getEnemySpawnController() != null)
        {
            this.currentWave = mapScreen.getEnemySpawnController().getCurrentWave();
            this.maxEnemiesOnMap = mapScreen.getEnemySpawnController().getMaxEnemiesOnMap();
            this.spawnInterval = mapScreen.getEnemySpawnController().getSpawnInterval();
            this.timeBetweenWaves = mapScreen.getEnemySpawnController().getTimeBetweenWaves();
//            this.activeEnemies = mapScreen.getEnemySpawnController().getActiveEnemies();
//            this.activeBosses = mapScreen.getEnemySpawnController().getActiveBoss();
        }

        if(mapScreen.getSystemController() != null)
        {
            this.isWaitingForNextStage = mapScreen.getSystemController().isWaitingForNextStage();
        }
    }

    public String getHeroName() {
        return heroName;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }

    public float getAtkspeed() {
        return atkspeed;
    }

    public InputHandle getInputHandle() {
        return inputHandle;
    }

    public HeroMovement getHeroMovement() {
        return heroMovement;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public boolean isWaitingForNextStage() {
        return isWaitingForNextStage;
    }

    public int getMaxEnemiesOnMap() {
        return maxEnemiesOnMap;
    }

    public float getSpawnInterval() {
        return spawnInterval;
    }

    public float getTimeBetweenWaves() {
        return timeBetweenWaves;
    }

    public ArrayList getActiveEnemies() {
        return activeEnemies;
    }

    public ArrayList getActiveBosses() {
        return activeBosses;
    }

    public float getPlayerX() {
        return playerX;
    }


    public float getPlayerY() {
        return playerY;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    @Override
    public String toString() {
        return "GameSaveData{" +
            "heroName='" + heroName + '\'' +
            ", currentHealth=" + currentHealth +
            ", maxHealth=" + maxHealth +
            ", damage=" + damage +
            ", speed=" + speed +
            ", atkspeed=" + atkspeed +
            ", inputHandle=" + (inputHandle != null ? inputHandle.toString() : "null") +
            ", heroMovement=" + (heroMovement != null ? heroMovement.toString() : "null") +
            ", currentWave=" + currentWave +
            ", isWaitingForNextStage=" + isWaitingForNextStage +
            ", maxEnemiesOnMap=" + maxEnemiesOnMap +
            ", spawnInterval=" + spawnInterval +
            ", timeBetweenWaves=" + timeBetweenWaves +
            ", activeEnemies=" + (activeEnemies != null ? activeEnemies.toString() : "null") +
            ", activeBosses=" + (activeBosses != null ? activeBosses.toString() : "null") +
            '}';
    }
}
