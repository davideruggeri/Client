package it.unibs.pajc;

import java.awt.*;

public class Ball extends Rectangle {
    private static final double G_PAVIMENTO = 0.9859;
    public double xVelocity, yVelocity;
    private double xPosition, yPosition;
    private static final double GRAVITY = 0.99;
    private static final int INITIAL_SPEED = 20;
    private static final int BALL_SIZE = 20;
    private static final int PANEL_HEIGHT = 500;
    private static final int PANEL_WIDTH = 1000;

    public Ball(int startX, int startY) {
        super(startX, startY, BALL_SIZE, BALL_SIZE);
        this.xPosition = startX;
        this.yPosition = startY;

        xVelocity = 0;
        yVelocity = 0;
    }

    public void move() {
        yVelocity += GRAVITY;

        xPosition += xVelocity;
        yPosition += yVelocity;

        this.x = (int) xPosition;
        this.y = (int) yPosition;
    }

    public void resetBall(int x, int y, int lato) { //lato = 1 dx, lato = 2 sx
        this.xPosition = x;
        this.yPosition = y;
        double angle = 0;
        if (lato == 1) {
            angle = Math.toRadians(45);
        } else if (lato == 2) {
            angle = Math.toRadians(135);
        }
        xVelocity = Math.cos(angle)*((double) INITIAL_SPEED /2.5);
        yVelocity = Math.sin(angle)*((double) INITIAL_SPEED /2.5);
    }

    public void checkBounds() {
        if (x <= 0 || x + width >= PANEL_WIDTH) {
            xVelocity = -xVelocity;
        }
        if (y + height >= PANEL_HEIGHT) {
            yVelocity = -yVelocity*G_PAVIMENTO;
            y = PANEL_HEIGHT -height;

        }
    }

    public void checkCollision(Giocatore giocatore1, Giocatore giocatore2) {
        collision(giocatore1);
        collision(giocatore2);
    }

    private void collision(Giocatore giocatore) {
        if (this.intersects(giocatore)) {
            double Velocity = Math.sqrt(Math.pow(yVelocity, 2) + Math.pow(xVelocity, 2));
            double angleBall = Math.atan2(yVelocity, xVelocity);

            if (yVelocity >= 5) {
                yVelocity = -Velocity * Math.sin(angleBall) + giocatore.getyVelocity();
            } else {
                Math.max(yVelocity = 2 * giocatore.getyVelocity(), 15);
            }
            if (xVelocity <= 5) {
                xVelocity = -Velocity * Math.cos(angleBall) + giocatore.getxVelocity();
            } else {

                if (giocatore.getxVelocity() > 0) {
                    Math.max(xVelocity = 2 * giocatore.getxVelocity(), 15);
                } else if (giocatore.getxVelocity() == 0) {
                    Math.max(xVelocity = -xVelocity * G_PAVIMENTO, -15);
                }
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(this.x, this.y, BALL_SIZE, BALL_SIZE);
    }
}