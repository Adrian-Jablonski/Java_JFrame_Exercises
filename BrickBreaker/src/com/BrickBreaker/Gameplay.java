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

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private boolean gamePaused = false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX, ballPosX, ballPosY, ballXdir, ballYdir, playerMovementSpeed;

    private MapGenerator map;

    public Gameplay() {
        startLevelPosition();
        map = new MapGenerator(3, 7);
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
        g.drawString(""+score, 590, 30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if (totalBricks <= 0) {     // If player won level
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won: " + score, 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        if(ballPosY > 570) {    // If player missed the ball
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score", 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        if (gamePaused) {
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Paused", 240, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Resume", 230, 350);
        }

        g.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();  // Starts time automatically. Do not need to call method
        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

        if (play) {
            if (ballRect.intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            } // Checks for collision between ball and paddle

            A: for (int i = 0; i <map.map.length; i++) {   // Detecting collision with brick
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {    // Moves ball away from intersected brick
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;    // Breaks out of outer loop
                        }

                    }
                }
            }

            if (!gamePaused) {  // Ball only moves when game isn't paused
                ballPosX += ballXdir;
                ballPosY += ballYdir;
            }
            if (ballPosX < 0) {     // Left border
                ballXdir = -ballXdir;
            }
            if (ballPosY < 0) {     // Top Border
                ballYdir = -ballYdir;
            }
            if (ballPosX > 670) {     // Right Border
                ballXdir = -ballXdir;
            }
        }

        repaint();  // Repaints screen on any changes
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gamePaused) { // Disabled player movement when game is paused
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (playerX >= 586) {
                    playerX = 586;
                }
                else {
                    moveRight();
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (playerX <= 10) {
                    playerX = 10;
                }
                else {
                    moveLeft();
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                startLevelPosition();
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                repaint();
            }
            else if (play) {
                gamePaused = !gamePaused;   //Toggle game paused
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += playerMovementSpeed;
    }

    public void moveLeft() {
        play = true;
        playerX -= playerMovementSpeed;
    }

    public void startLevelPosition() {
        ballPosX = 120;
        ballPosY = 350;
        ballXdir = -3;
        ballYdir = -1;
        playerX = 310;
        playerMovementSpeed = 20;
    }

}
