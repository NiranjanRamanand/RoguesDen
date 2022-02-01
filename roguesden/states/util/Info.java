package roguesden.states.util;

import org.powbot.api.Tile;
import org.powbot.api.rt4.Bank;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
    Information shared across roguesden.states and miscellaneous util functions
 */

public class Info {
    private static List<String> missing = null;
    private static boolean updated = false;
    private static boolean portOpen = true;

    private static final Tile[] path = {
            new Tile(3056, 4992, 1),
            null,
            new Tile(3048, 4997, 1),
            new Tile(3039, 4997, 1),
            new Tile(3037, 5002, 1),
            null,
            new Tile(3023, 5001, 1),
            new Tile(3013, 5004, 1),
            new Tile(3008, 5003, 1),
            new Tile(2994, 5004, 1),
            null,
            new Tile(2988, 5003, 1),
            new Tile(2988, 5004, 1),
            new Tile(2976, 5014, 1),
            new Tile(2970, 5017, 1),
            new Tile(2969, 5017, 1),
            new Tile(2967, 5017, 1),
            new Tile(2964, 5024, 1),
            new Tile(2959, 5024, 1),
            new Tile(2958, 5028, 1),
            new Tile(2959, 5028, 1),
            null,
            new Tile(2958, 5035, 1),
            new Tile(2962, 5048, 1),
            new Tile(2962, 5052, 1),
            new Tile(2963, 5055, 1),
            null,
            new Tile(2968, 5061, 1),
            null,
            new Tile(2974, 5059, 1),
            new Tile(2989, 5057, 1),
            new Tile(2990, 5057, 1),
            new Tile(2992, 5067, 1),
            new Tile(2992, 5078, 1),
            new Tile(3009, 5063, 1),
            new Tile(3029, 5056, 1),
            new Tile(3028, 5056, 1),
            new Tile(3028, 5051, 1),
            new Tile(3028, 5047, 1),
            new Tile(3018, 5047, 1)
    };

    private static final int [] obstacles = {-1, 7251, -1, -1, -1, 7255,
            -1 ,-1, -1,  -1, 7240,
            -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, 7239, -1, -1, -1, -1, 7246, -1, 7251, -1,
            -1, 7255, -1, -1, -1, -1, -1, -1 , -1};

    public static List<String> getMissing() {
        return missing;
    }

    public static void setMissing(List<String> l) {
        missing = l;
    }

    public static void update() {
        sendString("Setting update to true");
        updated = true;
    }

    public static boolean isUpdated() {
        return updated;
    }

    public static int [] getObjects() {
        return obstacles;
    }

    public static Tile [] getPathTiles() {
        return path;
    }

    // Assumes bank is open
    public static List<String> getMissingPieces() {
        String [] items = new String [] {"Rogue top", "Rogue mask", "Rogue trousers", "Rogue boots", "Rogue gloves"};

        // Format item names into list of dialogue options
        List<String> missing = Arrays.asList(items);

        missing = missing.
                stream().
                map(i -> i.replace("Rogue ", "") + ".").
                collect(Collectors.toList());

        missing.
                removeAll(Bank.stream().
                        name(items).
                        list().
                        stream().
                        map(i -> i.name().replace("Rogue ", "") + ".").
                        collect(Collectors.toList()));



        return missing;
    }

    // Local debugging sometimes fails to log, but this works
    public static void sendString(String msg) {
        if(!portOpen) return;

        try {
            Socket s = new Socket("10.0.2.2", 4999);

            PrintWriter pw = new PrintWriter( s.getOutputStream());
            pw.println(System.currentTimeMillis() + ": " + msg);

            pw.flush();
        } catch(Exception e) {
            portOpen = false;
        }
    }
}
