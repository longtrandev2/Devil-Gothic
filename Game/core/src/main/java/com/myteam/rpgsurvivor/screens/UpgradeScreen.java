package com.myteam.rpgsurvivor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myteam.rpgsurvivor.Main;
import com.myteam.rpgsurvivor.model.Player;

public class UpgradeScreen implements Screen {
    private Main game;
    private Viewport viewport;
    private SpriteBatch batch;
    private Stage stage;

    private OrthographicCamera camera;
    private Player player;
    private boolean isDone;

    // Stats tracking
    private int availablePoints;
    private int healthMod;
    private int damageMod;
    private int speedMod;
    private int atkSpeedMod;
    private int skillMod;

    private Texture background;

    // Button textures
    private Texture increaseHealthUnactive;
    private Texture increaseHealthActive;
    private ImageButton healthInBtn;

    private Texture decreaseHealthUnactive;
    private Texture decreaseHealthActive;
    private ImageButton healthDeBtn;

    private Texture increaseDamageUnactive;
    private Texture increaseDamageActive;
    private ImageButton damageInBtn;

    private Texture decreaseDamageUnactive;
    private Texture decreaseDamageActive;
    private ImageButton damageDeBtn;

    private Texture increaseSpeedUnactive;
    private Texture increaseSpeedActive;
    private ImageButton speedInBtn;

    private Texture decreaseSpeedUnactive;
    private Texture decreaseSpeedActive;
    private ImageButton speedDeBtn;

    private Texture increaseAtkSpeedUnactive;
    private Texture increaseAtkSpeedActive;
    private ImageButton atkSpeedInBtn;

    private Texture decreaseAtkSpeedUnactive;
    private Texture decreaseAtkSpeedActive;
    private ImageButton atkSpeedDeBtn;

    private Texture increaseSkillUnactive;
    private Texture increaseSkillActive;
    private ImageButton skillInBtn;

    private Texture decreaseSkillUnactive;
    private Texture decreaseSkillActive;
    private ImageButton skillDeBtn;

    private Texture playUnActive;
    private Texture playActive;
    private ImageButton playBtn;

    private UpgradeScreenListener listener;

    // UI labels
    private Label availablePointsLabel;
    private Label healthLabel;
    private Label damageLabel;
    private Label speedLabel;
    private Label atkSpeedLabel;
    private Label skillLabel;
    private BitmapFont font;

    public UpgradeScreen(OrthographicCamera camera, Main game, Player chosenHero) {
        this.game = game;
        this.camera = camera;
        this.player = chosenHero;
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.batch = new SpriteBatch();
        this.stage = new Stage(viewport, batch);
        this.isDone = false;


        reset();

        loadTexture();
        createFont();
        createMenu();
    }

    public void createFont() {
        font = new BitmapFont();
        font.getData().setScale(1.5f);
    }

    public void loadTexture() {
        background = new Texture(Gdx.files.internal("Menu/Upgrade Screen/Upgrade_Screen.png"));

        increaseHealthUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseUnActive.png"));
        increaseHealthActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseActive.png"));
        decreaseHealthUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseUnActive.png"));
        decreaseHealthActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseActive.png"));

        increaseDamageUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseUnActive.png"));
        increaseDamageActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseActive.png"));
        decreaseDamageUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseUnActive.png"));
        decreaseDamageActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseActive.png"));

        increaseSpeedUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseUnActive.png"));
        increaseSpeedActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseActive.png"));
        decreaseSpeedUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseUnActive.png"));
        decreaseSpeedActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseActive.png"));

        increaseAtkSpeedUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseUnActive.png"));
        increaseAtkSpeedActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseActive.png"));
        decreaseAtkSpeedUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseUnActive.png"));
        decreaseAtkSpeedActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseActive.png"));

        increaseSkillUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseUnActive.png"));
        increaseSkillActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/increaseActive.png"));
        decreaseSkillUnactive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseUnActive.png"));
        decreaseSkillActive = new Texture(Gdx.files.internal("Menu/Upgrade Screen/decreaseActive.png"));

        playUnActive = new Texture(Gdx.files.internal("Menu/Description hero/PlayBtnUnActive (1).png"));
        playActive = new Texture(Gdx.files.internal("Menu/Description hero/PlayBtnActive (1).png"));
    }

