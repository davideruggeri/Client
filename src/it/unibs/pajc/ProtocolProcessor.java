package it.unibs.pajc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProtocolProcessor implements Runnable {
    private Socket client;
    private BufferedReader in;
    private String clientName;
    private Set<String> activeKeys = new HashSet<>();

    public ProtocolProcessor(Socket client, String clientName) {
        this.client = client;
        this.clientName = clientName;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String input;

            while ((input = in.readLine()) != null  ) {
                System.out.printf("Ricevuto [%s]: %s\n", clientName, input);
                input = "PRESS:" + input;

                if(input.startsWith("PRESS:")) {
                    String key = input.substring(6);
                    activeKeys.add(key);
                    System.out.printf("Tasto premuto [%s]: %s\n", clientName, key);
                } else if (input.startsWith("RELEASE:")) {
                    String key = input.substring(8);
                    activeKeys.remove(key);
                    System.out.printf("Tasto rilasciato [%s]: %s\n", clientName, key);
                }

                if (activeKeys.contains("LEFT") && activeKeys.contains("SPACE")) {
                    System.out.printf("Combinazione riconosciuta da %s: FRECCE + SPAZIO.\n", clientName);
                } else if (activeKeys.contains("LEFT")) {
                    System.out.printf("Tasto attivo da %s: FRECCIA SINISTRA.\n", clientName);
                } else if (activeKeys.contains("RIGHT")) {
                    System.out.printf("Tasto attivo da %s: FRECCIA DESTRA.\n", clientName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
