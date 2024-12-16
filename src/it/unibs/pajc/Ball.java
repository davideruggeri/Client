package it.unibs.pajc;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ball extends Rectangle {
    public static final double G_PAVIMENTO = 0.9859;
    public static final double GRAVITY = 0.98;
    public static final int INITIAL_SPEED = 20;
    public static final int BALL_SIZE = 30;
    public static final int PANEL_HEIGHT = 530;
    public static final int PANEL_WIDTH = 1000;
    private int goal;
    public double xVelocity, yVelocity;
    public double xPosition, yPosition;

    private LinkedList<PointWithTime> trajectory = new LinkedList<>();
    private static final int TRAJECTORY_LIFETIME = 600;
    private long lastPointTime;

    private ImageIcon ballImage;


    public Ball(int startX, int startY) {
        super(startX, startY, BALL_SIZE, BALL_SIZE);
        this.xPosition = startX;
        this.yPosition = startY;

        xVelocity = 0;
        yVelocity = 0;

        lastPointTime = System.currentTimeMillis();
    }

    public void disegnaPalla () {
        ImageIcon palla = new ImageIcon(getClass().getResource("/images/palla.png"));
        Image ridimensionaPalla = palla.getImage().getScaledInstance(BALL_SIZE, BALL_SIZE, Image.SCALE_SMOOTH);
        ballImage = new ImageIcon(ridimensionaPalla);
    }

    private class PointWithTime {
        int x, y;
        long time;

        public PointWithTime(int x, int y, long time) {
            this.x = x;
            this.y = y;
            this.time = time;
        }
    }

    public void updateTrajectory() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPointTime >= 16) {
            int centerX = (int) (xPosition + (double) BALL_SIZE / 2);
            int centerY = (int) (yPosition + (double) BALL_SIZE / 2);
            trajectory.add(new PointWithTime(centerX, centerY, currentTime));
            lastPointTime = currentTime;
        }

        Iterator<PointWithTime> iterator = trajectory.iterator();
        while (iterator.hasNext()) {
            PointWithTime p = iterator.next();
            if (currentTime - p.time > TRAJECTORY_LIFETIME) {
                iterator.remove();
            }
        }
    }

    public void move() {
        yVelocity += GRAVITY;

        xPosition += xVelocity;
        yPosition += yVelocity;

        this.x = (int) xPosition;
        this.y = (int) yPosition;

        updateTrajectory();
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
        xVelocity = Math.cos(angle)*((double) INITIAL_SPEED /2.1);
        yVelocity = Math.sin(angle)*((double) INITIAL_SPEED /2.1);
    }

    public void checkBounds() {
        if (x <= 0 || x + width >= PANEL_WIDTH) {
            xVelocity = -xVelocity;
        }
        if (y + height >= PANEL_HEIGHT) {
            yVelocity = -yVelocity * G_PAVIMENTO;
            //y = PANEL_HEIGHT - height;
        }

        if ((xPosition > 0 && xPosition < 68 && y + height > 350) ||
                (xPosition > 910 && xPosition < PANEL_WIDTH && y + height > 350)) {
            yVelocity = -yVelocity * G_PAVIMENTO;
            //y = 350 - height;
        }
        if (yVelocity <= 0.5 && yVelocity >= 0) {
            yVelocity = 0;
        }
    }

    public int goal(){
        goal = 0;
        if (xPosition < 68 && yPosition > 350) {
            goal = 2;
        } else if (xPosition > 910 && yPosition > 350) {
            goal = 1;
        }

        return goal;
    }

    public void checkCollision(Giocatore giocatore1, Giocatore giocatore2) {
        collision(giocatore1);
        collision(giocatore2);
    }

   /* void collision(Giocatore giocatore) {
        if (this.intersects(giocatore)) {
            resetPosition(giocatore);

            double velocity = Math.sqrt(Math.pow(yVelocity, 2) + Math.pow(xVelocity, 2));
            double angleBall = Math.atan2(yVelocity, xVelocity);

            if (yVelocity >= 5) {
                yVelocity = -velocity * Math.sin(angleBall) + giocatore.getyVelocity();
            } else {
                yVelocity = Math.max(2 * giocatore.getyVelocity(), 15);
            }

            if (xVelocity <= 5) {
                xVelocity = -velocity * Math.cos(angleBall) + giocatore.getxVelocity();
            } else {
                if (giocatore.getxVelocity() > 0) {
                    xVelocity = Math.max(2 * giocatore.getxVelocity(), 15);
                } else if (giocatore.getxVelocity() == 0) {
                    xVelocity = Math.max(-xVelocity * G_PAVIMENTO, -15);
                }
            }
        }
    }*/

    public void collision(Giocatore giocatore) {
        if (this.intersects(giocatore)) {
            // Determina se la collisione avviene sopra
            if (this.y + this.height <= giocatore.y + 5) {
                // Collisione sulla parte superiore
                this.y = giocatore.y - this.height; // Riposiziona sopra il giocatore
                yVelocity = -Math.abs(yVelocity); // Inverti velocità verticale

                // Modifica l'angolo in base alla posizione del contatto
                double giocatoreCentro = giocatore.x + giocatore.width / 2.0;
                double pallaCentro = this.x + this.width / 2.0;
                double distanza = pallaCentro - giocatoreCentro;
                double maxDistanza = giocatore.width / 2.0;
                xVelocity = INITIAL_SPEED * (distanza / maxDistanza); // Nuova velocità orizzontale
            }
            // Collisione sul lato sinistro
            else if (this.x + this.width <= giocatore.x + 5) {
                this.x = giocatore.x - this.width; // Riposiziona a sinistra del giocatore
                xVelocity = -Math.abs(xVelocity); // Inverti velocità orizzontale
            }
            // Collisione sul lato destro
            else if (this.x >= giocatore.x + giocatore.width - 5) {
                this.x = giocatore.x + giocatore.width; // Riposiziona a destra del giocatore
                xVelocity = Math.abs(xVelocity); // Inverti velocità orizzontale
            }
        }
    }
    // controllare qua il problema della palla
