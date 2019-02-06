package com.BrickBreaker;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private boolean gamePaused = false;
    private int score = 0;

    private int mapRows;
    private int mapCol;

    private int totalBricks;

    private Timer timer;
    private int delay = 8;

    private Paddle paddle;

    private MapGenerator map;

    private List<PowerUps> powerUpList = new ArrayList<>();
    private List<Ball> ballList = new ArrayList<>();
    private int powerUpListSize;
    private boolean holdingSpace = false;

    private boolean laserFired = false;
    private int laserX, laserY;

    public Gameplay() {
        startLevelPosition();
        map = new MapGenerator();
        mapRows = map.getRows();
        mapCol = map.getCols();
        totalBricks = map.getTotalBricks();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // drawing map
        map.draw((Graphics2D)g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: "+score, 550, 30);

        // lives
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Lives: "+paddle.getLives(), 50, 30);

        // level
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Level: "+map.currentLevel, 300, 30);

        // the paddle
        if (paddle.getPaddleColor() == "Orange") {
            g.setColor(Color.orange);
        }
        else {
            g.setColor(Color.green);
        }
        g.fillRect(paddle.getPaddleX(), paddle.getPaddleY(), paddle.getWidth(), 8);

        // the ball
        try {
            ballList.forEach((ball) -> {
                if (ball.getBallColor() == "Orange") {
                    g.setColor(Color.orange);
                }
                else {
                    g.setColor(Color.white);
                }
                g.fillOval(ball.getBallPosX(), ball.getBallPosY(), 20, 20);

                if (ball.getBallPosY() > 570) {    // If player missed the ball
                    Ball.ballCount -= 1;
                    ballList.remove(ball);

                    if (Ball.ballCount <= 0) {
                        paddle.decreaseLives();
                        play = false;
                        ball.setBallXdir(0);
                        ball.setBallYdir(0);

                        if (paddle.getLives() <= 0) {
                            g.setColor(Color.red);
                            g.setFont(new Font("serif", Font.BOLD, 30));
                            g.drawString("Game Over, Score", 190, 300);

                            g.setFont(new Font("serif", Font.BOLD, 20));
                            g.drawString("Press Enter to Restart", 230, 350);
                        }
                        else {
                            g.setColor(Color.red);
                            g.setFont(new Font("serif", Font.BOLD, 30));
                            g.drawString(paddle.getLives() + " Live(s) Left", 190, 300);

                            g.setFont(new Font("serif", Font.BOLD, 20));
                            g.drawString("Press Enter to Continue", 230, 350);
                        }
                    }
                }
            });

        } catch(Exception ConcurrentModificationException) {
//            System.out.println("Error");
        }

        if (totalBricks <= 0) {     // If player won level
            play = false;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Level " + map.currentLevel + " Complete", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Start Level " + (map.currentLevel + 1), 230, 350);
        }

        if (gamePaused) {
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Paused", 240, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Resume", 230, 350);
        }

        powerUpListSize = powerUpList.size();
        if (powerUpListSize > 0 && !gamePaused ) {
            for (int i = 0; i < powerUpListSize; i++) {
                PowerUps powerUp = powerUpList.get(i);

                if (play) {
                    powerUp.setPowerUpPosY();

                    g.setColor(Color.blue);
                    g.setFont(new Font("serif", Font.BOLD, 16));
                    g.drawString(powerUp.getPowerUpName(), powerUp.getPowerUpPosX(), powerUp.getPowerUpPosY());
                }
            }
        }

        if (laserFired && !gamePaused && play) {
            g.setColor(Color.red);
            g.fillRect(laserX, laserY, 10, 10);
            laserY -= 6;
            if (laserY < 0) {
                laserFired = false;
            }
        }

        g.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();  // Starts time automatically. Do not need to call method
        try {
            ballList.forEach((ball) -> {
                Rectangle ballRect = new Rectangle(ball.getBallPosX(), ball.getBallPosY(), 20, 20);
                Rectangle paddleRect = new Rectangle(paddle.getPaddleX(), 550, paddle.getWidth(), 8);
                Rectangle laserRect = new Rectangle(laserX, laserY,10, 10);
                if (play) {
                    if (ballRect.intersects(paddleRect)) {
                        ball.setBallYdir(-Math.abs(ball.getBallYdir()));  //Math.abs prevents ball getting stuck on paddle bug
                        deflectionAngle(ball);

                    } // Checks for collision between ball and paddle

                    A:
                    for (int i = 0; i < map.map.length; i++) {   // Detecting collision with brick
                        for (int j = 0; j < map.map[0].length; j++) {
                            if (map.map[i][j] > 0) {
                                int brickX = j * map.brickWidth + 10;
                                int brickY = i * map.brickHeight + 50;
                                int brickWidth = map.brickWidth;
                                int brickHeight = map.brickHeight;

                                Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

                                if (ballRect.intersects(brickRect) || laserRect.intersects(brickRect)) {
                                    boolean unbreakableBrick = map.getBrickValue(i, j) == 100;
                                    if (!unbreakableBrick) {
                                        map.setBrickValue(i, j);
                                        score += 5;
                                    }

                                    if ((!ball.getFireBall() && ballRect.intersects(brickRect)) || (ball.getFireBall() && unbreakableBrick)){
                                        if (ball.getBallPosX() + 19 <= brickRect.x || ball.getBallPosX() + 1 >= brickRect.x + brickRect.width) {    // Moves ball away from intersected brick
                                            ball.setBallXdir(-ball.getBallXdir());
                                        } else {
                                            ball.setBallYdir(-ball.getBallYdir());
                                        }
                                    }
                                    if (laserRect.intersects(brickRect)) {
                                        laserY = -50;
                                    }

                                    if (map.getBrickValue(i, j) <= 0) {
                                        totalBricks--;
                                        String powerUpName = map.powerUpArray[i * map.col + j];
                                        if (!powerUpName.equals("None")) {
                                            powerUpList.add(new PowerUps(brickX, brickY, powerUpName));       //Adds new falling power up to screen
                                        }
                                    }

                                    break A;    // Breaks out of outer loop
                                }
                            }
                        }
                    }

                    if (powerUpListSize > 0) { // detecting pad collision with powerup
                        for (int i = 0; i < powerUpListSize; i++) {
                            try {
                                PowerUps powerUp = powerUpList.get(i);
                                Rectangle powerUpRect = new Rectangle(powerUp.getPowerUpPosX(), powerUp.getPowerUpPosY() - 5, 5, 20);
                                if (powerUpRect.intersects(paddleRect) && play) {
                                    removePowerUp(i);
                                    activatePowerUp(powerUp.getPowerUpName());
                                }
                                if (powerUp.getPowerUpPosY() > 570) {   // If player misses powerup
                                    removePowerUp(i);
                                }
                            } catch (IndexOutOfBoundsException exception) {
                                System.out.println("Index Out of Bounds");
                            }

                        }
                    }

                    if (!gamePaused) {  // Ball only moves when game isn't paused
                        ball.setBallPosX(ball.getBallPosX() + ball.getBallXdir());
                        ball.setBallPosY(ball.getBallPosY() + ball.getBallYdir());
                    }
                    if (ball.getBallPosX() < 0 || ball.getBallPosX() > 670) {     // Left border Or Right Border
                        ball.setBallXdir(-ball.getBallXdir());
                    }
                    if (ball.getBallPosY() < 0) {     // Top Border
                        ball.setBallYdir(-ball.getBallYdir());
                    }
                }
                repaint();  // Repaints screen on any changes
            });
        } catch(Exception ConcurrentModificationException) {
//            System.out.println("Error");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            holdingSpace = false;

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gamePaused) { // Disabled player movement when game is paused
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && play) {
                if (paddle.getPaddleX() >= 570 + (150 - paddle.getWidth())) {
                    paddle.setPaddleX(570 + (150 - paddle.getWidth()));
                }
                else {
                    moveRight();
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT && play) {
                if (paddle.getPaddleX() <= 10) {
                    paddle.setPaddleX(10);
                }
                else {
                    moveLeft();
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                if (paddle.getLives() <= 0 || totalBricks <= 0) {
                    startLevelPosition();
                    totalBricks = mapRows * mapCol;

                    map.currentLevel += 1;
                    map = new MapGenerator();
                    mapRows = map.getRows();
                    mapCol = map.getCols();
                    totalBricks = map.getTotalBricks();
                    if (paddle.getLives() <= 0) {
                        score = 0;
                        paddle.resetLives();
                    }
                }
                else {
                    startLevelPosition();
                }
                repaint();
            }
            else if (play) {
                gamePaused = !gamePaused;   //Toggle game paused
            }
        }

        if (!holdingSpace && paddle.getLaser() && !laserFired) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                holdingSpace = true;
                laserFired = true;
                laserX = paddle.calcPaddleCenter();
                laserY = paddle.getPaddleY();
            }
        }
    }

    public void moveRight() {
        play = true;
        paddle.setPaddleX(paddle.getPaddleX() + paddle.getMovementSpeed());
    }

    public void moveLeft() {
        play = true;
        paddle.setPaddleX(paddle.getPaddleX() - paddle.getMovementSpeed());
    }

    public void startLevelPosition() {
        Ball.ballCount = 0;
        ballList.clear();
        ballList.add(new Ball(250, 450, -1, -3));
        paddle = new Paddle(310, 15, 100);

        powerUpListSize = powerUpList.size();
        if (powerUpListSize > 0 && !gamePaused ) {
            for (int i = 0; i < powerUpListSize; i++) {
                PowerUps powerUp = powerUpList.get(i);
                    powerUp.removePowerUp();

            }
        }
    }

    public void deflectionAngle(Ball ball) {     // Deflection angle off the paddle
        int paddleCenter = paddle.calcPaddleCenter();
        int paddleTenths = paddle.getWidth() / 10;

        int directionX = 1;
        if (ball.getBallXdir() < 0) {
            directionX = -1;
        }

        for (var i = 1; i <= 5; i++) {
            int lowerLimit = paddleCenter - (paddleTenths * i);
            int upperLimit = paddleCenter + (paddleTenths * i);
//            System.out.println("Lower: " + lowerLimit + " Higher: " + upperLimit);
            if (i >= 3) {
                if (ball.getBallPosX() >= lowerLimit && ball.getBallPosX() <= paddleCenter) {
                    ball.setBallXdir(-1 * (i - 1));
                    break;
                }
                else if (ball.getBallPosX() >= paddleCenter && ball.getBallPosX() <= upperLimit) {
                    ball.setBallXdir(1 * (i - 1));
                    break;
                }
            }

            if (ball.getBallPosX() >= lowerLimit  && ball.getBallPosX() <= upperLimit) {
                ball.setBallXdir(directionX * (i - 1));
                break;
            }
        }
    }

    public void removePowerUp(int i) {
        powerUpListSize -= 1;
        powerUpList.remove(i);
    }

    public void activatePowerUp(String powerUp) {
        switch (powerUp) {
            case "LP":  // Long Pad
                paddle.setWidth(150);
                break;
            case "SP":  // Short Pad
                paddle.setWidth(50);
                break;
            case "NP":  // Normal Pad
                paddle.setWidth(100);
                paddle.setPaddleColor("Green");
                paddle.setLaser(false);
                break;
            case "QB":  // Quick Ball
                ballList.forEach((ball) -> {
                    if (ball.getBallYdir() < 0) {
                        ball.setBallYdir(-5);
                    }
                    else {
                        ball.setBallYdir(5);
                    }
                });
                break;
            case "SB":  // Slow Ball
                ballList.forEach((ball) -> {
                    if (ball.getBallYdir() < 0) {
                        ball.setBallYdir(-2);
                    }
                    else {
                        ball.setBallYdir(2);
                    }
                });
                break;
            case "NB":  // Normal Ball
                ballList.forEach((ball) -> {
                    if (ball.getBallYdir() < 0) {
                        ball.setBallYdir(-3);
                    }
                    else {
                        ball.setBallYdir(3);
                    }
                    ball.setFireBall(false);
                    ball.setBallColor("White");
                });
                break;
            case "TB":  // Triple Ball
                Ball ballToClone = ballList.get(0);
                ballList.add(new Ball(ballToClone.getBallPosX(), ballToClone.getBallPosY(), -ballToClone.getBallXdir() -1, ballToClone.getBallYdir()));
                ballList.add(new Ball(ballToClone.getBallPosX(), ballToClone.getBallPosY(), ballToClone.getBallXdir() + 1, -ballToClone.getBallYdir()));
                break;
            case "FB":  // Fire Ball
                for (Ball ball : ballList) {
                    if (!ball.getFireBall()) {
                        ball.setFireBall(true);
                        ball.setBallColor("Orange");
                        break;
                    }
                }
                break;
            case "LZ":  // Laser Paddle
                paddle.setPaddleColor("Orange");
                paddle.setLaser(true);
                break;
            case "+1":  // Add a life
                paddle.increaseLives();
                break;
            default:
                break;
        }
    }
}
