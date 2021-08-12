package org.maaret.gameoflife;

import java.awt.Point;
import java.util.List;

import com.spun.swing.Paintable;
import com.spun.util.Colors;
import org.approvaltests.Approvals;
import org.approvaltests.awt.AwtApprovals;
import org.approvaltests.core.Options;
import org.approvaltests.reporters.DelayedClipboardReporter;
import org.approvaltests.reporters.ImageReporter;
import org.approvaltests.reporters.ImageWebReporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.jupiter.api.Test;

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
		GameOfLife game = new GameOfLife(15, 15);
		for (Point point : GameOfLife.getNeighbors(new Point(4, 2))) {
			game.placeCell(point, Colors.Reds.Red);
		}
		for (Point point : GameOfLife.getNeighbors(new Point(4, 5))) {
			game.placeCell(point, Colors.Greens.Green);
		}
		for (Point point : GameOfLife.getNeighbors(new Point(7, 5))) {
			game.placeCell(point, Colors.Blues.Blue);
		}
		for (Point point : GameOfLife.getNeighbors(new Point(7, 2))) {
			game.placeCell(point, Colors.Yellows.Yellow);
		}


		final GameOfLifeGui gameOfLifeGui = new GameOfLifeGui(game);
		AwtApprovals.verifySequence(15,
				n -> {
					if(n == 0)
						return gameOfLifeGui;
					return gameOfLifeGui.advanceOneTurn();
				}, new Options(new ImageWebReporter()));

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