void resetPosition(Giocatore giocatore) {
    double dx;
    double dy;
    double xNew;
    double yNew;
    if (this.getCenterX() > giocatore.getCenterX()) {
        if (this.getCenterY() > giocatore.getCenterY() ) {
            if (this.getCenterY() < (giocatore.getCenterY() + (giocatore.getHeight() / 2))
                    && this.getCenterY()  > giocatore.getCenterY()
                    && this.getCenterX() > giocatore.getCenterX()
                    && this.getCenterX() < (giocatore.getCenterX() + giocatore.getWidth() / 2)) {
                dx = (giocatore.getWidth()/2) - (getCenterX() - giocatore.getCenterX());
                dy = (giocatore.getHeight()/2) - (getCenterY() - giocatore.getCenterY());
                xNew = this.getCenterX() + dx + (double) BALL_SIZE / 2;
                yNew = this.getCenterY() + dy + (double) BALL_SIZE / 2;
                this.xPosition = xNew;
                this.yPosition = yNew;

            }
        }
        if (this.getCenterY() < giocatore.getCenterY()) {
            if (this.getCenterY() < (giocatore.getCenterY() + (giocatore.getHeight() / 2))
                    && this.getCenterY()  > giocatore.getCenterY()
                    && this.getCenterX() > giocatore.getCenterX()
                    && this.getCenterX() < (giocatore.getCenterX() + giocatore.getWidth() / 2)) {
                dx = (giocatore.getWidth()/2) - (getCenterX() - giocatore.getCenterX());
                dy = (giocatore.getHeight()/2) - (getCenterY() - giocatore.getCenterY());
                xNew = this.getCenterX() + dx + (double) BALL_SIZE / 2;
                yNew = this.getCenterY() - (dy + (double) BALL_SIZE / 2);
                this.xPosition = xNew;
                this.yPosition = yNew;

            }
        }
    }
    if (this.getCenterX() < giocatore.getCenterX()) {
        if (this.getCenterY() > giocatore.getCenterY() ) {
            if (this.getCenterY() < (giocatore.getCenterY() + (giocatore.getHeight() / 2))
                    && this.getCenterY()  > giocatore.getCenterY()
                    && this.getCenterX() > giocatore.getCenterX()
                    && this.getCenterX() < (giocatore.getCenterX() - giocatore.getWidth() / 2)) {
                dx = (giocatore.getWidth()/2) - (-getCenterX() - giocatore.getCenterX());
                dy = (giocatore.getHeight()/2) - (getCenterY() - giocatore.getCenterY());
                xNew = this.getCenterX() - (dx + (double) BALL_SIZE / 2);
                yNew = this.getCenterY() + dy + (double) BALL_SIZE / 2;
                this.xPosition = xNew;
                this.yPosition = yNew;

            }
        }
        if (this.getCenterY() < giocatore.getCenterY()) {
            if (this.getCenterY() < (giocatore.getCenterY() + (giocatore.getHeight() / 2))
                    && this.getCenterY()  > giocatore.getCenterY()
                    && this.getCenterX() > giocatore.getCenterX()
                    && this.getCenterX() < (giocatore.getCenterX() - giocatore.getWidth() / 2)) {
                dx = (giocatore.getWidth()/2) - (getCenterX() - giocatore.getCenterX());
                dy = (giocatore.getHeight()/2) - (getCenterY() - giocatore.getCenterY());
                xNew = this.getCenterX() - (dx + (double) BALL_SIZE / 2);
                yNew = this.getCenterY() - (dy + (double) BALL_SIZE / 2);
                this.xPosition = xNew;
                this.yPosition = yNew;

            }
        }
    }
}

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        for (PointWithTime p : trajectory) {
            g.fillOval(p.x -2 , p.y - 2, 3, 3); // Disegna ogni punto come un cerchio piccolo
        }

        g.setColor(Color.RED);
        g.fillOval((int) xPosition, (int) yPosition, BALL_SIZE, BALL_SIZE);


        disegnaPalla();
        g.drawImage(ballImage.getImage(), (int) xPosition, (int) yPosition, null);
    }
}