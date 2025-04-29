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
import com.myteam.rpgsurvivor.model.Player;

public class LayoutPlayScreen {
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private Player chosenHero;
    private PauseScreen pauseScreen;

    //Pause Button
    private Texture pauseUnactiveTexture;
    private Texture pauseActiveTexture;
    private ImageButton pauseButton;

    // UI Interface blood  and hero avatar
    private Texture heroAvatar;
    private Texture frameAvatar;
    private Texture frameBlood;
    private Texture bloodTexture;

    private TextureRegion[] bloodFrames;

    private float avatarFrameSize = 80;
    private float bloodBarWidth = 200;
    private float bloodBarHeight = 40;
    private float padding = 10;
    private float bloodBarInnerPaddingX = 6;
    private float bloodBarInnerPaddingY = 6;

    private float maxHealth ;
    private float currentHealth ;

    private static final int BLOOD_FRAME_COLS = 6;
    private static final int BLOOD_FRAME_ROWS = 1;
    private int currentBloodFrame = 0;

    private boolean isPaused = false;

    public LayoutPlayScreen(OrthographicCamera camera, Player chosenHero)
    {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        this.chosenHero = chosenHero;
        maxHealth = chosenHero.getMaxHealth();
        currentHealth = chosenHero.getCurrentHealth();
        pauseScreen = new PauseScreen(camera);
        pauseScreen.setListener(new PauseScreenListener() {
            @Override
            public void onHomeButtonClicked() {
                System.out.println("Home button clicked");
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void onResumeButtonClicked() {
                System.out.println("Resume button clicked");
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void onRestartButtonClicked() {
                System.out.println("Restart button clicked");
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }
        });

        try {
            pauseUnactiveTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/pauseGameUnactive.png"));
            pauseActiveTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/pauseGameActive.png"));
            heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/knightAva.png"));
            frameAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/Khung ava.png"));
            frameBlood = new Texture(Gdx.files.internal("Menu/IngameIcon/Khung Blood(1)(1)-1.png.png"));
            bloodTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/Blood.png"));

            setupBloodFrames();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        createPauseButton();

        Gdx.input.setInputProcessor(stage);

    }

    private void setupBloodFrames()
    {
        bloodFrames = new TextureRegion[BLOOD_FRAME_COLS];
        float frameWidth = bloodTexture.getWidth() / BLOOD_FRAME_COLS;
        float frameHeight = bloodTexture.getHeight() / BLOOD_FRAME_ROWS;

        for (int i = 0 ; i < BLOOD_FRAME_COLS ; ++i)
        {
            bloodFrames[i] = new TextureRegion(bloodTexture, i * (int)frameWidth, 0, (int)frameWidth, (int)frameHeight);
        }
    }

    private void updateHealth(float health)
    {
        currentHealth = Math.max(0, Math.min(health, maxHealth));
        float healthPercent = currentHealth / maxHealth;
        if (healthPercent >= 0.81f) {
            currentBloodFrame = 0;
        } else if (healthPercent >= 0.61f) {
            currentBloodFrame = 1;
        } else if (healthPercent >= 0.41f) {
            currentBloodFrame = 2;
        } else if (healthPercent >= 0.21f) {
            currentBloodFrame = 3;
        } else if (healthPercent > 0f) {
            currentBloodFrame = 4;
        } else {
            currentBloodFrame = 5;
        }
    }

    public void createPauseButton() {
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseUnactiveTexture);
        TextureRegionDrawable pauseActiveDrawable = new TextureRegionDrawable(pauseActiveTexture);

        pauseButton = new ImageButton(pauseDrawable, pauseActiveDrawable) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setPosition(Gdx.graphics.getWidth() - getWidth() - padding,
                    Gdx.graphics.getHeight() - getHeight() - padding);
            }
        };

        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth() - padding,
            Gdx.graphics.getHeight() - pauseButton.getHeight() - padding);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Hien thi menu
                togglePause();
                System.out.println("Pause button clicked");
            }
        });

        stage.addActor(pauseButton);
    }
    public void render() {
        updateHealth(currentHealth);

        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();

        if(!isPaused)
        {
            float topY = Gdx.graphics.getHeight() - padding - avatarFrameSize;

            float avatarPadding = 5;
            float avatarSize = avatarFrameSize - (avatarPadding * 2);

            batch.draw(frameAvatar,
                padding,
                topY,
                avatarFrameSize,
                avatarFrameSize);

            batch.draw(heroAvatar,
                padding + avatarPadding,
                topY + avatarPadding,
                avatarSize,
                avatarSize);

            float bloodBarX = padding + avatarFrameSize + padding;
            float bloodBarY = topY + (avatarFrameSize - bloodBarHeight) / 2;

            batch.draw(frameBlood,
                bloodBarX,
                bloodBarY,
                bloodBarWidth,
                bloodBarHeight);

            float innerBloodWidth = bloodBarWidth - (2 * bloodBarInnerPaddingX);
            float innerBloodHeight = bloodBarHeight - (2 * bloodBarInnerPaddingY);

            // Draw current blood frame based on health percentage
            batch.draw(bloodFrames[currentBloodFrame],
                bloodBarX + bloodBarInnerPaddingX - 12,
                bloodBarY + bloodBarInnerPaddingY - 1,
                innerBloodWidth,
                innerBloodHeight);
        }

        batch.end();
        stage.draw();

        if (isPaused) {
            pauseScreen.render();
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        batch.dispose();
        pauseUnactiveTexture.dispose();
        pauseActiveTexture.dispose();
        heroAvatar.dispose();
        frameAvatar.dispose();
        frameBlood.dispose();
        bloodTexture.dispose();
    }

    public PauseScreen getPauseScreen()
    {
        return  pauseScreen;
    }

    public void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            pauseScreen.show();
        } else {
            pauseScreen.hide();
        }
    }

    public boolean isPaused() {
        return isPaused;
    }
}
