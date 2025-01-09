package net.tarsa.components;

import net.minecraft.server.network.ServerPlayerEntity;
import net.tarsa.enums.GameStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
class GameStat {
    private GameStates GAME_STATE;
    private int PLAYERS_COUNT;
    private List<ServerPlayerEntity> PLAYERS;
    public GameStat(GameStates currentState, int playerCount, List<ServerPlayerEntity> currentPlayers) {
        this.GAME_STATE = currentState;
        this.PLAYERS_COUNT = playerCount;
        this.PLAYERS = currentPlayers;
    }

    public int getPlayersCount() {
        return PLAYERS_COUNT;
    }
    public GameStates getGameState() {
        return GAME_STATE;
    }
    public List<ServerPlayerEntity> getPlayers() {
        return PLAYERS;
    }
    public void setGameState(GameStates state) {
        this.GAME_STATE = state;
    }
    public void setPlayersCount(int count) {
        this.PLAYERS_COUNT = count;
    }
    public void updatePlayerCount(int count, boolean increment) {
        if (increment) {
            this.PLAYERS_COUNT += count;
        } else {
            this.PLAYERS_COUNT -= count;
        }
    }
    public boolean addPlayer(ServerPlayerEntity player) {
        for (ServerPlayerEntity alreadyPlayer : this.PLAYERS) {
            if (player == alreadyPlayer) {
                return false;
            }
        }
        this.PLAYERS.add(player);
        return true;
    }
    public boolean removePlayer(ServerPlayerEntity player) {
        for (ServerPlayerEntity alreadyPlayer : this.PLAYERS) {
            if (player == alreadyPlayer) {
                this.PLAYERS.remove(player);
                return true;
            }
        }
        return false;
    }
}