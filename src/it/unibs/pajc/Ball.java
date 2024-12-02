package it.unibs.pajc;

import java.awt.*;

public class Ball extends Rectangle {
    public static final double G_PAVIMENTO = 0.9859;
    public double xVelocity, yVelocity;
    public double xPosition, yPosition;
    public static final double GRAVITY = 0.99;
    public static final int INITIAL_SPEED = 20;
    public static final int BALL_SIZE = 20;
    public static final int PANEL_HEIGHT = 530;
    public static final int PANEL_WIDTH = 1000;
    private int goal;

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

        if ((xPosition > 0 && xPosition < 68 && y + height > 350) ||
                (xPosition > 910 && xPosition < PANEL_WIDTH && y + height > 350)) {
            yVelocity = -yVelocity * G_PAVIMENTO;
            y = 350 - height;
        }
    }

    public int goal(){
        goal = 0;
        if (xPosition <= 68 && yPosition >= 350) {
            goal = 2;
        } else if (xPosition >= 910 && yPosition >= 350) {
            goal = 1;
        }

        return goal;
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