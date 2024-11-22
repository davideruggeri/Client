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
        //String host = "localHost";
        String host = "192.168.1.101";
        int port = 1234;
        String clientId = "Client-1";

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
                   String message = clientId + ": " + KeyEvent.getKeyText(e.getKeyCode());
                   out.println(message);
               }

               public void keyReleased(KeyEvent e) {}
           });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected static void clientToServer(Socket client) {
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

            String line;

            while ((line = stdin.readLine()) != null) {
                out.println(line);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    protected static void serverToClient(Socket client) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String response;

            while ((response = in.readLine()) != null) {
                System.out.printf("> %s\n", response);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
