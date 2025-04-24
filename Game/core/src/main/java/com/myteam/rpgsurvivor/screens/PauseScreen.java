package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PauseScreen {
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;

    // Screen
    private Texture backgroundTexture;
    private Texture titleTexture;
    private Texture nameTexture;

    // Home Button
    private Texture homeTexture;
    private ImageButton homeButton;

    // Resume Button
    private Texture resumeTexture;
    private ImageButton resumeButton;

    // Restart Button
    private Texture restartTexture;
    private ImageButton restartButton;

    //Interface resolve event
    private PauseScreenListener listener ;

    // Size parameters
    private float padding = 20;
    private float buttonSize = 200;
    private float titleWidth = 800;
    private float titleHeight = 200;

    public PauseScreen (OrthographicCamera camera)
    {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);

        try {
            backgroundTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/Carved_9Slides.png"));
            titleTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/Banner_Horizontal.png"));
            homeTexture = new Texture((Gdx.files.internal("Menu/PauseMenu/UI_TravelBook_IconHome01a.png")));
            resumeTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/UI_TravelBook_IconPlay01a.png"));
            restartTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/UI_TravelBook_IconRestart01a.png"));
            nameTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/GameName.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        createMenu();

        Gdx.input.setInputProcessor(stage);
    }

    public void createMenu()
    {
        TextureRegionDrawable homeDrawable = new TextureRegionDrawable(homeTexture);
        homeButton = new ImageButton(homeDrawable);
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onHomeButtonClicked();
                }
            }
        });

        TextureRegionDrawable resumeDrawable = new TextureRegionDrawable(resumeTexture);
        resumeButton = new ImageButton(resumeDrawable);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onResumeButtonClicked();
                }
            }
        });

        TextureRegionDrawable restartDrawable = new TextureRegionDrawable(restartTexture);
        restartButton = new ImageButton(restartDrawable);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onRestartButtonClicked();
                }
            }
        });

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float centerX = screenWidth / 2;
        float centerY = screenHeight / 2;

        float totalButtonsWidth = (3 * buttonSize) + (2 * padding); // 3 buttons with padding between
        float buttonsStartX = centerX - (totalButtonsWidth / 2);
        float buttonsY = centerY - buttonSize - padding + 100;

        homeButton.setSize(buttonSize, buttonSize);
        homeButton.setPosition(buttonsStartX, buttonsY);

        resumeButton.setSize(buttonSize, buttonSize);
        resumeButton.setPosition(buttonsStartX + buttonSize + padding + 20, buttonsY);

        restartButton.setSize(buttonSize, buttonSize);
        restartButton.setPosition(buttonsStartX + (2 * (buttonSize + padding)), buttonsY);

        stage.addActor(homeButton);
        stage.addActor(resumeButton);
        stage.addActor(restartButton);

    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float bgWidth = screenWidth * 0.8f;
        float bgHeight = screenHeight * 0.8f;
        float bgX = (screenWidth - bgWidth) / 2;
        float bgY = (screenHeight - bgHeight) / 2;

        batch.draw(backgroundTexture, bgX, bgY, bgWidth, bgHeight);

        float titleX = (screenWidth - titleWidth) / 2;
        float titleY = bgY + bgHeight - titleHeight - padding;

        batch.draw(titleTexture, titleX, titleY, titleWidth, titleHeight);

        float nameWidth = 600;
        float nameHeight = 100;
        float nameX = (screenWidth - nameWidth) / 2;
        float nameY = titleY + titleHeight + padding - 150;

        batch.draw(nameTexture, nameX, nameY, nameWidth, nameHeight);

        batch.end();
        stage.draw();
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        titleTexture.dispose();
        homeTexture.dispose();
        resumeTexture.dispose();
        restartTexture.dispose();
    }

    public void setListener(PauseScreenListener listener)
    {
        this.listener = listener;
    }


}
