package org.maaret.gameoflife;

import com.spun.util.Colors;
import com.spun.util.NumberUtils;
import com.spun.util.logger.SimpleLogger;
import org.approvaltests.Approvals;
import org.approvaltests.awt.AwtApprovals;
import org.approvaltests.core.Options;
import org.approvaltests.reporters.ImageWebReporter;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//@UseReporter(DelayedClipboardReporter.class)
public class GameOfLifeTest {

    @Test
    public void test0Neighbors() throws Exception {
        verifyGameOfLife(0);
    }

    @Test
    public void testCellHasAColor() throws Exception {
        GameOfLife game = new GameOfLife(10, 9);
        game.placeCell(2, 2, Colors.Reds.Red);
        game.placeCell(3, 2, Colors.Greens.Green);
        game.placeCell(4, 2, Colors.Blues.Blue);
        game.placeCell(5, 2, Colors.Yellows.Yellow);
        game.placeCell(6, 2, Colors.Grays.Gray);
        String output = "1) \n" + game;
        Approvals.verify(output);
    }

    @Test
    public void testNewContestedCellHasNeutralColor() throws Exception {
        GameOfLife game = new GameOfLife(10, 9);
        game.placeCell(1, 1, Colors.Reds.Red);
        game.placeCell(3, 1, Colors.Greens.Green);
        game.placeCell(4, 1, Colors.Greens.Green);
        game.placeCell(3, 3, Colors.Blues.Blue);
        String output = "1) \n" + game;
        game.advanceTurn();
        output += "2) \n" + game;
        game.advanceTurn();
        output += "3) \n" + game;
        Approvals.verify(output);
    }

    @Test
    public void test1Neighbors() throws Exception {
        verifyGameOfLife(1);
    }

    @Test
    public void test2Neighbors() throws Exception {
        verifyGameOfLife(2);
    }

    @Test
    public void test3Neighbors() throws Exception {
        verifyGameOfLife(3);
    }

    @Test
    public void test4Neighbors() throws Exception {
        verifyGameOfLife(4);
    }

    @Test
    public void test8Neighbors() throws Exception {
        verifyGameOfLife(8);
    }

    @Test
    public void testGameOfLifeGui() throws Exception {
        final int numberOfFrames = 15;
        GameOfLife game = null;
        boolean generate = false;
        if (generate) {
            game = generateGameOfLife(numberOfFrames);
            print(game);

        } else {
            game = new GameOfLife(15, 15);
            //addAll(game, new Cell(5, 4, "Y"), new Cell(6, 9, "G"), new Cell(3, 2, "B"), new Cell(6, 4, "B"), new Cell(7, 1, "R"), new Cell(13, 7, "R"), new Cell(3, 12, "B"), new Cell(14, 9, "B"), new Cell(10, 13, "B"), new Cell(6, 5, "G"), new Cell(14, 11, "B"), new Cell(2, 12, "B"), new Cell(2, 3, "R"), new Cell(9, 10, "R"));
            addAll(game, new Cell(10,8,"Y"),new Cell(14,8,"B"),new Cell(2,12,"G"),new Cell(2,9,"R"),new Cell(5,10,"Y"),new Cell(5,9,"Y"),new Cell(6,10,"R"),new Cell(6,11,"R"),new Cell(7,2,"G"),new Cell(7,3,"G"),new Cell(7,4,"B"),new Cell(7,5,"B"),new Cell(7,9,"R"),new Cell(9,10,"Y"),new Cell(9,13,"B"));
        }


        final GameOfLifeGui gameOfLifeGui = new GameOfLifeGui(game);
        AwtApprovals.verifySequence(numberOfFrames, Duration.ofMillis(700),
                n -> {
                    if (n == 0)
                        return gameOfLifeGui;
                    return gameOfLifeGui.advanceOneTurn();
                }, new Options(new ImageWebReporter()));

    }

    private void addAll(GameOfLife game, Cell... cells) {
        for (Cell cell : cells) {
            game.placeCell(cell.x, cell.y, cell.getColor());
        }
    }

    private void print(GameOfLife game) {
        SimpleLogger.variable(getString(game));
    }

    private String getString(GameOfLife game) {
        final List<Cell> aliveCells = game.getAliveCells().orderBy(p -> p.toString());
        String text = "";
        for (Cell c : aliveCells) {
            text += String.format("new Cell(%s,%s,\"%s\"),", c.x, c.y, game.getColorText(c));
        }
        return text;
    }

    private GameOfLife generateGameOfLife(int numberOfFrames) {
        for (int interations = 0; interations < 100000; interations++) {

            GameOfLife game = new GameOfLife(15, 15);
            for (int i = 0; i < 15; i++) {
                final int x = NumberUtils.getRandomInt(1, 15);
                final int y = NumberUtils.getRandomInt(1, 15);

                if (!game.isCellAlive(new Cell(x, y, Color.BLACK))) {
                    game.placeCell(x, y, NumberUtils.getShuffled(Cell.getPlayerColors(), 1)[0]);
                }
            }
            GameOfLife game2 = game.duplicate();
            ArrayList<String> previous = new ArrayList<>();
            previous.add(getString(game2));
            boolean repeated = false;
            for (int i = 0; i < numberOfFrames; i++) {
                game2.advanceTurn();
                String next = getString(game2);
                if (previous.contains(next)) {
                    repeated = true;
                    break;
                }
                previous.add(next);
            }

            boolean colorHappy = true;
            for (Color c : Cell.getPlayerColors()) {
                colorHappy &= 0 < game2.getAliveCells().where(p -> p.getColor() == c).size();
            }
            if (1 < game2.getAliveCells().size() && !repeated && colorHappy) {
                return game;
            }
        }
        return null;
    }

    private void verifyGameOfLife(int number) throws Exception {
        GameOfLife game = new GameOfLife(10, 9);
        game.placeCell(2, 2, Colors.Reds.Red);
        List<Point> neighbors = GameOfLife.getNeighbors(new Point(2, 2));
        for (int i = 0; i < number; i++) {
            Point point = neighbors.get(i);
            game.placeCell(point.x, point.y, Colors.Reds.Red);

        }
        String output = "1) \n" + game;
        game.advanceTurn();
        output += "2) \n" + game;
        Approvals.verify(output);
    }

}
