package Tetris;

import java.util.List;

import javax.swing.JOptionPane;

public class Tetris implements ArrowListener {
	private static BoundedGrid<Block> grid; // The grid containing the Tetris pieces.
	private static BlockDisplay display; // Displays the grid.
	private static Tetrad activeTetrad; // The active Tetrad (Tetris Piece).
	private static int score, level, clearCount;

	// Constructs a Tetris Game
	public Tetris() {
		score = 0;
		level = 10;
		clearCount = 99;
		grid = new BoundedGrid<Block>(20, 10);
		display = new BlockDisplay(grid);
		display.setArrowListener(this);
		display.setTitle("Tetris");
		activeTetrad = new Tetrad(grid);
		display.showBlocks();
	}

	// Play the Tetris Game
	public static void play() {
		JOptionPane.showMessageDialog(null, "Play the Tetris Game");
		while(level <= 10) {
			sleep(1.0-(level-1)*0.1);
			if(!activeTetrad.translate(1, 0)) {
				clearCompletedRows();
				activeTetrad = new Tetrad(grid);
			}
			display.showBlocks();
			display.setTitle("Tetris - Level:" + level + "/Score: " + score);
		}
		if(level >= 10) JOptionPane.showMessageDialog(null, 
				"Game Completed!\nLevel: 10\nScore: " + score);
		else JOptionPane.showMessageDialog(null, 
				"Game Over!\nLevel: " + level + "\nScore: " + score);
	}

	// Precondition: 0 <= row < number of rows
	// Postcondition: Returns true if every cell in the given row
	// is occupied; false otherwise.
	private static boolean isCompletedRow(int row) {
		boolean valid = true;
		for(int i = 0; i < grid.getNumCols(); i++) 
			if(grid.get(new Location(row, i)) == null) valid = false;
		return valid;
	}

	// Precondition: 0 <= row < number of rows;
	// The given row is full of blocks.
	// Postcondition: Every block in the given row has been removed, and
	// every block above row has been moved down one row.
	private static void clearRow(int row) {
		for(int i = 0; i < grid.getNumCols(); i++) 
			grid.remove(new Location(row, i));
		List<Location> list = grid.getOccupiedLocations();
		for(int i = 0; i < list.size(); i++) 
			if(list.get(i).getRow() < row) 
				grid.get(list.get(i)).moveTo(new Location(
						list.get(i).getRow() + 1, list.get(i).getCol()));
	}

	// Postcondition: All completed rows have been cleared.
	private static void clearCompletedRows() {
		double comboRate = 1;
		for(int i = 0; i < grid.getNumRows(); i++) 
			if(isCompletedRow(i)) {
				clearRow(i);
				score += 40*comboRate*level;
				comboRate += 0.5;
				clearCount++;
			}
		level = (int) (clearCount*0.1) + 1;
	}

	// Sleeps (suspends the active thread) for duration seconds.
	private static void sleep(double duration) {
		final int MILLISECONDS_PER_SECOND = 1000;
		int milliseconds = (int) (duration * MILLISECONDS_PER_SECOND);
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			System.err.println("Can't sleep!");
		}
	}

	@Override
	public void upPressed() {
		activeTetrad.rotate();
		display.showBlocks();
	}

	@Override
	public void downPressed() {
		activeTetrad.translate(1, 0);
		display.showBlocks();
	}

	@Override
	public void leftPressed() {
		activeTetrad.translate(0, -1);
		display.showBlocks();
	}

	@Override
	public void rightPressed() {
		activeTetrad.translate(0, 1);
		display.showBlocks();
	}

	@Override
	public void spacePressed() {
		for(int i = 0; i < 20; i++)
			activeTetrad.translate(1, 0);
		display.showBlocks();
	}
	
	// Creates and plays the Tetris game.
	public static void main(String[] args) {
		new Tetris();
		play();
	}
}
