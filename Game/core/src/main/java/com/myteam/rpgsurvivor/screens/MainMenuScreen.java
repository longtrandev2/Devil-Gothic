package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack.MeleeAttackComponent;
import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.controller.system.AudioManager;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.saveGame.GameSaveData;
import com.myteam.rpgsurvivor.saveGame.GameSaveManager;

public class MainMenuScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final float w;
    private final float h;

    private GameSaveManager gameSaveManager = new GameSaveManager();
    private AudioManager audioManager;

    // Background
    private Texture backGDTexture;
    private Texture logoGame;

    // Button
    private Texture playUnHover;
    private Texture playHover;
    private ImageButton playButton;

    private Texture continueUnHover;
    private Texture continueHover;
    private ImageButton continueBtn;

    private Texture settingUnHover;
    private Texture settingHover;
    private ImageButton settingButton;

    private Texture exitUnHover;
    private Texture exitHover;
    private ImageButton exitButton;


    private int btnSizeWidth = 100;
    private int btnSizeHeight = 50;
    private int paddingY = 20;
    private int paddingX = 100;

    private GameScreen gameScreen;


    public MainMenuScreen(final Main game) {
        this.game = game;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        viewport = new FitViewport(w, h, camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);

        audioManager = AudioManager.getInstance();

        loadTextures();
        setupLayout();

        Gdx.input.setInputProcessor(stage);
    }

    private void loadTextures() {
        backGDTexture = new Texture(Gdx.files.internal("Menu/IntroScreen/BG.png"));
        logoGame = new Texture(Gdx.files.internal("Menu/IntroScreen/LogoGame.png"));

        playUnHover = new Texture(Gdx.files.internal("Menu/IntroScreen/NewGameUnActive.png"));
        playHover = new Texture(Gdx.files.internal("Menu/IntroScreen/NewGameActive.png"));

        continueUnHover = new Texture(Gdx.files.internal("Menu/IntroScreen/ContinueUnActive.png"));
        continueHover = new Texture(Gdx.files.internal("Menu/IntroScreen/ContinueActive.png"));

        settingUnHover = new Texture(Gdx.files.internal("Menu/IntroScreen/SettingUnActive.png"));
        settingHover = new Texture(Gdx.files.internal("Menu/IntroScreen/SettingActive.png"));

        exitUnHover = new Texture(Gdx.files.internal("Menu/IntroScreen/ExitUnActive.png"));
        exitHover = new Texture(Gdx.files.internal("Menu/IntroScreen/ExitActive.png"));

    }

    private void setupLayout() {
        Stack rootStack = new Stack();
        rootStack.setFillParent(true);
        stage.addActor(rootStack);

        Image background = new Image(backGDTexture);
        rootStack.add(background);

        TextureRegionDrawable playUnAC = new TextureRegionDrawable(playUnHover);
        TextureRegionDrawable playAC = new TextureRegionDrawable(playHover);
        playButton = new ImageButton(playUnAC, playAC);

        TextureRegionDrawable continueUnAC = new TextureRegionDrawable(continueUnHover);
        TextureRegionDrawable continueAC = new TextureRegionDrawable(continueHover);
        continueBtn = new ImageButton(continueUnAC, continueAC);

        TextureRegionDrawable settingUnAC = new TextureRegionDrawable(settingUnHover);
        TextureRegionDrawable settingAC = new TextureRegionDrawable(settingHover);
        settingButton = new ImageButton(settingUnAC, settingAC);

        TextureRegionDrawable exitUnAC = new TextureRegionDrawable(exitUnHover);
        TextureRegionDrawable exitAC = new TextureRegionDrawable(exitHover);
        exitButton = new ImageButton(exitUnAC, exitAC);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenuScreen", "Play button clicked");
                audioManager.playButtonClickSound();
                audioManager.pauseMusic();
                game.setScreen(new ChosseHeroScreen(game));

            }
        });


        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainMenuScreen", "Exit button clicked");
                audioManager.playButtonClickSound();
                Gdx.app.exit();
            }
        });

        Image logo = new Image(logoGame);
        float logoScale = 0.4f;
        logo.setScale(logoScale);

        logo.setPosition(400, 250);
        stage.addActor(logo);



        playButton.setPosition(425, 250);
        stage.addActor(playButton);

//        continueBtn.setPosition(150, 120);
//        stage.addActor(continueBtn);
//
//        settingButton.setPosition(900, 280);
//        stage.addActor(settingButton);

        exitButton.setPosition(550, 100);
        stage.addActor(exitButton);


    }

    private void loadSavedMapScreen() {
        Gdx.app.log("MainMenuScreen", "Attempting to load saved game...");
        GameSaveData saveData = gameSaveManager.loadGame();

        if (saveData != null && saveData.getHeroName() != null) {
            Gdx.app.log("MainMenuScreen", "Save data loaded successfully. Hero type: " + saveData.getHeroName());

            try {
                gameScreen = new GameScreen(game, saveData.getHeroName());

                gameSaveManager.applyLoadedData(saveData, gameScreen.getMap());
                System.out.println(gameScreen.getMap().getChosenHero().getHeroType().name());

                if (gameScreen.getMap().getChosenHero() != null) {
                    HeroMovement movement = new HeroMovement(gameScreen.getMap().getChosenHero());
                    gameScreen.getMap().getChosenHero().setMovement(movement);

                    InputHandle inputHandle = new InputHandle(
                        gameScreen.getMap().getChosenHero(),
                        gameScreen.getMap().getChosenHero().getHeroMovement()
                    );
                    gameScreen.getMap().getChosenHero().setInputHandle(inputHandle);
                    gameScreen.getMap().getChosenHero().setAttackHandler(new MeleeAttackComponent(gameScreen.getMap().getChosenHero(), gameScreen.getMap().getEnemySpawnController(), gameScreen.getMap().getChosenHero().getAttackSpeed(), gameScreen.getMap().getChosenHero().getRangeAttack(), gameScreen.getMap().getChosenHero().getDamage()));
                    gameScreen.getMap().setLayoutPlayScreen(new LayoutPlayScreen(camera,gameScreen.getMap().getChosenHero(), gameScreen.getMap().getChosenHero().getHeroType().name(),game));
                    gameScreen.getMap().getSystemController().setUpgradeScreen(new UpgradeScreen(camera, game, gameScreen.getMap().getChosenHero()));
                    game.setScreen(gameScreen);
                    Gdx.app.log("MainMenuScreen", "Game loaded successfully");
                } else {
                    Gdx.app.error("MainMenuScreen", "Failed to create hero from save data");
                    game.setScreen(new ChosseHeroScreen(game));
                }
            } catch (Exception e) {
                Gdx.app.error("MainMenuScreen", "Error loading saved game: " + e.getMessage());
                e.printStackTrace();
                game.setScreen(new ChosseHeroScreen(game));
            }
        } else {
            Gdx.app.error("MainMenuScreen", "No save data found or data is invalid");
            game.setScreen(new ChosseHeroScreen(game));
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        audioManager.playMenuMusic();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backGDTexture.dispose();
        logoGame.dispose();
        playUnHover.dispose();
        playHover.dispose();
        settingUnHover.dispose();
        settingHover.dispose();
        exitUnHover.dispose();
        exitHover.dispose();
    }
}