    public ImageButton createButton(Texture unactiveBtn, Texture activeBtn)
    {
        TextureRegionDrawable unactiveDrawable = new TextureRegionDrawable(unactiveBtn);
        TextureRegionDrawable activeDrawable = new TextureRegionDrawable(activeBtn);
        ImageButton button = new ImageButton(unactiveDrawable,activeDrawable);
        return button;
    }

    public void createMenu() {

//        Image bgImage = new Image(background);
//        bgImage.setSize((float) (Gdx.graphics.getWidth() / 2 * 1.5), (float) (Gdx.graphics.getHeight() / 2 * 1.5));
//        bgImage.setPosition(Gdx.graphics.getWidth() / 2 - 500, Gdx.graphics.getHeight() / 2 - 290);
        //stage.addActor(bgImage);

        float paddingX = 100;
        float paddingY = 200;


        playBtn = createButton(playUnActive, playActive);
        playBtn.setPosition(Gdx.graphics.getWidth() / 2 + 100, Gdx.graphics.getHeight() / 2 - 200);


        healthInBtn = createButton(increaseHealthUnactive, increaseHealthActive);
        damageInBtn = createButton(increaseDamageUnactive, increaseDamageActive);
        speedInBtn = createButton(increaseSpeedUnactive, increaseSpeedActive);
        atkSpeedInBtn = createButton(increaseAtkSpeedUnactive, increaseAtkSpeedActive);
        skillInBtn = createButton(increaseSkillUnactive, increaseSkillActive);

        healthInBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX, Gdx.graphics.getHeight() / 2 - paddingY + 320);
        damageInBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX, Gdx.graphics.getHeight() / 2 - paddingY + 250);
        speedInBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX, Gdx.graphics.getHeight() / 2 - paddingY + 160);
        atkSpeedInBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX, Gdx.graphics.getHeight() / 2 - paddingY + 80);
        skillInBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX, Gdx.graphics.getHeight() / 2 - paddingY);

        // Create decrease buttons
        healthDeBtn = createButton(decreaseHealthUnactive, decreaseHealthActive);
        damageDeBtn = createButton(decreaseDamageUnactive, decreaseDamageActive);
        speedDeBtn = createButton(decreaseSpeedUnactive, decreaseSpeedActive);
        atkSpeedDeBtn = createButton(decreaseAtkSpeedUnactive, decreaseAtkSpeedActive);
        skillDeBtn = createButton(decreaseSkillUnactive, decreaseSkillActive);

        healthDeBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX + 50, Gdx.graphics.getHeight() / 2 - paddingY + 320);
        damageDeBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX + 50, Gdx.graphics.getHeight() / 2 - paddingY + 250);
        speedDeBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX + 50, Gdx.graphics.getHeight() / 2 - paddingY + 160);
        atkSpeedDeBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX + 50, Gdx.graphics.getHeight() / 2 - paddingY + 80);
        skillDeBtn.setPosition(Gdx.graphics.getWidth() / 2 - paddingX + 50, Gdx.graphics.getHeight() / 2 - paddingY);

        // Create labels
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        availablePointsLabel = new Label("Available Points: " + availablePoints, labelStyle);
        availablePointsLabel.setPosition(Gdx.graphics.getWidth() / 2 + 100, Gdx.graphics.getHeight() / 2 + 100);

        healthLabel = new Label("Health: +" + healthMod, labelStyle);
        healthLabel.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - paddingY + 320);

        damageLabel = new Label("Damage: +" + damageMod, labelStyle);
        damageLabel.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - paddingY + 250);

        speedLabel = new Label("Speed: +" + speedMod, labelStyle);
        speedLabel.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - paddingY + 160);

        atkSpeedLabel = new Label("Attack Speed: +" + atkSpeedMod, labelStyle);
        atkSpeedLabel.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - paddingY + 80);

        skillLabel = new Label("Skill: +" + skillMod, labelStyle);
        skillLabel.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - paddingY);


        addButtonListeners();


        stage.addActor(healthInBtn);
        stage.addActor(damageInBtn);
        stage.addActor(speedInBtn);
        stage.addActor(atkSpeedInBtn);
        stage.addActor(skillInBtn);
        stage.addActor(healthDeBtn);
        stage.addActor(damageDeBtn);
        stage.addActor(speedDeBtn);
        stage.addActor(atkSpeedDeBtn);
        stage.addActor(skillDeBtn);
        stage.addActor(playBtn);


        stage.setDebugAll(true);


        //healthInBtn.setDebug(true);

