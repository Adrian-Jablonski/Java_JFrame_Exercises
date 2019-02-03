package com.BrickBreaker;

public class Ball {
    private int ballPosX, ballPosY, ballXdir, ballYdir;
    static int ballCount = 0;   // Will be new way of checking if game is over if there are 0 balls left;
    private boolean fireball = false; // Will be used to determine if ball bounces away from bricks on collision

    private String ballColor;

    public Ball(int ballPosX, int ballPosY, int ballXdir, int ballYdir) {
        this.ballPosX = ballPosX;
        this.ballPosY = ballPosY;
        this.ballXdir = ballXdir;
        this.ballYdir = ballYdir;
        this.ballColor = "White";
        ballCount += 1;
    }

    public int getBallPosX() {
        return this.ballPosX;
    }
    public void setBallPosX(int ballPosX) {
        this.ballPosX = ballPosX;
    }
    public int getBallPosY() {
        return this.ballPosY;
    }
    public void setBallPosY(int ballPosY) {
        this.ballPosY = ballPosY;
    }
    public int getBallXdir() {
        return this.ballXdir;
    }
    public void setBallXdir(int ballXdir) {
        this.ballXdir = ballXdir;
    }
    public int getBallYdir() {
        return this.ballYdir;
    }
    public void setBallYdir(int ballYdir) {
        this.ballYdir = ballYdir;
    }

    public String getBallColor() {
        return this.ballColor;
    }
    public void setBallColor(String ballColor) {
        this.ballColor = ballColor;
    }

    public boolean getFireBall() {
        return this.fireball;
    }
    public void setFireBall(boolean setTo) {
        this.fireball = setTo;
    }
}
