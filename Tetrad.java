package Tetris;
import java.awt.Color;

// Represents a Tetris piece.
public class Tetrad {
	private Block[] blocks = new Block[4]; // The blocks for the piece.

	// Constructs a Tetrad.
	public Tetrad(BoundedGrid<Block> grid) {
		int type = (int)(Math.random()*7);
		Color[] color = {Color.RED, Color.GRAY, Color.CYAN, 
				Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.GREEN};
		int[][][] cord = {
				{{0, 4}, {0, 3}, {0, 5}, {0, 6}}, 
				{{0, 4}, {0, 3}, {0, 5}, {1, 4}}, 
				{{0, 4}, {0, 5}, {1, 4}, {1, 5}}, 
				{{0, 4}, {0, 3}, {0, 5}, {1, 3}}, 
				{{0, 4}, {0, 3}, {0, 5}, {1, 5}}, 
				{{0, 4}, {0, 5}, {1, 3}, {1, 4}}, 
				{{0, 4}, {0, 3}, {1, 4}, {1, 5}}
		};
		Location location[] = new Location[4];
		for(int i = 0; i < blocks.length; i++) {
			blocks[i] = new Block();
			blocks[i].setColor(color[type]);
			location[i] = new Location(cord[type][i][0], cord[type][i][1]);
		}
		addToLocations(grid, location);
	}

	// Postcondition: Attempts to move this tetrad deltaRow rows down and
	// deltaCol columns to the right, if those positions are
	// valid and empty.
	// Returns true if successful and false otherwise.
	public boolean translate(int deltaRow, int deltaCol) {
		BoundedGrid<Block> grid = blocks[0].getGrid();
		Location oldLoc[] = removeBlocks();
		Location newLoc[] = new Location[oldLoc.length];
		for(int i = 0; i < 4; i++) {
			newLoc[i] = new Location(
					oldLoc[i].getRow() + deltaRow, 
					oldLoc[i].getCol() + deltaCol);
		}
		if(areEmpty(grid, newLoc)) {
			addToLocations(grid, newLoc);
			return true;
		} else { 
			addToLocations(grid, oldLoc);
			return false;
		}
	}

	// Postcondition: Attempts to rotate this tetrad clockwise by 90 degrees
	// about its center, if the necessary positions are empty.
	// Returns true if successful and false otherwise.
	public boolean rotate() {
		BoundedGrid<Block> grid = blocks[0].getGrid();
		Location oldLoc[] = removeBlocks();
		Location newLoc[] = new Location[oldLoc.length];
		for(int i = 0; i < 4; i++) {
			newLoc[i] = new Location(
					oldLoc[0].getRow() - oldLoc[0].getCol() + oldLoc[i].getCol(), 
					oldLoc[0].getRow() + oldLoc[0].getCol() - oldLoc[i].getRow());
		}
		if(areEmpty(grid, newLoc)) {
			addToLocations(grid, newLoc);
			return true;
		} else { 
			addToLocations(grid, oldLoc);
			return false;
		}
	}

	// Precondition: The elements of blocks are not in any grid;
	// locs.length = 4.
	// Postcondition: The elements of blocks have been put in the grid
	// and their locations match the elements of locs.
	private boolean addToLocations(BoundedGrid<Block> grid, Location[] locs) {
		boolean valid = areEmpty(grid, locs);
		for(int i = 0; i < locs.length; i++) {
			blocks[i].putSelfInGrid(grid, locs[i]);
		}
		return valid;
	}

	// Precondition: The elements of blocks are in the grid.
	// Postcondition: The elements of blocks have been removed from the grid
	// and their old locations returned.
	private Location[] removeBlocks() {
		Location locations[] = new Location[4];
		for(int i = 0; i < 4; i++) {
			locations[i] = blocks[i].getLocation();
			blocks[i].removeSelfFromGrid();
		}
		return locations;
	}

	// Postcondition: Returns true if each of the elements of locs is valid
	// and empty in grid; false otherwise.
	private boolean areEmpty(BoundedGrid<Block> grid, Location[] locs) {
		boolean valid = true;
		for(int i = 0; i < 4; i++) {
			if(!grid.isValid(locs[i]) || (grid.get(locs[i]) != null))
				valid = false;
		}
		return valid;
	}
}
