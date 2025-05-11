package com.myteam.rpgsurvivor.controller.combat.attack.impl;

public interface Attack {
    /**
     * Thực hiện hành động tấn công
     * @return true nếu tấn công thành công
     */
    boolean executeAttack();

    /**
     * Lấy thời gian hồi chiêu của đòn tấn công
     * @return thời gian hồi chiêu tính bằng mili giây
     */
    int getCooldown();

    /**
     * Kiểm tra xem có thể tấn công hay không (dựa vào cooldown)
     * @return true nếu nhân vật có thể tấn công
     */
    boolean canAttack();

    /**
     * Lấy lượng sát thương gây ra
     * @return lượng sát thương
     */
    int getDamage();

    /**
     * Lấy phạm vi tấn công
     * @return khoảng cách tấn công
     */
    float getRange();
}
