package com.myteam.rpgsurvivor.skills;

import com.badlogic.gdx.graphics.Texture;

public interface Skill {
//        Texture getIconTexture();
        float getCooldownRemaining();
        float getCooldownPercent();
        boolean isReady();

}
