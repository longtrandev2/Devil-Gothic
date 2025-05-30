package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.controller.system.AudioManager;
import com.myteam.rpgsurvivor.model.Player;

public class LayoutPlayScreen implements Screen {
    private Main game;
    private Stage stage;
    private Stage deathStage;
    private Viewport viewport;
    private SpriteBatch batch;
    private Player chosenHero;
    private PauseScreen pauseScreen;
    private String heroType;

    private AudioManager audioManager;

    //Pause Button
    private Texture pauseUnactiveTexture;
    private Texture pauseActiveTexture;
    private ImageButton pauseButton;

    // UI Interface blood  and hero avatar
    private Texture heroAvatar;
    private Texture frameAvatar;
    private Texture frameBlood;
    private Texture bloodTexture;

    private Texture endStage;
    private Texture yesUnAtive;
    private Texture yesActive;
    private Texture noUnActive;
    private Texture noActive;
    private ImageButton yesBtn;
    private ImageButton noBtn;

    private static final float END_STAGE_DURATION = 3f;
    private float endStageTimer = 0f;
    private boolean playerDeathHandled = false;

    private TextureRegion currentBloodFrame;

    private float avatarFrameSize = 80;
    private float bloodBarWidth = 200;
    private float bloodBarHeight = 40;
    private float padding = 10;
    private float bloodBarInnerPaddingX = 12;
    private float bloodBarInnerPaddingY = 12;

    private float maxHealth ;
    private float currentHealth ;


    private boolean isPaused = false;


    //Kích thước thanh máu và frame máu
    float innerBloodWidth = bloodBarWidth - (2 * bloodBarInnerPaddingX)  + 5 ;
    float innerBloodHeight = bloodBarHeight - (2 * bloodBarInnerPaddingY);

    float topY = Gdx.graphics.getHeight() - padding - avatarFrameSize;

