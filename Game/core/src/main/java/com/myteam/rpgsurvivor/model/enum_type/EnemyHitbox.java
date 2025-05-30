package com.myteam.rpgsurvivor.model.enum_type;

import com.badlogic.gdx.math.Rectangle;

public enum EnemyHitbox {
    GOBLIN(17,16,0,-2),
    RAT(19,18,6,0),
    SKELETON(22,30,3,0),
    ORC(17,16,45,40),
    VAMPIRE(17,16,10,6),
    SLIME_BOSS(230,300,0,0),
    SKELETON_KING(180,180,10,0);

    public final float width;
    public final float height;
    public final float offsetX;
    public final float offsetY;

    EnemyHitbox(float width, float height, float offsetX, float offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Rectangle createHitbox(float x, float y) {
        return new Rectangle(x + offsetX, y + offsetY, width, height);
    }

    public float getOffsetX(){
        return offsetX;
    }

    public float getOffsetY(){
        return offsetY;
    }
}
