package it.unibs.pajc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GameController {
    private static final int PANEL_WIDTH = 1000;
    private static final int PANEL_HEIGHT = 600;
    private static final int FPS = 60;

    private Ball ball;
    private Giocatore giocatore1, giocatore2;
    private BackGround backGround;
    private static Set<Integer> pressedKeys = new HashSet<>();

    public GameController(JFrame frame) {
        // Inizializza gli oggetti del gioco
        ball = new Ball(PANEL_WIDTH / 2, PANEL_HEIGHT / 4);
        giocatore1 = new Giocatore(100, 450, 20, 50);
        giocatore2 = new Giocatore(800, 450, 20, 50);

        // Inizializza il pannello grafico e imposta nel frame
        backGround = new BackGround(ball, giocatore1, giocatore2);
        frame.setContentPane(backGround);
        frame.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        frame.pack();

        frame.setFocusable(true);
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                synchronized (pressedKeys) {
                    pressedKeys.add(e.getKeyCode());
                    handleMovement(); // Gestisce il movimento basato sui tasti premuti
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                synchronized (pressedKeys) {
                    pressedKeys.remove(e.getKeyCode());
                }
            }
        });
        startGame();
    }

    private void handleMovement() {
        // Gestione del movimento del giocatore1 basato sui tasti premuti
        if (pressedKeys.contains(KeyEvent.VK_SPACE) && pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            giocatore1.moveUpRight();
        } else if (pressedKeys.contains(KeyEvent.VK_SPACE) && pressedKeys.contains(KeyEvent.VK_LEFT)) {
            giocatore1.moveUpLeft();
        }

        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            giocatore1.moveLeft();
        } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            giocatore1.moveRight();
        }

        if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
            giocatore1.jump();
        }

        // Impedisci al giocatore di attraversare l'altro
        giocatore1.impedisciTrapasso(giocatore2);
    }

    public void startGame() {
        Timer timer = new Timer(1000 / FPS, e -> {
            // Aggiorna lo stato degli oggetti
            ball.move();
            ball.checkBounds();
            giocatore1.update();
            giocatore2.update();
            ball.checkCollision(giocatore1, giocatore2);

            // Richiede il ridisegno del pannello
            backGround.repaint();
        });
        timer.start();
    }

    public void reset() {
        ball.resetBall(PANEL_WIDTH / 2, PANEL_HEIGHT / 4, 2);
    }
}
