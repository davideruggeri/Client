package it.unibs.pajc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ProtocolProcessor implements Runnable {
    private Socket client;
    private BufferedReader in;

    public ProtocolProcessor(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String input;

            while ((input = in.readLine()) != null  ) {
                System.out.printf("Ricevuto dal client [%s]: %s\n", client.getInetAddress(), input);

                String command = Command.getCommand(input);
                if (command != null) {
                    System.out.printf("Comando riconosciuto %s.\n", command);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
