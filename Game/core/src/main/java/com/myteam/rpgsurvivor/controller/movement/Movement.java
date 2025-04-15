package com.myteam.rpgsurvivor.controller.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.model.Entity;

public class Movement {
    private Entity entity;
    private Vector2 direction;
    private boolean isMoving;

    // Hướng di chuyển hiện tại
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    public Movement(Entity entity) {
        this.entity = entity;
        this.direction = new Vector2(0, 0);
        this.isMoving = false;
    }

    /**
     * Thiết lập hướng di chuyển dựa trên input
     * @param up Di chuyển lên
     * @param down Di chuyển xuống
     * @param left Di chuyển trái
     * @param right Di chuyển phải
     */
    public void setMovementDirection(boolean up, boolean down, boolean left, boolean right) {
        this.moveUp = up;
        this.moveDown = down;
        this.moveLeft = left;
        this.moveRight = right;

        // Tính toán vector hướng
        calculateDirection();
    }

    /**
     * Tính toán vector hướng di chuyển dựa trên các phím được nhấn
     */
    private void calculateDirection() {
        direction.set(0, 0);

        if (moveUp) direction.y += 1;
        if (moveDown) direction.y -= 1;
        if (moveLeft) direction.x -= 1;
        if (moveRight) direction.x += 1;

        // Chuẩn hóa vector nếu đang di chuyển theo đường chéo
        if (direction.len2() > 0) {
            direction.nor();
            isMoving = true;
        } else {
            isMoving = false;
        }
    }

    /**
     * Cập nhật vị trí của entity dựa trên hướng di chuyển và tốc độ
     */
    public void update() {
        if (isMoving) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            float speed = entity.getSpeed();

            // Tính toán vị trí mới
            float newX = entity.getEntityX() + direction.x * speed * deltaTime;
            float newY = entity.getEntityY() + direction.y * speed * deltaTime;

            // Cập nhật vị trí cho entity
            entity.setEntityPosition(newX, newY);

            // Cập nhật hướng mặt của entity (nếu cần)
            updateFacingDirection();
        }
    }

    /**
     * Cập nhật hướng mặt của entity dựa trên hướng di chuyển
     */
    private void updateFacingDirection() {
        if (direction.x < 0) {
            entity.setFacingRight(false);
        } else if (direction.x > 0) {
            entity.setFacingRight(true);
        }
    }

    /**
     * Kiểm tra xem entity có đang di chuyển không
     * @return true nếu entity đang di chuyển
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * Lấy hướng di chuyển hiện tại
     * @return Vector2 đại diện cho hướng di chuyển
     */
    public Vector2 getDirection() {
        return direction;
    }

    /**
     * Kiểm tra xem đang di chuyển theo hướng nào
     * @return Mã hướng: 0=idle, 1=up, 2=right, 3=down, 4=left, 5-8=các hướng chéo
     */
    public int getDirectionCode() {
        if (!isMoving) return 0; // idle

        if (direction.x > 0.5f && Math.abs(direction.y) < 0.5f) return 2; // right
        if (direction.x < -0.5f && Math.abs(direction.y) < 0.5f) return 4; // left
        if (Math.abs(direction.x) < 0.5f && direction.y > 0.5f) return 1; // up
        if (Math.abs(direction.x) < 0.5f && direction.y < -0.5f) return 3; // down

        // Các hướng chéo
        if (direction.x > 0 && direction.y > 0) return 5; // up-right
        if (direction.x > 0 && direction.y < 0) return 6; // down-right
        if (direction.x < 0 && direction.y < 0) return 7; // down-left
        if (direction.x < 0 && direction.y > 0) return 8; // up-left

        return 0; // mặc định là idle
    }
}
