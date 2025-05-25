package com.myteam.rpgsurvivor.saveGame;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.impl.Hero.Archer;
import com.myteam.rpgsurvivor.model.impl.Hero.Knight;
import com.myteam.rpgsurvivor.model.impl.Hero.Samurai;
import com.myteam.rpgsurvivor.model.impl.Hero.Wizard;
import com.myteam.rpgsurvivor.screens.MapScreen;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class GameSaveManager {
    private static final String SAVE_FILE_NAME = "D:\\Package IDE Java\\RPG-Game\\Game\\data\\game_save.json";
    private Gson gson;

    public GameSaveManager() {
        gson = new Gson();
    }

    public boolean saveGame(GameSaveData saveData) {
        if (saveData == null) {
            Gdx.app.error("GameSaveManager", "Invalid save data");
            return false;
        }

        try (FileWriter writer = new FileWriter(SAVE_FILE_NAME)) {
            gson.toJson(saveData, writer);
            writer.flush(); // Đảm bảo dữ liệu được ghi hết
            System.out.println("Dữ liệu đã được lưu vào file");
            return true;
        } catch (IOException e) {
            Gdx.app.error("GameSaveManager", "Error saving game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public GameSaveData loadGame() {
        try (FileReader reader = new FileReader(SAVE_FILE_NAME)) {
            return gson.fromJson(reader, GameSaveData.class);
        } catch (FileNotFoundException e) {
            Gdx.app.error("GameSaveManager", "Save file not found: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Gdx.app.error("GameSaveManager", "Error loading save file: " + e.getMessage());
            return null;
        }
    }

    public void applyLoadedData(GameSaveData saveData, MapScreen mapScreen) {
        if (saveData == null || mapScreen == null) {
            return;
        }


        try {
            switch (saveData.getHeroName()) {
                case "KNIGHT":
                    mapScreen.setChosenHero(new Knight(saveData.playerX, saveData.playerY));
                    break;
                case "SAMURAI":
                    mapScreen.setChosenHero(new Samurai(saveData.playerX, saveData.playerY));
                    break;
                case "ARCHER":
                    mapScreen.setChosenHero(new Archer(saveData.playerX, saveData.playerY));
                    break;
                case "WIZARD":
                    mapScreen.setChosenHero(new Wizard(saveData.playerX, saveData.playerY));
                    break;
            }

            mapScreen.getChosenHero().setCurrentHealth((int)saveData.getCurrentHealth());
            mapScreen.getChosenHero().setMaxHealth((int)saveData.getMaxHealth());
            mapScreen.getChosenHero().setDamage((int)saveData.getDamage());
            mapScreen.getChosenHero().setMoveSpeed(saveData.getSpeed());
            mapScreen.getChosenHero().setAttackSpeed(saveData.getAtkspeed());
            mapScreen.getChosenHero().setEntityPosition(saveData.getPlayerX(), saveData.getPlayerY());
            mapScreen.getChosenHero().setSkillPoints(saveData.getSkillPoints());



            mapScreen.getEnemySpawnController().setCurrentWave(saveData.getCurrentWave());
            mapScreen.getEnemySpawnController().setMaxEnemiesOnMap(saveData.getMaxEnemiesOnMap());
            mapScreen.getEnemySpawnController().setSpawnInterval(saveData.getSpawnInterval());
            mapScreen.getEnemySpawnController().setTimeBetweenWaves(saveData.getTimeBetweenWaves());

            mapScreen.getSystemController().setWaitingForNextStage(saveData.isWaitingForNextStage());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
