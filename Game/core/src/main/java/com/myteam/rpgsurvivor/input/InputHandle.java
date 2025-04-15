package com.myteam.rpgsurvivor.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.myteam.rpgsurvivor.controller.movement.Movement;
import com.myteam.rpgsurvivor.model.Player;

public class InputHandle {
    private Player player;
    private Movement movement;

    public static final int ACTION_MOVE_UP = 0;
    public static final int ACTION_MOVE_DOWN = 1;
    public static final int ACTION_MOVE_LEFT = 2;
    public static final int ACTION_MOVE_RIGHT = 3;
    public static final int ACTION_ATTACK = 4;

    private boolean[] actions;

    public InputHandle(Player player , Movement movement)
    {
        this.player = player;
        this.movement = movement;
        this.actions = new boolean[10];
    }

    public boolean handleInput()
    {
        resetActions();

        if(Gdx.input.isKeyJustPressed(Input.Keys.W))
        {
            actions[ACTION_MOVE_UP] = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S))
        {
            actions[ACTION_MOVE_DOWN] = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.A))
        {
            actions[ACTION_MOVE_LEFT] = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D))
        {
            actions[ACTION_MOVE_RIGHT] = true;
        }
        if (isMoving()) {
            movement.setMovementDirection(
                actions[ACTION_MOVE_UP],
                actions[ACTION_MOVE_DOWN],
                actions[ACTION_MOVE_LEFT],
                actions[ACTION_MOVE_RIGHT]
            );
        }
        return isAnyActionActive();
    }

    /**
     * Đặt lại trạng thái tất cả các hành động
     */
    private void resetActions() {
        for (int i = 0; i < actions.length; i++) {
            actions[i] = false;
        }
    }

    /**
     * Kiểm tra xem có bất kỳ hành động nào đang được thực hiện không
     * @return true nếu có bất kỳ hành động nào được kích hoạt
     */
    public boolean isAnyActionActive() {
        for (boolean action : actions) {
            if (action) return true;
        }
        return false;
    }

    /**
     * Kiểm tra xem người chơi có đang di chuyển không
     * @return true nếu người chơi đang di chuyển
     */
    public boolean isMoving() {
        return actions[ACTION_MOVE_UP] || actions[ACTION_MOVE_DOWN] ||
            actions[ACTION_MOVE_LEFT] || actions[ACTION_MOVE_RIGHT];
    }

    /**
     * Kiểm tra một hành động cụ thể
     * @param actionCode Mã hành động cần kiểm tra
     * @return true nếu hành động đang được kích hoạt
     */
    public boolean isActionActive(int actionCode) {
        if (actionCode >= 0 && actionCode < actions.length) {
            return actions[actionCode];
        }
        return false;
    }

    /**
     * Đặt trạng thái của một hành động
     * @param actionCode Mã hành động cần đặt
     * @param active Trạng thái kích hoạt
     */
    public void setAction(int actionCode, boolean active) {
        if (actionCode >= 0 && actionCode < actions.length) {
            actions[actionCode] = active;
        }
    }
}
