package org.maaret.gameoflife;

import com.spun.util.Colors;

import java.awt.*;

public class Cell extends Point {
    private final Color color;

    public Cell(Point point, Color color) {
        this.x = point.x;
        this.y = point.y;
        this.color = color;
    }

    public Cell(int x, int y, Color color) {
        this.y = y;
        this.x = x;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Boolean isAlive() {
        return color != Colors.Grays.Black;
    }
}
