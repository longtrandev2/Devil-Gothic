package com.myteam.rpgsurvivor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.myteam.rpgsurvivor.screens.MainMenuScreen;
import com.myteam.rpgsurvivor.screens.MapScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private MapScreen map;
    private MainMenuScreen mainMenuScreen;

    @Override
    public void create() {
        // Khởi tạo camera
       //map = new MapScreen();
        mainMenuScreen = new MainMenuScreen();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cập nhật camera
        //map.update();
        //map.render();
        mainMenuScreen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        //map.dispose();
        mainMenuScreen.dispose();
    }
}