//        stage.addActor(availablePointsLabel);
//        stage.addActor(healthLabel);
//        stage.addActor(damageLabel);
//        stage.addActor(speedLabel);
//        stage.addActor(atkSpeedLabel);
//        stage.addActor(skillLabel);
    }

    private void addButtonListeners() {
        healthInBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (availablePoints > 0) {
                    healthMod++;
                    availablePoints--;
                    updateLabels();

                }
            }
        });

        damageInBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (availablePoints > 0) {
                    damageMod++;
                    availablePoints--;
                    updateLabels();

                }
            }
        });

        speedInBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (availablePoints > 0) {
                    speedMod++;
                    availablePoints--;
                    updateLabels();

                }
            }
        });

        atkSpeedInBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (availablePoints > 0) {
                    atkSpeedMod++;
                    availablePoints--;
                    updateLabels();

                }
            }
        });

        skillInBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (availablePoints > 0) {
                    skillMod++;
                    availablePoints--;
                    updateLabels();

                }
            }
        });

        healthDeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (healthMod > 0) {
                    healthMod--;
                    availablePoints++;
                    updateLabels();

                }
            }
        });

        damageDeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (damageMod > 0) {
                    damageMod--;
                    availablePoints++;
                    updateLabels();

                }
            }
        });

        speedDeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (speedMod > 0) {
                    speedMod--;
                    availablePoints++;
                    updateLabels();

                }
            }
        });

        atkSpeedDeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (atkSpeedMod > 0) {
                    atkSpeedMod--;
                    availablePoints++;
                    updateLabels();

                }
            }
        });

        skillDeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (skillMod > 0) {
                    skillMod--;
                    availablePoints++;
                    updateLabels();
                }
            }
        });

        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                applyStatsToPlayer();
                setDone(true);
                System.out.println("Play button clicked, returning to game");
            }
        });
    }

    private void updateLabels() {
        availablePointsLabel.setText("Available Points: " + availablePoints);
        healthLabel.setText("Health: +" + healthMod);
        damageLabel.setText("Damage: +" + damageMod);
        speedLabel.setText("Speed: +" + speedMod);
        atkSpeedLabel.setText("Attack Speed: +" + atkSpeedMod);
        skillLabel.setText("Skill: +" + skillMod);
    }

    private void applyStatsToPlayer() {

        if (player != null) {
//            player.increaseMaxHealth(healthMod * 10);
//            player.increaseDamage(damageMod * 2);
//            player.increaseSpeed(speedMod * 0.1f)
//            player.increaseAttackSpeed(atkSpeedMod * 0.05f);
//            player.increaseSkill(skillMod);

            System.out.println("Applied stats to player - Health: +" + (healthMod * 10) +
                ", Damage: +" + (damageMod * 2) +
                ", Speed: +" + (speedMod * 0.1f) +
                ", Attack Speed: +" + (atkSpeedMod * 0.05f) +
                ", Skill: +" + skillMod);
        } else {
            System.err.println("Warning: Player is null, stats not applied");
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float bgWidth = screenWidth * 0.8f;
        float bgHeight = screenHeight * 0.8f;
        float bgX = (screenWidth - bgWidth) / 2;
        float bgY = (screenHeight - bgHeight) / 2;

        batch.draw(background, bgX, bgY, bgWidth, bgHeight);



        batch.end();
        stage.draw();
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        batch.dispose();

        // Dispose all textures
        increaseHealthUnactive.dispose();
        increaseHealthActive.dispose();
        decreaseHealthUnactive.dispose();
        decreaseHealthActive.dispose();

        increaseDamageUnactive.dispose();
        increaseDamageActive.dispose();
        decreaseDamageUnactive.dispose();
        decreaseDamageActive.dispose();

        increaseSpeedUnactive.dispose();
        increaseSpeedActive.dispose();
        decreaseSpeedUnactive.dispose();
        decreaseSpeedActive.dispose();

        increaseAtkSpeedUnactive.dispose();
        increaseAtkSpeedActive.dispose();
        decreaseAtkSpeedUnactive.dispose();
        decreaseAtkSpeedActive.dispose();

        increaseSkillUnactive.dispose();
        increaseSkillActive.dispose();
        decreaseSkillUnactive.dispose();
        decreaseSkillActive.dispose();

        playUnActive.dispose();
        playActive.dispose();

        font.dispose();
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public void reset() {
        isDone = false;
        availablePoints = 10;
        healthMod = 0;
        damageMod = 0;
        speedMod = 0;
        atkSpeedMod = 0;
        skillMod = 0;

        if (availablePointsLabel != null) {
            updateLabels();
        }

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