    float avatarPadding = 5;
    float avatarSize = avatarFrameSize - (avatarPadding * 2);
    //Vị trí khung máu
    float bloodBarX = padding + avatarFrameSize + padding;
    float bloodBarY = topY + (avatarFrameSize - bloodBarHeight) / 2;
    public LayoutPlayScreen(OrthographicCamera camera, Player chosenHero, String heroType, Main game)
    {
        this.game = game;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        this.deathStage = new Stage(viewport, batch);
        this.heroType = heroType;
        this.chosenHero = chosenHero;
        maxHealth = chosenHero.getMaxHealth();
        currentHealth = chosenHero.getCurrentHealth();
        pauseScreen = new PauseScreen(camera,game);
        audioManager = AudioManager.getInstance();
        pauseScreen.setListener(new PauseScreenListener() {
            @Override
            public void onBackButtonClicked() {
                System.out.println("Home button clicked");
                audioManager.playButtonClickSound();
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void onResumeButtonClicked() {
                System.out.println("Resume button clicked");
                audioManager.playButtonClickSound();
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void onRestartButtonClicked() {
                System.out.println("Restart button clicked");
                audioManager.playButtonClickSound();
                togglePause();
                isPaused = false;
                Gdx.input.setInputProcessor(stage);
            }
        });

        loadTexture();
        createPauseButton();

        Gdx.input.setInputProcessor(stage);

    }
    private void loadTexture()
    {
        try {
            pauseUnactiveTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/pauseGameUnactive.png"));
            pauseActiveTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/pauseGameActive.png"));
            switch (chosenHero.getHeroType().name())
            {
                case "KNIGHT" :
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/knightAva.png"));
                    break;
                case "SAMURAI" :
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/SamuraiAva.png"));
                    break;
                case "ARCHER" :
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/archerAva.png"));
                    break;
                case "WIZARD" :
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/wizardAva.png"));
                    break;
                default:
                    heroAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/knightAva.png"));
                    break;
            }
            frameAvatar = new Texture(Gdx.files.internal("Menu/IngameIcon/Khung ava.png"));
            frameBlood = new Texture(Gdx.files.internal("Menu/IngameIcon/Khung Blood(1)(1)-1.png.png"));
            bloodTexture = new Texture(Gdx.files.internal("Menu/IngameIcon/Blood.png"));
            endStage = new Texture(Gdx.files.internal("Menu/NoticeStage/YouDied.png"));
            yesUnAtive = new Texture(Gdx.files.internal("Menu/NoticeStage/YesUnActive.png"));
            yesActive = new Texture(Gdx.files.internal("Menu/NoticeStage/YesActive.png"));
            noUnActive = new Texture(Gdx.files.internal("Menu/NoticeStage/NoUnActive.png"));
            noActive = new Texture(Gdx.files.internal("Menu/NoticeStage/NoActive.png"));

            setupBloodFrames();
            setupDeathScreen();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setupDeathScreen() {
        deathStage.clear();
        Image deathImage = new Image(endStage);
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        deathImage.setPosition(centerX - deathImage.getWidth() / 2f, centerY - deathImage.getHeight() / 2f);
        deathStage.addActor(deathImage);
    }

    private void setupBloodFrames()
    {
        currentBloodFrame = new TextureRegion();
        float frameWidth = bloodTexture.getWidth();
        float frameHeight = bloodTexture.getHeight();

        currentBloodFrame = new TextureRegion(bloodTexture, 0 , 0, (int)frameWidth, (int)frameHeight);
    }

    private void updateHealth(float health)
    {
        currentHealth = Math.max(0, Math.min(health, maxHealth));


    }

    private void handlePlayerDeath() {
        if (!playerDeathHandled) {
            playerDeathHandled = true;
            endStageTimer = 0f;
            isPaused = false; // Ensure game is not paused

            // Hide pause screen if it's showing
            if (pauseScreen != null) {
                pauseScreen.hide();
            }

            System.out.println("Player died! Showing death screen...");
        }
    }

    private void updateDeathTimer(float deltaTime) {
        endStageTimer += deltaTime;
        if (endStageTimer >= END_STAGE_DURATION) {
            System.out.println("Death screen timer finished. Returning to main menu...");
            game.setScreen(new MainMenuScreen(game));
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


        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Hien thi menu
                System.out.println("Pause button clicked");
                audioManager.playButtonClickSound();
                togglePause();
            }
        });

        stage.addActor(pauseButton);

        yesBtn = createButton(yesUnAtive, yesActive);
        noBtn = createButton(noUnActive, noActive);


        yesBtn.setPosition(Gdx.graphics.getWidth() /2 - 200, Gdx.graphics.getHeight()/2 - 300);
        noBtn.setPosition(Gdx.graphics.getWidth() /2 + 200, Gdx.graphics.getHeight()/2 - 300);

        yesBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        noBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        deathStage.addActor(yesBtn);
        deathStage.addActor(noBtn);
    }

    public ImageButton createButton(Texture unactiveBtn, Texture activeBtn) {
        TextureRegionDrawable unactiveDrawable = new TextureRegionDrawable(unactiveBtn);
        TextureRegionDrawable activeDrawable = new TextureRegionDrawable(activeBtn);
        ImageButton button = new ImageButton(unactiveDrawable, activeDrawable);
        return button;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (chosenHero.isDead()) {
            Gdx.input.setInputProcessor(deathStage);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            handlePlayerDeath();
            deathStage.act(delta);
            deathStage.draw();
            return;
        }
        Gdx.input.setInputProcessor(stage);
        updateHealth(chosenHero.getCurrentHealth());
        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();

        if(!isPaused)
        {


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


            batch.draw(frameBlood,
                bloodBarX,
                bloodBarY,
                bloodBarWidth,
                bloodBarHeight);


            float healthPercent = currentHealth / (float) maxHealth;

            int newWidth = (int)(bloodTexture.getWidth() * healthPercent);
            int height = bloodTexture.getHeight();
            currentBloodFrame = new TextureRegion(bloodTexture, 0, 0, newWidth, height);
            batch.draw(
                currentBloodFrame,
                bloodBarX + bloodBarInnerPaddingX,
                bloodBarY + bloodBarInnerPaddingY
            );

        }

        batch.end();
        stage.draw();

        if (isPaused) {
            pauseScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        stage.dispose();
        batch.dispose();
        pauseUnactiveTexture.dispose();
        pauseActiveTexture.dispose();
        heroAvatar.dispose();
        frameAvatar.dispose();
        frameBlood.dispose();
        bloodTexture.dispose();
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

    public Player getChosenHero() {
        return chosenHero;
    }

    public void setChosenHero(Player chosenHero) {
        this.chosenHero = chosenHero;
    }
}
