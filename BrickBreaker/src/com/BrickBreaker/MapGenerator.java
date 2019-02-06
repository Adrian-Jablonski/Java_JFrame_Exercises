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
    static int currentLevel = 1;

    Levels level = new Levels();

    public MapGenerator() {
        level.mapLevel(currentLevel);
        this.row = level.getRow();
        this.col = level.getCol();

        powerUpArray = new String[row * col];
        powerUpArray = shufflePowerUpArray(row, col);

        map = level.getMap();

        brickWidth = 75;
        brickHeight = 25;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    if (map[i][j] == 100) {
                        g.setColor(Color.gray);
                    }
                    else {
                        g.setColor(Color.red);
                    }
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

    public int getRows() {return this.row;}
    public int getCols() {return this.col;}
    public int getTotalBricks() {return this.row * this.col;}

    public String[] shufflePowerUpArray(int row, int col) {
//        String[] powerUps = {"None", "LP", "SP", "QB", "NP", "None", "None", "SB", "None", "None", "None", "None", "QB", "SP", "None", "None", "None", "None", "TB", "None", "None", "LZ", "NB", "NB", "NP", "NP", "None", "+1", "FB"};
        String[] powerUps = {"FB", "FB", "LZ"};

        int powerUpsLen = powerUps.length;
        int powerUpArrayLen = row * col;

        for (int i = 0; i < powerUpArrayLen; i++) {
            int random = (int)(Math.random() * powerUpsLen);
            powerUpArray[i] = powerUps[random];
            if (powerUps[random] == "+1") {
                System.out.println("+1 Inserted");
                powerUps[random] = "None";
            }

        }
        return powerUpArray;
    }
}
