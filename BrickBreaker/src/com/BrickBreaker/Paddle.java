package com.BrickBreaker;

public class Paddle {
    private int paddleX, paddleY, movementSpeed, width;
    private String paddleColor;
    private boolean laser;

    private static int lives = 3;

    public Paddle(int paddleX, int movementSpeed, int width) {
        this.paddleX = paddleX;
        this.movementSpeed = movementSpeed;
        this.width = width;
        this.paddleColor = "Green";
        this.paddleY = 550;
        this.laser = false;
    }

    public void setPaddleX(int paddleX) {
        this.paddleX = paddleX;
    }
    public int getPaddleX() { return this.paddleX; }

    public int getPaddleY() { return this.paddleY; }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }
    public int getMovementSpeed() { return this.movementSpeed; }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() { return this.width; }

    public void setPaddleColor(String paddleColor) {
        this.paddleColor = paddleColor;
    }
    public String getPaddleColor() { return this.paddleColor; }

    public void setLaser(boolean laser) {
        this.laser = laser;
    }
    public boolean getLaser() { return this.laser; }

    public int calcPaddleCenter() {
        return this.getPaddleX() + (this.getWidth() /2);
    }

    public void decreaseLives() {
        lives -= 1;
    }
    public void increaseLives() {
        lives += 1;
    }

    public void resetLives() {
        lives = 3;
    }

    public int getLives() {
        return lives;
    }
}
