package it.unibs.pajc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main  {
    GameController controller;

    private JFrame frame;
    public JLabel description;


    public static void main(String[] args) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {

                        Main window = new Main();
                        window.frame.setVisible(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
               }
            });
    }

    public Main() {
        startGame();
    }

    public void startGame(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Menu");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        JPanel menuPanel = new JPanel();
        frame.getContentPane().add(menuPanel, BorderLayout.CENTER);
        menuPanel.setBackground(Color.GRAY);
        menuPanel.setLayout(null);

        JLabel titleLabel = new JLabel("HEAD BALL");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Showcard Gothic", Font.BOLD, 80));
        titleLabel.setBounds(175, 47, 673, 173);
        menuPanel.add(titleLabel);

        JButton btnSinglePlayer = new JButton("SINGLE PLAYER");
        btnSinglePlayer.setFont(new Font("Arial Black", Font.PLAIN, 40));
        btnSinglePlayer.setBounds(150, 244, 728, 96);
        menuPanel.add(btnSinglePlayer);
        btnSinglePlayer.addActionListener(this::startLocalGame);

        JButton btnHostGame = new JButton("HOST GAME");
        //btnHostGame.addActionListener(this::hostGame);
        btnHostGame.setFont(new Font("Arial Black", Font.PLAIN, 40));
        btnHostGame.setBounds(150, 340, 364, 96);
        menuPanel.add(btnHostGame);

        JButton btnJoinGame = new JButton("JOIN GAME");
        //btnJoinGame.addActionListener(this::joinGame);
        btnJoinGame.setFont(new Font("Arial Black", Font.PLAIN, 40));
        btnJoinGame.setBounds(514, 340, 364, 96);
        menuPanel.add(btnJoinGame);

    }

    public void startLocalGame(ActionEvent e){

        JOptionPane.showMessageDialog(null, "I tasti disponibili per la partita sono:\n" +
                "- (<- e ->) per muoversi di lato.\n" +
                "- (space) per saltare.\n" +
                "- (z) calcio.", "Mosse disponibili", JOptionPane.PLAIN_MESSAGE);

        frame.getContentPane().removeAll();
        controller = new GameController(frame);

    }
}
