package it.unibs.pajc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GameController {
    private static final int PANEL_WIDTH = 1000;
    private static final int PANEL_HEIGHT = 600;
    private static final int FPS = 60;

    private JLabel timer;
    private Ball ball;
    private Giocatore giocatore1, giocatore2;
    private BackGround backGround;
    private static Set<Integer> pressedKeys = new HashSet<>();
    private int seconds;
    private Timer t;
    private int score1 = 0, score2 = 0;
    private JLabel scoreLabel1, scoreLabel2;

    public GameController(JFrame frame) {
        ball = new Ball(PANEL_WIDTH / 2, PANEL_HEIGHT / 4);
        giocatore1 = new Giocatore(100, 450, 20, 80);
        giocatore2 = new Bot(800, 450, 20, 80, ball);


        backGround = new BackGround(ball, giocatore1, giocatore2);
        frame.setContentPane(backGround);
        frame.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        frame.pack();
        scoreLabel1 = new JLabel("0");
        scoreLabel1.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel1.setFont(new Font("Courier New", Font.BOLD, 30));
        scoreLabel1.setBounds(40, 40, 364, 96);
        backGround.add(scoreLabel1);

        timer = new JLabel();
        timer.setHorizontalAlignment(JLabel.CENTER);
        timer.setFont(new Font("Courier New", Font.BOLD, 40));
        timer.setBounds(480, 30, 364,96);
        backGround.add(timer);


        scoreLabel2 = new JLabel("0");
        scoreLabel2.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel2.setFont(new Font("Courier New", Font.BOLD, 30));
        scoreLabel2.setBounds(700, 40, 364, 96);
        backGround.add(scoreLabel2);



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
        aggiornaTimer();
        startGame();
    }

    private void handleMovement() {
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

        giocatore1.impedisciTrapasso(giocatore2);
    }

    private void aggiornaTimer() {
        seconds = 90;
        timer.setText("01:30");

        t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds--;
                int minutes = seconds / 60;
                int sec = seconds % 60;

                timer.setText(String.format("%02d:%02d", minutes, sec));
            }
        });

        t.start(); // Avvia il timer
    }

    public void startGame() {
        Timer timer = new Timer(1000 / FPS, e -> {
            ball.move();
            ball.checkBounds();
            giocatore1.update();
            giocatore2.update();
            ball.checkCollision(giocatore1, giocatore2);

            backGround.repaint();
            goal();
        });
        timer.start();
    }

    private int goal(){
        int goal = ball.goal();
        if (goal == 1) {
            score1++;
            scoreLabel1.setText("" + score1);
            ball.resetBall(PANEL_WIDTH / 2, PANEL_HEIGHT / 2, 1);
        } else if (goal == 2) {
            score2++;
            scoreLabel2.setText("" + score2);
            ball.resetBall(PANEL_WIDTH / 3, PANEL_HEIGHT / 2, 2);
        }
        return goal;
    }
}
