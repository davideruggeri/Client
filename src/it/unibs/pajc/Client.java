package it.unibs.pajc;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) {
        String host = "localHost";
        //String host = "192.168.1.101";
        int port = 1234;
        String clientId = "Client";

        try {
            Socket client = new Socket(host, port);
            System.out.println("Client connected");

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            JFrame frame = new JFrame("Key listener");
            frame.setSize(400, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            frame.addKeyListener(new KeyListener() {

               public void keyTyped(KeyEvent e) {}
               public void keyPressed(KeyEvent e) {
                   String keyText = KeyEvent.getKeyText(e.getKeyCode());
                   String command = Command.getCommand(keyText);

                   if (command != null) {
                       String message = clientId + ": " + command;
                       out.println(message);
                       System.out.println("Inviato al server: " + message);
                   } else {
                       System.out.println("Comando non valido: " + keyText);
                   }
               }

               public void keyReleased(KeyEvent e) {}
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
