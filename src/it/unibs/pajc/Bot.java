package it.unibs.pajc;

import java.awt.*;

public class Bot extends Giocatore {
    private static final int FOLLOW_DELAY = 3; // Numero di frame tra le azioni
    private static final int REACTION_RADIUS = 200; // Distanza di reazione della palla
    private int timer = 0; // Contatore per il ritardo
    private Ball ball;

    public Bot(int startX, int startY, int larghezza, int altezza, Ball ball) {
        super(startX, startY, larghezza, altezza);
        this.ball = ball;
    }

    public void update() {
        // Incrementa il timer
        timer++;

        // Esegui azioni solo ogni FOLLOW_DELAY frame
        if (timer >= FOLLOW_DELAY) {
            timer = 0; // Resetta il timer

            // Se la palla è nel raggio di reazione, il bot prova a colpirla
            if (Math.abs(ball.x - this.x) < REACTION_RADIUS) {
                // Movimento orizzontale verso la palla
                if (ball.x < this.x) {
                    moveLeft();
                } else if (ball.x > this.x + this.width) {
                    moveRight();
                }

                // Tentativo di colpire la palla (solo quando il bot è vicino)
                if (ball.y >= this.y - ball.height && ball.y <= this.y + this.height) {
                    // Il bot cerca di colpire la palla, salta se necessario
                    if (!isJumping()) {
                        jump();
                    }
                }
            }
        }

        // Chiamata alla logica base del giocatore per aggiornare posizione e stato
        super.update();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN); // Colore differente per distinguere il bot
        g.fillRect(x, y, width, height);
    }
}