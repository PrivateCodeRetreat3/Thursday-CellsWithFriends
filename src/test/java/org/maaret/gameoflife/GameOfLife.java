package org.maaret.gameoflife;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.spun.util.Colors;
import org.lambda.actions.Action2;
import org.lambda.query.Query;
import org.lambda.utils.Grid;

public class GameOfLife {

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private int width;
	private int height;
	private HashSet<Cell> board = new HashSet<>();

	public GameOfLife(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return Grid.print(width, height, this::printCell);
	}

	public String printCell(int x, int y) {
		return getColorText(getCellAt(x, y));
	}

	private String getColorText(Cell cell) {
		if (cell.getColor() == Colors.Reds.Red) return "R";
		if (cell.getColor() == Colors.Greens.Green) return "G";
		if (cell.getColor() == Colors.Blues.Blue) return "B";
		if (cell.getColor() == Colors.Yellows.Yellow) return "Y";
		if (cell.getColor() == Colors.Grays.Gray) return "X";
		return ".";
	}

	private Cell getCellAt(int x, int y) {
		Cell first = (Cell)Query.where(board, p -> p.equals(new Point(x, y))).first();
		return first == null ? new Cell(x, y, Colors.Grays.Black) : first;
	}

	public void placeCell(int x, int y, Color color) {
		placeCell(new Point(x, y), color);
	}

	public void placeCell(Point point, Color color) {
		board.add(new Cell(point, color));
	}

	public void advanceTurn() {
		HashSet<Cell> newBoard = new HashSet<>();
		renderCells((b, p) -> {
			if (isAliveNextTurn(p)) {
				if (p.isAlive()) {
					newBoard.add(p);
				} else {
					newBoard.add(new Cell(p, getBirthColor(p)));
				}
			}
		});
		board = newBoard;
	}

	private Color getBirthColor(Cell center) {
		// get the colors of the nieghbors
		List<Point> neighbors = getNeighbors(center);
		final Map<Color, Long> colors = neighbors.stream()
				.filter(this::isCellAlive)
				.map(p -> getCellAt(p.x, p.y))
				.map(Cell::getColor)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		final Map.Entry<Color, Long> max = Query.max(colors.entrySet(), c -> c.getValue());
		// if max 1 grey otherwise max
		if (max.getValue() == 1)
		{
			return Colors.Grays.Gray;
		}
		return max.getKey();
	}

	private boolean isAliveNextTurn(Point point) {
		int count = getLivingNeighborsCount(point);
		return (count == 2 && isCellAlive(point)) || count == 3;
	}

	private int getLivingNeighborsCount(Point point) {
		List<Point> neighbors = getNeighbors(point);
		return (int) neighbors.stream().filter(this::isCellAlive).count();
	}

	private boolean isCellAlive(Point p) {
		return board.contains(p);
	}

	public static List<Point> getNeighbors(Point point) {
		return Arrays.asList(new Point(point.x - 1, point.y - 1), new Point(point.x - 0, point.y - 1),
				new Point(point.x + 1, point.y - 1), new Point(point.x - 1, point.y - 0),
				new Point(point.x + 1, point.y - 0), new Point(point.x - 1, point.y + 1),
				new Point(point.x - 0, point.y + 1), new Point(point.x + 1, point.y + 1));
	}

	public void renderCells(Action2<Boolean, Cell> drawable) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell point = getCellAt(x, y);
				drawable.call(point.isAlive(), point);
			}
		}

	}

}
