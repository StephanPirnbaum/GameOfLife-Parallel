package main;

/**
 * The grid for Conways Game of Life
 * Created by stephan on 5/30/14.
 */
public class Grid {

    private boolean[][] grid;

    private int width;

    private int height;

    /**
     * Creates a grid with all cells set to false
     * @param width The width of the grid
     * @param height The height of the grid
     */
    public Grid(int width, int height) {
        this.grid = new boolean[width][height];
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a grid with the given percentage of living cells
     * @param width The width of the grid
     * @param height The height of the grid
     * @param livingCells The percentage of the living cells
     */
    public Grid(int width, int height, int livingCells) {
        this.grid = new boolean[width][height];
        this.width = width;
        this.height = height;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int rand = (int) (Math.random() * 100);
                if(rand < livingCells)
                    this.grid[x][y] = true;
                else
                    this.grid[x][y] = false;
            }
        }
    }

    /**
     * Returns the count of living neighbor cells
     * @param x x coordinates of the cell (starting from zero)
     * @param y y coordinates of the cell (starting from zero)
     * @return The count of the living neighbors
     */
    public int getNeighborCount(int x, int y) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            int count = 0;
            /*
             * state of the neighbor cell above
             */
            if(y > 0 && getCell(x, y - 1)) count++;
            /*
             * state of the neighbor cell right above
             */
            if(x < this.width - 1 && y > 0 && getCell(x + 1, y - 1)) count++;
            /*
             * state of the neighbor cell right
             */
            if(x < this.width - 1 && getCell(x + 1, y)) count++;
            /*
             * state of the neighbor cell right below
             */
            if(x < this.width - 1 && y < this.height - 1 && getCell(x + 1, y + 1)) count++;
            /*
             * state of the neighbor cell below
             */
            if(y < this.height - 1 && getCell(x, y + 1)) count ++;
            /*
             * state of the neighbor cell below left
             */
            if(x > 0 && y < this.height - 1 && getCell(x - 1, y + 1)) count++;
            /*
             * state of the neighbor cell left
             */
            if(x > 0 && getCell(x - 1, y)) count++;
            /*
             * state of the neighbor cell above left
             */
            if(x > 0 && y > 0 && getCell(x - 1, y - 1)) count++;
            return count;
        } else
            throw new IllegalArgumentException("This cell is not part of the grid! \n" + "x coord: " + x + "y coord: " + y);
    }

    /**
     * Returns the cell at the given position
     * @param x x coordinates of the cell (starting from zero)
     * @param y y coordinates of the cell (starting from zero)
     * @return The value of the cell
     */
    public boolean getCell(int x, int y) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            return this.grid[x][y];
        } else
            throw new IllegalArgumentException("This cell is not part of the grid! \n" + "x coord: " + x + "y coord: " + y);
    }

    /**
     * Sets the value of the given cell
     * @param x x coordinates of the cell (starting from zero)
     * @param y y coordinates of the cell (starting from zero)
     * @param value The value to which the cell is set
     */
    public void setCell(int x, int y, boolean value) {
        if(x >= 0 && x < width && y >= 0 && y < height) {
            this.grid[x][y] = value;
        } else
            throw new IllegalArgumentException("This cell is not part of the grid! \n" + "x coord: " + x + "y coord: " + y);
    }

    /**
     * Returns the grid
     * @return The grid
     */
    public boolean[][] getGrid() {
        return this.grid;
    }

    /**
     * Returns the height of this grid
     * @return The height of this grid
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns the width of this grid
     * @return The width of this grid
     */
    public int getWidth() {
        return this.width;
    }
}
