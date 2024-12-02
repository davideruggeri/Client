package it.unibs.pajc;

import javax.swing.*;
import java.awt.*;

public class BackGround extends JPanel {
    private Image backgroundImage;
    private Ball ball;
    private Giocatore giocatore1, giocatore2;

    public BackGround(Ball ball, Giocatore giocatore1, Giocatore giocatore2) {
        this.ball = ball;
        this.giocatore1 = giocatore1;
        this.giocatore2 = giocatore2;
        String imagePath = "/images/sfondo1.png";
        backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        g.drawLine(0,500,getWidth(),500);
        ball.draw(g);
        giocatore1.draw(g);
        giocatore2.draw(g);
    }
}
