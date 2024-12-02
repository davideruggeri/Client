package it.unibs.pajc;

import java.awt.*;

import static it.unibs.pajc.Ball.PANEL_HEIGHT;

public class Bot extends Giocatore {
    private static final int FOLLOW_DELAY = 3; // Numero di frame tra le azioni
    private static final int REACTION_RADIUS = 150; // Distanza di reazione della palla
    private int timer = 0; // Contatore per il ritardo
    private Ball ball;

    public Bot(int startX, int startY, int larghezza, int altezza, Ball ball) {
        super(startX, startY, larghezza, altezza);
        this.ball = ball;
    }

    @Override
    public void update() {
        timer++;

        if (timer >= FOLLOW_DELAY) {
            timer = 0;

            // Calcola il punto di rimbalzo previsto
            Point bouncePoint = predictBounce(ball);

            // Movimento orizzontale verso il punto previsto di rimbalzo
            if (bouncePoint.x < this.x) {
                moveLeft();
            } else if (bouncePoint.x > this.x + this.width) {
                moveRight();
            }

            // Salta se necessario per colpire la palla
            if (Math.abs(bouncePoint.x - this.x) < REACTION_RADIUS &&
                    Math.abs(bouncePoint.y - this.y) < 100 &&
                    !isJumping()) {
                jump();
            }
        }

        // Aggiorna la logica base del giocatore
        super.update();
    }


    private Point predictBounce(Ball ball) {
        double predictedX = ball.xPosition;
        double predictedY = ball.yPosition;
        double xVel = ball.xVelocity;
        double yVel = ball.yVelocity;

        while (predictedY + Ball.BALL_SIZE < PANEL_HEIGHT) {
            // Aggiorna la posizione simulata con la gravità
            yVel += Ball.GRAVITY;
            predictedX += xVel;
            predictedY += yVel;

            // Controllo dei rimbalzi contro i bordi orizzontali
            if (predictedX <= 0 || predictedX + Ball.BALL_SIZE >= Ball.PANEL_WIDTH) {
                xVel = -xVel;
            }

            // Controllo del rimbalzo contro il pavimento
            if (predictedY + Ball.BALL_SIZE >= PANEL_HEIGHT) {
                yVel = -yVel * Ball.G_PAVIMENTO;
                predictedY = PANEL_HEIGHT - Ball.BALL_SIZE; // Correggi posizione
            }
        }

        return new Point((int) predictedX, PANEL_HEIGHT - Ball.BALL_SIZE);
    }



    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN); // Colore differente per distinguere il bot
        g.fillRect(x, y, width, height);
    }
}