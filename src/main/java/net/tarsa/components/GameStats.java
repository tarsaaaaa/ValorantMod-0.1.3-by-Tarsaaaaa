package net.tarsa.components;

import java.util.List;
import java.util.Objects;

//For all the game logic that needs to be stored with limited access to the player.
public class GameStats {
    private static List<String> CurrentIDs;
    public static List<String> getCurrentIDs() {
        return CurrentIDs;
    }

    public static void setCurrentIDs(String id) {
        for (String a : CurrentIDs) {
            if (Objects.equals(a, id)) {
                return;
            }
        }
        CurrentIDs.add(id);
    }
}
