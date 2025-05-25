package com.myteam.rpgsurvivor.controller.movement;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.model.Enemy;

import java.awt.*;

public class SummonedKinghtMovement extends Movement{
    private float playerX;
    private float playerY;
    private Enemy enemy;
    private Rectangle hitbox;
    private float moveSpeed;
    private float minDistanceToEnemy;
    private boolean isMoving;

    public SummonedKinghtMovement(float playerX, float playerY, Enemy enemy, float moveSpeed)
    {
        this.playerX = playerX;
        this.playerY = playerY;
        this.enemy = enemy;
        this.moveSpeed = moveSpeed;
        this.minDistanceToEnemy = 10f;
        this.isMoving = false;
        this.hitbox = enemy.getHitbox();
    }

    public Vector2 move(float deltaTime)
    {
        float enemyX = hitbox.getX();
        float enemyY = hitbox.getY();

        Vector2 direction = new Vector2(enemyX - playerX, enemyY - playerY);
        Vector2 newPosition = new Vector2(playerX, playerY);

        float distance = direction.len();
        isMoving = false;

        if(distance > minDistanceToEnemy)
        {
            direction.nor();

            float newX = playerX + direction.x * moveSpeed * deltaTime;
            float newY = playerY + direction.y * moveSpeed * deltaTime;

            this.playerX = newX;
            this.playerY = newY;

            newPosition.set(newX, newY);
            isMoving = true;
        }
        return newPosition;
    }
}
