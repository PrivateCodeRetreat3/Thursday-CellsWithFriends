package org.maaret.gameoflife;

import java.awt.*;

import javax.swing.JPanel;

import com.spun.swing.Paintable;
import com.spun.util.Colors;
import com.spun.util.ThreadUtils;
import com.spun.util.WindowUtils;
import org.lambda.actions.Action0;

public class GameOfLifeGui extends JPanel implements Paintable {

	private static final int CELL_SIZE = 20;
	private GameOfLife game;

	public GameOfLifeGui(GameOfLife game) {
		this.game = game;
		setPreferredSize(getSize());
	}

	@Override
	public Dimension getSize() {
		return new Dimension(game.getWidth() * CELL_SIZE, game.getHeight() * CELL_SIZE);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getSize().width, getSize().height);
		game.renderCells((b, p) -> {
			g.setColor(p.getColor());
			if (b) {
				g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
			}
		});
	}

	public static void main(String[] args) {
		GameOfLife game = new GameOfLife(30, 30);
		int x = 15;
		int y = 10;
		for (Point point : GameOfLife.getNeighbors(new Point(x, y))) {
			game.placeCell(point, Colors.Reds.Red);
		}
		for (Point point : GameOfLife.getNeighbors(new Point(x, y + 3)).subList(0, 3)) {
			game.placeCell(point, Colors.Reds.Red);
		}
		for (Point point : GameOfLife.getNeighbors(new Point(x, y + 4)).subList(0, 3)) {
			game.placeCell(point, Colors.Reds.Red);
		}
		for (Point point : GameOfLife.getNeighbors(new Point(x, y + 5))) {
			game.placeCell(point, Colors.Reds.Red);
		}
		GameOfLifeGui gui = new GameOfLifeGui(game);
		gui.start();
		WindowUtils.testPanel(gui);
	}

	private void start() {
		Runnable run = () -> {
			while (true) {
				ThreadUtils.sleep(750);
				this.advanceTurn();
			}
		};
		new Thread(run).start();
	}

	void advanceTurn() {
		game.advanceTurn();
		repaint();
	}

	@Override
	public void registerRepaint(Action0 action0) {

	}

	public Paintable advanceOneTurn() {
		game.advanceTurn();
		return this;
	}
}
