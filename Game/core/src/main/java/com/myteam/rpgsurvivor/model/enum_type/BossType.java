package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum BossType {
    SLIME_BOSS(new EntityStat(700,100,25,1.7f,20f), EnemyHitbox.SLIME_BOSS),
    SKELETON_KING(new EntityStat(1400,150,35,2f,20f), EnemyHitbox.SKELETON_KING);


    public final EntityStat stat;
    public final EnemyHitbox hitbox;

    BossType(EntityStat stat, EnemyHitbox hitbox) {

        this.stat = stat;
        this.hitbox = hitbox;
    }
}
