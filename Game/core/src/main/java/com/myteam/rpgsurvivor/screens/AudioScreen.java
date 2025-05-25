package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.controller.system.AudioManager;

public class AudioScreen implements Screen {
    private Main game;
    private PauseScreen previousScreen;
    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private AudioManager audioManager;
    private boolean isShow;

    private Texture background;
    private Texture muteTexture;
    private Texture unmuteTexture;
    private Texture backUnActiveTexture;
    private Texture backActiveTexture;

    private ImageButton backBtn;
    private ImageButton audioBtn;
    private ImageButton soundBtn;

    public AudioScreen(Main game, PauseScreen previousScreen, OrthographicCamera camera) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.camera = camera;
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.batch = new SpriteBatch();
        this.stage = new Stage(viewport, batch);
        audioManager = AudioManager.getInstance();
        isShow = false;

        loadTexture();
        createUI();
    }

    public void loadTexture() {
        try {
            background = new Texture(Gdx.files.internal("Menu/PauseMenu/Audio.png"));
            backUnActiveTexture = new Texture(Gdx.files.internal("Menu/ChossenHero/Back_ButtonUnActive.png"));
            backActiveTexture = new Texture(Gdx.files.internal("Menu/ChossenHero/Back_ButtonActive.png"));
            unmuteTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/unmute.png"));
            muteTexture = new Texture(Gdx.files.internal("Menu/PauseMenu/mute.png"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createUI() {
        TextureRegionDrawable backUnActiveDrawable = new TextureRegionDrawable(backUnActiveTexture);
        TextureRegionDrawable backActiveDrawable = new TextureRegionDrawable(backActiveTexture);
        backBtn = new ImageButton(backUnActiveDrawable, backActiveDrawable);

        backBtn.setPosition(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 + 250);
        backBtn.setSize(100, 50);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioManager.playButtonClickSound();
                Gdx.app.log("AudioScreen", "Back button clicked");
                isShow = false;
                //game.setScreen(previousScreen);
            }
        });
        createAudioButton();
        createSoundButton();

        stage.addActor(backBtn);
        stage.addActor(audioBtn);
        stage.addActor(soundBtn);
    }

    private void createAudioButton() {
        TextureRegionDrawable currentDrawable = (audioManager.getCurrentMusic() != null && audioManager.getCurrentMusic().isPlaying()) ?
            new TextureRegionDrawable(unmuteTexture) :
            new TextureRegionDrawable(muteTexture);

        audioBtn = new ImageButton(currentDrawable);
        audioBtn.setPosition(Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 50);
        audioBtn.setSize(100, 100);

        audioBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioManager.playButtonClickSound(); // Add sound effect
                toggleMusic();
            }
        });
    }

    private void createSoundButton() {
        TextureRegionDrawable currentDrawable = audioManager.isSoundEnabled() ?
            new TextureRegionDrawable(unmuteTexture) :
            new TextureRegionDrawable(muteTexture);

        soundBtn = new ImageButton(currentDrawable);

        soundBtn.setPosition(Gdx.graphics.getWidth() / 2 + 80, Gdx.graphics.getHeight() / 2 - 50);
        soundBtn.setSize(100, 100);

        soundBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleSound();
            }
        });
    }

    private void toggleMusic() {
        if (audioManager.getCurrentMusic() != null && audioManager.getCurrentMusic().isPlaying()) {
            audioManager.pauseMusic();
            Gdx.app.log("AudioScreen", "Music paused");
        } else {
            audioManager.resumeMusic();
            Gdx.app.log("AudioScreen", "Music resumed");
        }
        updateAudioButtonTexture();
    }

    private void toggleSound() {
        boolean currentState = audioManager.isSoundEnabled();
        audioManager.setSoundEnabled(!currentState);

        if (!currentState) {
            Gdx.app.log("AudioScreen", "Sound enabled");
        } else {
            Gdx.app.log("AudioScreen", "Sound disabled");
        }
        updateSoundButtonTexture();
    }

    private void updateAudioButtonTexture() {
        stage.getActors().removeValue(audioBtn, true);
        createAudioButton();
        stage.addActor(audioBtn);
    }

    private void updateSoundButtonTexture() {
        stage.getActors().removeValue(soundBtn, true);
        createSoundButton();
        stage.addActor(soundBtn);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (isShow) {
            Gdx.input.setInputProcessor(stage);
            stage.act(delta);

            batch.begin();


            float bgWidth = background.getWidth() * 1.5f;
            float bgHeight = background.getHeight() * 1.5f;
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();

            batch.draw(background,
                (screenWidth - bgWidth) / 2,
                (screenHeight - bgHeight) / 2,
                bgWidth, bgHeight);

            batch.end();
            stage.draw();
        }
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
        isShow = false;
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (background != null) {
            background.dispose();
        }
        if (muteTexture != null) {
            muteTexture.dispose();
        }
        if (unmuteTexture != null) {
            unmuteTexture.dispose();
        }
        if (backUnActiveTexture != null) {
            backUnActiveTexture.dispose();
        }
        if (backActiveTexture != null) {
            backActiveTexture.dispose();
        }
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow)
    {
        this.isShow = isShow;
        if (isShow) {
            Gdx.input.setInputProcessor(stage);
        }
    }
}
