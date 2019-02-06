package com.BrickBreaker;

public class Levels {
    private int map[][];
    private int row, col;
    private int brickAdjustment = 0;    // brick adjustment for solid bricks

    public Levels() {
    }

    public int[][] getMap() {
        return this.map;
    }

    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }

    public int getBrickAdjustment() {
        return this.brickAdjustment;
    }

    public void mapLevel(int currentLevel) {
        brickAdjustment = 0;
        switch (currentLevel) {
            case 1:
                level1();
                break;
            case 2:
                level2();
                break;
            case 3:
                level3();
                break;
        }
    }

    private void fillMap() {
        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;  // One will detect that the brick has not been intersected with the ball. Change position to 0 if a brick should not be drawn
            }
        }
    }

    public void level1() {
        this.row = 2;
        this.col = 3;
        fillMap();

    }

    public void level2() {
        this.row = 6;
        this.col = 9;
        fillMap();

        for (int i = 1; i < 8; i++) {
            map[5][i] = solidBrick();
        }
    }

    public void level3() {
        this.row = 5;
        this.col = 9;
        fillMap();

        for (int i = 1; i < 8; i++) {
            map[2][i] = solidBrick();
        }
    }

    public int solidBrick() {   // unbreakable brick
        brickAdjustment += 1;
        return 100;
    }
}
