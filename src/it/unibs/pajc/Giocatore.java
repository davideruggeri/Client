package it.unibs.pajc;

import java.awt.*;

public class Giocatore extends Rectangle {
    private static final int BASE_HEIGHT = 410;
    private static final int PANEL_WIDTH = 1000;
    public static final double GRAVITY = 0.98;
    public static final int JUMP_STRENGTH = -10;
    public static final int MOVEMENT_SPEED = 7;

    private double xVelocity = 0;
    private double yVelocity = 0;
    private boolean isJumping = false;



    public Giocatore(int startX, int startY, int larghezza, int altezza) {
        super(startX, startY, larghezza, altezza);
    }
    public void moveLeft() {
        if (x - MOVEMENT_SPEED >= 0) {
            x -= MOVEMENT_SPEED;
        }
    }

    public void moveRight() {
        if (x + MOVEMENT_SPEED + width <= PANEL_WIDTH) {
            x += MOVEMENT_SPEED;
        }
    }

    public void moveUpRight() {
        if (!isJumping) {
            xVelocity = MOVEMENT_SPEED*0.6;
            yVelocity = JUMP_STRENGTH*GRAVITY;
            isJumping = true;
        }
    }

    public void jump(){
        if (!isJumping) {
            yVelocity = JUMP_STRENGTH*GRAVITY;
            isJumping = true;
        }
    }

    public void moveUpLeft() {
        if (!isJumping) {
            xVelocity = -MOVEMENT_SPEED*0.6;
            yVelocity = JUMP_STRENGTH*GRAVITY;
            isJumping = true;
        }
    }

    public void update() {
        if (isJumping) {
            x += xVelocity;
            y += yVelocity;

            yVelocity += GRAVITY;

            if (x < 0) {
                x = 0;
            } else if (x + width > PANEL_WIDTH) {
                x = PANEL_WIDTH - width;
            }

            if (y >= BASE_HEIGHT) {
                y = BASE_HEIGHT;
                yVelocity = 0;
                xVelocity = 0;
                isJumping = false;
            }
        }
    }

    public void impedisciTrapasso(Giocatore altroGiocatore) {
        if (this.intersects(altroGiocatore)) {
            if (x < altroGiocatore.x) {
                x = altroGiocatore.x - this.width;
            } else if (x > altroGiocatore.x) {
                x = altroGiocatore.x + altroGiocatore.width;
            }

            if (y < altroGiocatore.y) {
                y = altroGiocatore.y - this.height;
            } else if (y > altroGiocatore.y) {
                y = altroGiocatore.y + altroGiocatore.height;
            }
        }
    }


    public double getxVelocity() {
        return xVelocity;
    }
    public double getyVelocity() {
        return yVelocity;
    }
    public boolean isJumping(){
        return isJumping;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }
}