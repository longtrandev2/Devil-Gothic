package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.EnemySpawnController;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack.MeleeAttackComponent;
import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class Player extends Entity{

    protected int level;
    protected int experience;
    protected int mana;
    protected int skillPoints;
    protected int healthPoints;
    protected int damagePoints;
    protected int speedPoints;
    protected int atkSpeedPoints;
    protected int levelSkillPoints;
    protected EnemySpawnController enemySpawnController;
    protected HeroType heroType;
    protected InputHandle inputHandle;
    protected HeroMovement heroMovement;


    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;

    private ShapeRenderer shapeRenderer;
    private float offsetX;
    private float offsetY;

    protected  MeleeAttackComponent attackHandler;
    protected boolean attackTriggered = false;
    protected boolean skillTriggered = false;

    protected ArrayList<Enemy> enemyList;

    public Player(float x, float y, HeroType heroType) {
        this.entityX = x;
        this.entityY = y;

        this.heroType = heroType;

        this.stat = new EntityStat(
            heroType.stat.maxHealth,
            heroType.stat.damage,
            heroType.stat.moveSpeed,
            heroType.stat.attackSpeed,
            heroType.stat.rangeAttack
        );

        this.level = 1;
        this.experience = 0;
        this.mana = 100;
        this.skillPoints = 2;
        this.healthPoints = 0;
        this.damagePoints = 0;
        this.speedPoints = 0;
        this.atkSpeedPoints = 0;
        this.levelSkillPoints = 0;
        this.currentHealth = stat.maxHealth;
        this.isInvisible = false;
        this.isInvulnerable = false;
        this.isInteracting = false;

        this.hitbox = heroType.hitbox.createHitbox(entityX , entityY);
        this.attackbox = new Rectangle(hitbox);
        offsetX = heroType.hitbox.getOffsetX();
        offsetY = heroType.hitbox.getOffsetY();

        this.heroMovement = new HeroMovement(this);
        this.inputHandle = new InputHandle(this, heroMovement);
        this.animationManager = new AnimationManager();
    }

    @Override
    public void update(float deltaTime){
        if(enemySpawnController.isBossWave()) {
            enemyList = enemySpawnController.getActiveBoss();
        } else  {
            enemyList = enemySpawnController.getActiveEnemies();
        }
        this.setCurrentHealth(max(0, this.currentHealth));
        hitbox.setPosition(entityX + offsetX  ,entityY + offsetY);

        DebugRenderer.drawRect(attackbox, Color.RED);
    }
    public abstract void onHurt();

    public void setEnemySpawnController (EnemySpawnController controller)
    {

        this.enemySpawnController = controller;
        setMeleeAttackComponent();
    }

    public EnemySpawnController getEnemySpawnController() {
        return enemySpawnController;
    }

    public ArrayList<Enemy> getEnemies()
    {
        return enemySpawnController.getActiveEnemies();
    }

    public void performAttack() {

    }
    public void setMeleeAttackComponent(){
        this.attackHandler = new MeleeAttackComponent(this, enemySpawnController, this.getAttackSpeed(), this.getRangeAttack(), this.getDamage());
    }

    public void increaseHealth()
    {
        this.setMaxHealth(getMaxHealth() + 10);
    }

    public void decreaseHealth()
    {
        this.setMaxHealth(getMaxHealth() - 10);
    }

    public void increaseDamage()
    {
        this.setDamage(getDamage() + 5);
    }

    public void decreaseDamage()
    {
        this.setDamage(getDamage() - 5);
    }

    public void increaseMoveSpeed()
    {
        this.setMoveSpeed(getMoveSpeed() + 2f);
    }

    public void decreaseMoveSpeed()
    {
        this.setMoveSpeed(getMoveSpeed() - 2f);
    }

    public void increaseAttackSpeed()
    {
        this.setAttackSpeed(getAttackSpeed() - 0.005f);
    }

    public void decreaseAttackSpeed()
    {
        this.setAttackSpeed(getAttackSpeed() + 0.005f);
    }

    public void increaseLevelSkill()
    {
        this.updateSkill();
    }

    public void decreaseLevelSkill()
    {
        this.decreaseSkill();
    }


    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints)
    {
        this.skillPoints = skillPoints;
    }

    public void addSkillPoints(int points) {
        this.skillPoints += points;
    }

    public void deSpendSkillPointOnHealth() {
            skillPoints++;
            healthPoints--;
            decreaseHealth();

    }

    public void deSpendSkillPointOnDamage() {
            skillPoints++;
            damagePoints--;
            decreaseDamage();
    }

    public void deSpendSkillPointOnSpeed() {
            skillPoints++;
            speedPoints--;
            decreaseMoveSpeed();
    }

    public void deSpendSkillPointOnAttackSpeed() {
            skillPoints++;
            atkSpeedPoints--;
            decreaseAttackSpeed();
    }

    public void deSpendSkillPointOnSKill() {
        skillPoints++;
        levelSkillPoints--;
        decreaseLevelSkill();
    }



    public void spendSkillPointOnHealth() {
            skillPoints--;
            healthPoints++;
            increaseHealth();
    }

    public void spendSkillPointOnDamage() {
            skillPoints--;
            damagePoints++;
            increaseDamage();

    }

    public void spendSkillPointOnSpeed() {
            skillPoints--;
            speedPoints++;
            increaseMoveSpeed();
    }

    public void spendSkillPointOnAttackSpeed() {
            skillPoints--;
            atkSpeedPoints++;
            increaseAttackSpeed();
            animationManager.changeDurationAtk(this.getAttackSpeed());
    }

    public void spendSkillPointOnSKill() {
        skillPoints--;
        levelSkillPoints++;
        increaseLevelSkill();
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getDamagePoints() {
        return damagePoints;
    }

    public int getSpeedPoints() {
        return speedPoints;
    }

    public int getAttackSpeedPoints() {
        return atkSpeedPoints;
    }
    public int getLevelSkillPoints() {
        return levelSkillPoints;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public HeroMovement getHeroMovement() {
        return heroMovement;
    }

    public InputHandle getInputHandle() {
        return inputHandle;
    }

    public void setInputHandle(InputHandle inputHandle) {
        this.inputHandle = inputHandle;
    }

    public MeleeAttackComponent getAttackHandler() {
        return attackHandler;
    }

    public void setAttackHandler(MeleeAttackComponent attackHandler) {
        this.attackHandler = attackHandler;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }
    public abstract void updateSkill();
    public abstract void decreaseSkill();

}
