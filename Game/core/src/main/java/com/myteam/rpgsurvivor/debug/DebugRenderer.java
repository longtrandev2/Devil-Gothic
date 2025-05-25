package com.myteam.rpgsurvivor.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class DebugRenderer {

    private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static boolean enabled = false;

    private static final List<DebugShape> shapesToDraw = new ArrayList<>();

    public static void setEnabled(boolean isEnabled) {
        enabled = isEnabled;
    }

    public static void drawRect(Rectangle rect, Color color) {
        if (!enabled) return;
        shapesToDraw.add(new DebugShape(rect, color));
    }

    public static void drawCircle(Circle circle, Color color) {
        if (!enabled) return;
        shapesToDraw.add(new DebugShape(circle, color));
    }
    public static void render() {
        if (!enabled || shapesToDraw.isEmpty()) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (DebugShape shape : shapesToDraw) {
            shapeRenderer.setColor(shape.color);
            if (shape.rect != null) {
                shapeRenderer.rect(shape.rect.x, shape.rect.y, shape.rect.width, shape.rect.height);
            } else if (shape.circle != null) {
                shapeRenderer.circle(shape.circle.x, shape.circle.y, shape.circle.radius);
            }
        }
        shapeRenderer.end();
        shapesToDraw.clear();
    }

    private static class DebugShape {
        Rectangle rect;
        Color color;
        Circle circle;
        DebugShape(Rectangle rect, Color color) {
            this.rect = new Rectangle(rect);
            this.color = color;
        }

        DebugShape(Circle circle, Color color) {
            this.circle = new Circle(circle);
            this.color = color;
        }
    }
}
