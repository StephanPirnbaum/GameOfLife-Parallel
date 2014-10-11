package main.algorithm;

import main.Grid;
import main.gui.GameOfLife;

/**
 * The algorithm of Conways Game of Life for usage with multi core systems
 * Created by stephan on 5/30/14.
 */
public class GameOfLifeAlgorithm extends Thread {

    private Grid actualGrid;
    private Grid nextGrid;

    private int threads;

    private int threshold;

    private int leftBoundary;
    private int rightBoundary;

    private boolean mainThread;

    /**
     * Creates an object of type GameIfLifeAlgorithm
     * @param startGrid The actual grid
     * @param nextGrid The grid to store the next generation in
     * @param threads The number of available threads
     * @param threshold The minimum number of cells processed by one thread
     * @param leftBoundary The start index of the grid part
     * @param rightBoundary The end index of the grid part
     */
    public GameOfLifeAlgorithm(Grid startGrid, Grid nextGrid, int threads, int threshold, int leftBoundary, int rightBoundary, boolean mainThread) {
        this.mainThread = mainThread;
        this.actualGrid = startGrid;
        this.nextGrid = nextGrid;
        this.threads = threads;
        this.threshold = threshold;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
    }

    /**
     * Calculates the grid of the next generation
     * @param leftBoundary The start of the grid part to calculate
     * @param rightBoundary The end of the grid part to calculate
     */
    private void calculateNextGeneration(int leftBoundary, int rightBoundary) {
        int gridHeight = this.actualGrid.getHeight();
        int neighborCount;
        for(int x = leftBoundary; x <= rightBoundary; x++) {
            for(int y = 0; y < gridHeight; y++) {
                neighborCount = this.actualGrid.getNeighborCount(x, y);
                if(!this.actualGrid.getCell(x, y) && neighborCount == 3) {
                    /*
                     * rebirth of death cell with 3 living neighbors
                     */
                    this.nextGrid.setCell(x, y, true);
                } else if(this.actualGrid.getCell(x, y)) {
                    if(neighborCount < 2) {
                        /*
                         * death of cell because of loneliness
                         */
                        this.nextGrid.setCell(x, y, false);
                    } else if(neighborCount == 2 || neighborCount == 3) {
                        /*
                         * staying alive in next generation
                         */
                        this.nextGrid.setCell(x, y, true);
                    } else if(neighborCount > 3) {
                        /*
                         * death of cell because of overpopulation
                         */
                        this.nextGrid.setCell(x, y, false);
                    }
                }
            }
        }
    }

    /**
     * Splits the cell grid into t equal parts to get processed by t threads
     */
    private void splitGrid() {
        while (GameOfLife.getInstance().isRunning()) {
            int threads = GameOfLife.getInstance().getThreadNumber();
            long startTime = System.currentTimeMillis();
            //if (Thread.activeCount() <= this.threads && this.threads > 1) {
            if (Thread.activeCount() <= threads && threads > 1) {
            /*
             * array for storing the width of each part
             */
                int[] widths = new int[threads];
                for (int i = 0; i < threads; i++)
                    widths[i] = -1;
                int totalWidth = this.actualGrid.getWidth();
                int partWidth;
                int addition;
                int newThreadNumber = threads;
                do {
                    partWidth = totalWidth / newThreadNumber;
                    addition = totalWidth - newThreadNumber * partWidth;
                    newThreadNumber--;
                } while (partWidth * this.actualGrid.getHeight() < threshold);
                newThreadNumber++;
                for (int i = 0; i < threads; i++) {
                    if (i < addition)
                        widths[i] = partWidth + 1;
                    else
                        widths[i] = partWidth;
                }
                Thread[] childs = new GameOfLifeAlgorithm[newThreadNumber - 1];
                int i;
                for (i = 0; i < newThreadNumber - 1; i++) {
                    childs[i] = new GameOfLifeAlgorithm(this.actualGrid, this.nextGrid, threads, this.threshold, i * widths[i], (i + 1) * widths[i] - 1, false);
                    childs[i].start();
                }
                calculateNextGeneration(i * widths[i], (i + 1) * widths[i] - 1);
                for (int j = 0; j < newThreadNumber - 1; j++) {
                    try {
                        childs[j].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                calculateNextGeneration(this.leftBoundary, this.rightBoundary);
            }
            GameOfLife.setActualGrid(nextGrid);
            actualGrid = nextGrid;
            nextGrid = new Grid(actualGrid.getWidth(), actualGrid.getHeight());
            long endTime = System.currentTimeMillis() - startTime;
            GameOfLife.getInstance().paintGrid();
            GameOfLife.getInstance().updateTimeLabel(endTime);
        }
    }

    @Override
    public void run() {
        if(mainThread)
            splitGrid();
        else
            calculateNextGeneration(this.leftBoundary, this.rightBoundary);
    }
}