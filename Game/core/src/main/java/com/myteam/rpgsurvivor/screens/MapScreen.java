package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.impl.Hero.Archer;
import com.myteam.rpgsurvivor.model.impl.Hero.Knight;
import com.myteam.rpgsurvivor.model.impl.Hero.Samurai;
import com.myteam.rpgsurvivor.model.impl.Hero.Wizard;

import java.awt.*;


public class MapScreen {
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player chosenHero;
    private LayoutPlayScreen layoutPlayScreen;

    public MapScreen() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        try {
            batch = new SpriteBatch();
            chosenHero = new Samurai(300,300);
            layoutPlayScreen = new LayoutPlayScreen(camera,chosenHero);
        } catch (Exception e) {
            Gdx.app.error("MapScreen", "Error initializing: " + e.getMessage());
            e.printStackTrace();
        }

        loadMap();
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

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();




        if (tiledMapRenderer != null) {
            tiledMapRenderer.setView(camera);
            tiledMapRenderer.render();
        }


        batch.begin();

        chosenHero.render(batch, Gdx.graphics.getDeltaTime());

        batch.end();
        layoutPlayScreen.render();




    }

    public void update() {
        camera.update();
        chosenHero.update();
        System.out.println("Hero position: " + chosenHero.getEntityX() + ", " + chosenHero.getEntityY());

    }

    public void dispose() {
        if (map != null) {
            map.dispose();
        }
        batch.dispose();


    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
