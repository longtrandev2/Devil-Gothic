package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum HeroType {
    KNIGHT(new EntityStat(250, 18, 180, 1.5f, 1.0f), HeroHitbox.KNIGHT),
    SAMURAI(new EntityStat(180, 23, 250, 2.2f, 1.5f), HeroHitbox.SAMURAI),
    WIZARD(new EntityStat(120, 40, 200, 2.7f, 3.0f), HeroHitbox.WIZARD),
    ARCHER(new EntityStat(130, 28, 280, 2.8f, 3.5f), HeroHitbox.ARCHER),
    SUMMON_KNIGHT(new EntityStat(100, 10, 150, 1.5f, 1.0f), HeroHitbox.SUMMON_KNIGHT),;
    public final EntityStat stat;
    public final HeroHitbox hitbox;
    HeroType(EntityStat stat, HeroHitbox hitbox) {
        this.stat = stat;
        this.hitbox = hitbox;
    }
}
