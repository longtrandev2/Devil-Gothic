package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.controller.EnemySpawnController;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.impl.Hero.Archer;
import com.myteam.rpgsurvivor.model.impl.Hero.Knight;
import com.myteam.rpgsurvivor.model.impl.Hero.Samurai;
import com.myteam.rpgsurvivor.model.impl.Hero.Wizard;

import java.awt.*;


public class MapScreen implements Screen {
    private Main game;
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player chosenHero;
    private LayoutPlayScreen layoutPlayScreen;
    private EnemySpawnController enemySpawnController;

    private boolean debugEnabled = false;

    private String heroType;

    public MapScreen(String heroType, Main game) {
        this.game = game;
        this.heroType = heroType;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        try {
            batch = new SpriteBatch();

            switch (heroType)
            {
                case "Knight" :
                    chosenHero = new Knight(400, 400);
                    break;
                case "Samurai" :
                    chosenHero = new Samurai(400,400);
                    break;
                case "Archer" :
                    chosenHero = new Archer(400, 400);
                    break;
                case "Wizard" :
                    chosenHero = new Wizard(400, 400);
                    break;
                default:
                    chosenHero = new Knight(400,400);
                    break;
            }

        } catch (Exception e) {
            Gdx.app.error("MapScreen", "Error initializing: " + e.getMessage());
            e.printStackTrace();
        }

        loadMap();
        enemySpawnController = new EnemySpawnController(chosenHero, map);

        enemySpawnController.setMaxEnemiesOnMap(10);
        enemySpawnController.setSpawnInterval(3.0f);
        enemySpawnController.setTimeBetweenWaves(45.0f);

        chosenHero.setEnemySpawnController(enemySpawnController);

        layoutPlayScreen = new LayoutPlayScreen(camera,chosenHero,heroType,game);
    }

    public void loadMap() {
        try {
            map = new TmxMapLoader().load("Map Asset/Map Final.tmx");
            tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        } catch (Exception e) {
            Gdx.app.error("Map Loading", "Error loading map: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void update() {
        camera.update();
        if (!isPaused()) {
            chosenHero.update(Gdx.graphics.getDeltaTime());
        }
        enemySpawnController.update(Gdx.graphics.getDeltaTime());
        //System.out.println("Hero position: " + chosenHero.getEntityX() + ", " + chosenHero.getEntityY());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
          if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            DebugRenderer.setEnabled(!debugEnabled);
            debugEnabled = !debugEnabled;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        if (tiledMapRenderer != null) {
            tiledMapRenderer.setView(camera);
            tiledMapRenderer.render();
        }

        batch.begin();


        if (!isPaused()) {
            chosenHero.render(batch, Gdx.graphics.getDeltaTime());
            if (enemySpawnController != null) {
                enemySpawnController.render(batch, Gdx.graphics.getDeltaTime());
            }
        }


        batch.end();
        DebugRenderer.render();

        layoutPlayScreen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose() {
        if (map != null) {
            map.dispose();
        }
        batch.dispose();
        layoutPlayScreen.dispose();


    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public boolean isPaused() {
        return layoutPlayScreen != null && layoutPlayScreen.isPaused();
    }
}
