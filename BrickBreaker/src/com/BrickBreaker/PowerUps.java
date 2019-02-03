package com.BrickBreaker;

public class PowerUps {
    private int powerUpPosX, powerUpPosY;
    public String powerUpName;

    PowerUps(int powerUpPosX, int powerUpPosY, String powerUpName) {
        this.powerUpPosX = powerUpPosX;
        this.powerUpPosY = powerUpPosY;
        this.powerUpName = powerUpName;
    }

    public void setPowerUpPosY() {
        this.powerUpPosY += 1;
    }

    public int getPowerUpPosX() {
        return this.powerUpPosX;
    }

    public int getPowerUpPosY() {
        return this.powerUpPosY;
    }

    public String getPowerUpName() {
        return this.powerUpName;
    }

    public void removePowerUp() {
        this.powerUpPosX = 1000;
    }

}
