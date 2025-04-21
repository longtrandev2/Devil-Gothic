package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.rpgsurvivor.model.EnemyManager;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.impl.Hero.Archer;
import com.myteam.rpgsurvivor.model.impl.Hero.Knight;
import com.myteam.rpgsurvivor.model.impl.Hero.Samurai;
import com.myteam.rpgsurvivor.model.impl.Hero.Wizard;


public class MapScreen extends ScreenAdapter {
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Samurai samurai;
    private Knight knight;
    private Wizard wizard;
    private Archer archer;

    private Player player; // Sử dụng kiểu abstract Player
    private EnemyManager enemyManager;

    public MapScreen() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        try {


            batch = new SpriteBatch();
            samurai = new Samurai(100,100,100,100,200);
            knight = new Knight(200,200,100,100,200);
            wizard = new Wizard(400,400,100,100,200);
            archer = new Archer(300,300,100,100,200);

            player = samurai;
            Gdx.app.log("MapScreen", "Player created at: " + player.getX() + ", " + player.getY());
            enemyManager = new EnemyManager(player); // Truyền player vào
            Gdx.app.log("MapScreen", "EnemyManager created.");

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
        batch.setProjectionMatrix(camera.combined);


        batch.begin();
        player.render(batch,Gdx.graphics.getDeltaTime());
        enemyManager.render(batch);
        samurai.render(batch, Gdx.graphics.getDeltaTime());
        knight.render(batch, Gdx.graphics.getDeltaTime());
        wizard.render(batch, Gdx.graphics.getDeltaTime());
        archer.render(batch, Gdx.graphics.getDeltaTime());
        batch.end();
    }

    public void update() {
        camera.update();
        samurai.update();
        knight.update();
        wizard.update();
        archer.update();

        player.update();
        enemyManager.update(Gdx.graphics.getDeltaTime());
        camera.position.scl(player.getPosition().x,player.getPosition().y,0);
        camera.update();
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
