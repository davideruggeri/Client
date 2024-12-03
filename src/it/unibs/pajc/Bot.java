package it.unibs.pajc;

import java.awt.*;

import static it.unibs.pajc.Ball.PANEL_HEIGHT;
import static it.unibs.pajc.Giocatore.*;

public class Bot extends Giocatore {
    private static final int FOLLOW_DELAY = 3; // Numero di frame tra le azioni
    private static final int REACTION_RADIUS = 150; // Distanza di reazione della palla
    private int timer = 0; // Contatore per il ritardo
    private Ball ball;
    private Giocatore altroGiocatore;

    public Bot(int startX, int startY, int larghezza, int altezza, Ball ball, Giocatore altroGiocatore) {
        super(startX, startY, larghezza, altezza);
        this.ball = ball;
        this.altroGiocatore = altroGiocatore;

    }

    @Override
    public void update() {
        timer++;

        if (timer >= FOLLOW_DELAY) {
            timer = 0;

            Point bouncePoint = predictBounce(ball);

            // Controllo del movimento laterale
            if (bouncePoint.x < this.x && !willCollide(-MOVEMENT_SPEED, 0)) {
                moveLeft();
            } else if (bouncePoint.x > this.x + this.width && !willCollide(MOVEMENT_SPEED, 0)) {
                moveRight();
            }

            // Salto solo se non c'è collisione con l'altro giocatore
            if (Math.abs(bouncePoint.x - this.x) < REACTION_RADIUS &&
                    Math.abs(bouncePoint.y - this.y) < 100 &&
                    !isJumping() && !willCollide(0, (int) (JUMP_STRENGTH * GRAVITY))) {
                jump();
            }
        }

        super.update();
    }


    private Point predictBounce(Ball ball) {
        double predictedX = ball.xPosition;
        double predictedY = ball.yPosition;
        double xVel = ball.xVelocity;
        double yVel = ball.yVelocity;

        while (predictedY + Ball.BALL_SIZE < PANEL_HEIGHT) {
            yVel += Ball.GRAVITY;
            predictedX += xVel;
            predictedY += yVel;

            if (predictedX <= 0 || predictedX + Ball.BALL_SIZE >= Ball.PANEL_WIDTH) {
                xVel = -xVel;
            }

            if (predictedY + Ball.BALL_SIZE >= PANEL_HEIGHT) {
                yVel = -yVel * Ball.G_PAVIMENTO;
                predictedY = PANEL_HEIGHT - Ball.BALL_SIZE; // Correggi posizione
            }
        }

        return new Point((int) predictedX, PANEL_HEIGHT - Ball.BALL_SIZE);
    }

    private boolean willCollide(int xOffset, int yOffset) {
        Rectangle nextPosition = new Rectangle(this.x + xOffset, this.y + yOffset, this.width, this.height);
        return nextPosition.intersects(altroGiocatore);
    }



    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN); // Colore differente per distinguere il bot
        g.fillRect(x, y, width, height);
    }
}