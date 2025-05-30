package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum HeroType {
    KNIGHT(new EntityStat(250, 20, 80, 0.14f, 10f), HeroHitbox.KNIGHT),
    SAMURAI(new EntityStat(180, 25, 120, 0.09f, 25f), HeroHitbox.SAMURAI),
    WIZARD(new EntityStat(100, 40, 100, 0.12f, 20f), HeroHitbox.WIZARD),
    ARCHER(new EntityStat(130, 30, 90, 0.09f, 50f), HeroHitbox.ARCHER),
    SUMMON_KNIGHT(new EntityStat(100, 10, 130, 0.01f, 10f), HeroHitbox.SUMMON_KNIGHT),;
    public final EntityStat stat;
    public final HeroHitbox hitbox;
    HeroType(EntityStat stat, HeroHitbox hitbox) {
        this.stat = stat;
        this.hitbox = hitbox;
    }
}
