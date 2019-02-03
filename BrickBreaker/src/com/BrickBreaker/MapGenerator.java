package com.BrickBreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    public String[] powerUpArray;
    public int row, col;

    public MapGenerator(int row, int col) {
        this.row = row;
        this.col = col;
        powerUpArray = new String[row * col];
        powerUpArray = shufflePowerUpArray(row, col);

        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;  // One will detect that the brick has not been intersected with the ball. Change position to 0 if a brick should not be drawn
            }
        }

        brickWidth = 75;
        brickHeight = 25;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(Color.red);
                    g.fillRect(j * brickWidth + 10, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3)); // Setting up a border
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 10, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int row, int col) {
        map[row][col] -= 1;
    }

    public int getBrickValue(int row, int col) {
        return map[row][col];
    }

    public String[] shufflePowerUpArray(int row, int col) {
//        String[] powerUps = {"None", "LP", "SP", "QB", "None", "None", "None", "SB", "None", "None", "None", "None", "QB", "SP", "None", "None", "None", "None", "TB"};
        String[] powerUps = {"None", "TB", "TB", "FB", "FB"};

        int powerUpsLen = powerUps.length;
        int powerUpArrayLen = row * col;

        for (int i = 0; i < powerUpArrayLen; i++) {
            int random = (int)(Math.random() * powerUpsLen);
            powerUpArray[i] = powerUps[random];
        }
        return powerUpArray;
    }
}
