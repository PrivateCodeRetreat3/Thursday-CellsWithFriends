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

    public Cell(int x, int y, String colorText) {
        this(x,y,loadFromText(colorText));
    }

    private static Color loadFromText(String colorText) {
        switch (colorText) {
            case "R":
                return Colors.Reds.Red;
            case "G":
                return Colors.Greens.Green;
            case "B":
                return Colors.Blues.Blue;
            case "Y":
                return Colors.Yellows.Yellow;
            case "X":
                return Colors.Grays.Gray;
        }
        return null;
    }

    public static Color[] getPlayerColors() {
        return new Color[]{ Colors.Reds.Red,
                Colors.Greens.Green,
                Colors.Blues.Blue,
                Colors.Yellows.Yellow};
    }

    public Color getColor() {
        return color;
    }

    public Boolean isAlive() {
        return color != Colors.Grays.Black;
    }
}
