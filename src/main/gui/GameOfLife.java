package main.gui;

import main.Grid;
import main.algorithm.GameOfLifeAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.InputMismatchException;

/**
 * Created by stephan on 5/30/14.
 */
public class GameOfLife {
    private boolean running;
    private static GameOfLife instance;
    private JTextField gridWidth;
    private JTextField gridHeight;
    private JButton generateGridButton;
    private JSlider threadSlider;
    private JPanel mainPanel;
    private JSlider livingCellSlider;
    private GridPanel gridPanel;
    private JButton runButton;
    private JButton stopButton;
    private JLabel pTime;
    private static Grid actualGrid;
    private static GameOfLifeAlgorithm gameOfLifeAlgorithm;

    private GameOfLife() {
        GameOfLife.instance = this;
        configureThreadSlider();
        configureLivingCellSlider();
        configureGenerateGridButton();
        configureRunButton();
        configureStopButton();
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
        pTime.setText("hi");
    }

    public void updateTimeLabel (long time) {
        this.pTime.setText(Long.toString(time) + "ms");
    }

    public static GameOfLife getInstance() {
        if(instance == null) {
            new GameOfLife();
        }
        return instance;
    }

    private void configureGenerateGridButton() {
        this.generateGridButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int height, width, livingCells;
                try {
                    height = Integer.parseInt(gridHeight.getText());
                    width = Integer.parseInt(gridWidth.getText());
                    if (height < 0 || width < 0)
                        throw new InputMismatchException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "No valid size entered",
                            "Format error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (InputMismatchException ex) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Grid height can't be less than 0",
                            "Range error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                gridPanel.setSize(width, height);
                actualGrid = new Grid(width, height, livingCellSlider.getValue());
                gridPanel.repaint();
            }
        });
    }

    private void configureLivingCellSlider() {
        this.livingCellSlider.setMinimum(0);
        this.livingCellSlider.setMaximum(100);
        this.livingCellSlider.setMinorTickSpacing(5);
        this.livingCellSlider.setMajorTickSpacing(20);
        this.livingCellSlider.setPaintTicks(true);
        this.livingCellSlider.setPaintLabels(true);
    }

    private void configureThreadSlider() {
        this.threadSlider.setMinimum(1);
        this.threadSlider.setMaximum(Runtime.getRuntime().availableProcessors());
        this.threadSlider.setMinorTickSpacing(1);
        this.threadSlider.setMajorTickSpacing(1);
        this.threadSlider.setPaintTicks(true);
        this.threadSlider.setPaintLabels(true);
    }

    private void configureRunButton() {
        this.runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(actualGrid == null) {
                    JOptionPane.showMessageDialog(mainPanel, "No grid was created", "No grid error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Grid nextGrid = null;
                    running = true;
                    long generationsCalculated = 0;
                    long startTime;
                    gameOfLifeAlgorithm = new GameOfLifeAlgorithm(actualGrid, new Grid(actualGrid.getWidth(), actualGrid.getHeight()), threadSlider.getValue(), 1000, 0, actualGrid.getWidth() - 1, true);
                    gameOfLifeAlgorithm.start();
                }
                /*while(running) {
                    startTime = System.currentTimeMillis();
                    nextGrid = new Grid(actualGrid.getWidth(), actualGrid.getHeight());
                    gameOfLifeAlgorithm = new GameOfLifeAlgorithm(actualGrid, nextGrid, threadSlider.getValue(), 1000, 0, actualGrid.getWidth() - 1);
                    gameOfLifeAlgorithm.start();
                    try {
                        gameOfLifeAlgorithm.join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    actualGrid = nextGrid;
                    pTime.setText(Long.toString(System.currentTimeMillis() - startTime));
                    System.out.println(System.currentTimeMillis() - startTime);
                    gridPanel.paintImmediately(0, 0, gridPanel.getWidth(), gridPanel.getHeight());
                    generationsCalculated++;
                }*/
            }
        });

    }

    private void configureStopButton() {
        this.stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = false;
            }
        });
    }

    public void paintGrid() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gridPanel.paintImmediately(0, 0, gridPanel.getWidth(), gridPanel.getHeight());
            }
        });
    }

    public int getThreadNumber() {
        return this.threadSlider.getValue();
    }

    public boolean isRunning() {
        return this.running;
    }

    public static void setActualGrid(Grid newGrid) {
        GameOfLife.actualGrid = newGrid;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GameOfLife");
        frame.setContentPane(new GameOfLife().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1000, 800);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        this.pTime = new JLabel("Time");
        // TODO: place custom component creation code here
    }

    /**
     * Created by stephan on 5/31/14.
     */
    public static class GridPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (actualGrid != null) {
                for (int x = 0; x < actualGrid.getWidth(); x++) {
                    for (int y = 0; y < actualGrid.getHeight(); y++) {
                        if (actualGrid.getCell(x, y)) {
                            g.fillRect(x, y, 1, 1);
                        }
                    }
                }
            }
        }
    }
}
