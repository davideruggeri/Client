package it.unibs.pajc;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        System.out.println("Avvio del server...");
        int nclients = 1;

        try (ServerSocket server = new ServerSocket(1234)) {
            while (true) {
                System.out.printf("Attesa di un nuovo client ... [%d]\n", nclients++);
                Socket client = server.accept();
                ProtocolProcessor pp = new ProtocolProcessor(client);

                Thread t = new Thread(pp);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
