package it.unibs.pajc;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static AtomicInteger counter = new AtomicInteger(1);

    public static void main(String[] args) {
        System.out.println("Avvio del server...");

        try (ServerSocket server = new ServerSocket(1234)) {
            while (true) {
                Socket client = server.accept();
                int clientId = counter.getAndIncrement();
                String clientName = "Client" + clientId;

                System.out.printf("Client connesso %s, %s\n", clientName, client.getInetAddress());
                ProtocolProcessor pp = new ProtocolProcessor(client, clientName);

                Thread t = new Thread(pp);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
