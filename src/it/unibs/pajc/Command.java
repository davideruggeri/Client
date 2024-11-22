package it.unibs.pajc;

import java.util.HashMap;
import java.util.Map;

public class Command {
    private static Map<String, String> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("LEFT", "Left: Movimento verso sinistra");
        COMMANDS.put("RIGHT", "Right: Movimento verso destra");
        COMMANDS.put("SPACE", "Space: Salto");
        COMMANDS.put("X", "X: Calcio tipo 1");
        COMMANDS.put("Z", "Z: Calcio tipo 2");
    }

    public static String getCommand(String key) {
        return COMMANDS.getOrDefault(key.toUpperCase(), null);
    }
}
