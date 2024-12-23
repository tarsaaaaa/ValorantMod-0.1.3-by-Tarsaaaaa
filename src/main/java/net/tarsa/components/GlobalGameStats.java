package net.tarsa.components;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//For all the game logic that needs to be stored with limited access to the player.
public class GlobalGameStats {
    private static List<String> CurrentIDs;

    public static List<String> getCurrentIDs() {
        return CurrentIDs;
    }

    public static void setCurrentIDs(String id) {
        if (CurrentIDs == null) {
            CurrentIDs = new ArrayList<>();
            CurrentIDs.add(id);
            return;
        }
        for (String a : CurrentIDs) {
            if (Objects.equals(a, id)) {
                return;
            }
        }
        CurrentIDs.add(id);
    }
}
