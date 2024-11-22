package it.unibs.pajc;

import java.util.HashMap;
import java.util.Map;

public class Command {
    private static Map<String, String> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("LEFT", "Movimento verso sinistra");
        COMMANDS.put("RIGHT", "Movimento verso destra");
        COMMANDS.put("SPACE", "Salto");
        COMMANDS.put("X", "Calcio tipo 1");
        COMMANDS.put("Z", "Calcio tipo 2");
    }

    public static String getCommand(String key) {
        return COMMANDS.getOrDefault(key.toUpperCase(), null);
    }

    public static boolean isValidCommand(String key) {
        return COMMANDS.containsKey(key.toUpperCase());
    }
}
