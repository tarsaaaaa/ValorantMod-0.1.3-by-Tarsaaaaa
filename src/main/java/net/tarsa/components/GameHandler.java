package net.tarsa.components;


import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.Random;

//Logic for the game.
public class GameHandler {
    //creates a new game.
    /*
    * * filename = gameID *
    * current-players=
    * player-1=
    * . . .
    * player-n=
    */
    public static void createGame(String gameID, MinecraftServer server) {
        ExternalFileHandler.writeFile("current-players=0\nplayer-0=\nplayer-1=\nplayer-2=\nplayer-3=\nplayer-4=\nplayer-5=\nplayer-6=\nplayer-7=\nplayer-8=\nplayer-9=", "valorant", gameID + ".txt", false, server);
    }

    //adds the player to the game with gameID.
    public static void addPlayersToGame(String gameID, ServerPlayerEntity player, MinecraftServer server) {
        int currentPlayers = Integer.parseInt(Objects.requireNonNull(ExternalFileHandler.readFile(1, "valorant", gameID + ".txt", "current-players", server)));
        String playerKey = "player-" + (currentPlayers);
        int newPlayers = currentPlayers + 1;
        ExternalFileHandler.modifyFile(currentPlayers+2, playerKey, player.getName().getString(), "valorant", gameID + ".txt", server);
        ExternalFileHandler.modifyFile(1, "current-players", String.valueOf(newPlayers), "valorant", gameID + ".txt", server);
    }

    //creates a game with random id.
    public static void createGameWithRandomID(ServerPlayerEntity player, ServerCommandSource commandSource) {
        PlayerStats playerStats = new PlayerStats(player);
        if (!Objects.equals(playerStats.getPlayerGameID(), "")) {
            player.sendMessage(Text.translatable("command.valorant.create.error.already-in-game"), false);
            return;
        }
        MinecraftServer server = commandSource.getServer();
        String NewGameID = GameHandler.generateRandomID();
        createGame(NewGameID, server);
        addPlayersToGame(NewGameID, player, server);
        playerStats.setPlayerGameID(NewGameID);
        GlobalGameStats.setCurrentIDs(NewGameID);
        player.sendMessage(Text.translatable("command.valorant.create.successfully-created"), false);
    }
    public static void createGameWithRandomID(ServerPlayerEntity player) {
        PlayerStats playerStats = new PlayerStats(player);
        if (!Objects.equals(playerStats.getPlayerGameID(), "")) {
            player.sendMessage(Text.translatable("command.valorant.create.error.already-in-game"), false);
            return;
        }
        String NewGameID = GameHandler.generateRandomID();
        playerStats.setPlayerGameID(NewGameID);
        GlobalGameStats.setCurrentIDs(NewGameID);
        player.sendMessage(Text.translatable("command.valorant.create.successfully-created"), false);
    }

    //creates a game with given id.
    public static void createGameWithGivenId(ServerPlayerEntity player, ServerCommandSource commandSource, String gameID) {
        PlayerStats playerStats = new PlayerStats(player);
        if (!Objects.equals(playerStats.getPlayerGameID(), "")) {
            player.sendMessage(Text.translatable("command.valorant.create.error.already-in-game"), false);
            return;
        }
        MinecraftServer server = commandSource.getServer();
        List<String> inUseIds = GlobalGameStats.getCurrentIDs();
        if (inUseIds != null) {
            for (String id : inUseIds) {
                if (Objects.equals(id, gameID)) {
                    player.sendMessage(Text.translatable("command.valorant.create.error.id-already-in-use"), false);
                    return;
                }
            }
        }
        createGame(gameID, server);
        addPlayersToGame(gameID, player, server);
        GlobalGameStats.setCurrentIDs(gameID);
        player.sendMessage(Text.translatable("command.valorant.create.successfully-created"), false);
    }

    //join a random game.
    public static void joinRandomGame(ServerPlayerEntity player, ServerCommandSource commandSource) {
        List<String> inUseIds = GlobalGameStats.getCurrentIDs();
        MinecraftServer server = commandSource.getServer();
        final Random RANDOM = new Random();
        int count = 0;
        if (inUseIds != null) {
            for (String id : inUseIds) {
                count++;
            }
            int randomNo = RANDOM.nextInt(count);
            String SelectedId = inUseIds.get(randomNo-1);
            addPlayersToGame(SelectedId, player, server);
            player.sendMessage(Text.translatable("command.valorant.join.no-id.successfully-joined"), false);
            return;
        }
        createGameWithRandomID(player, commandSource);
        player.sendMessage(Text.translatable("command.valorant.join.no-id.id-not-found-create-new"), false);
    }

    //join a game with given id.
    public static void joinGameWithId(ServerPlayerEntity player, ServerCommandSource commandSource, String gameId) {
        List<String> inUseIds = GlobalGameStats.getCurrentIDs();
        MinecraftServer server = commandSource.getServer();
        for (String id : inUseIds) {
            if (Objects.equals(gameId, id)) {
                addPlayersToGame(gameId, player, server);
                player.sendMessage(Text.translatable("command.valorant.join.id.successfullyJoined"), false);
                return;
            }
        }
        player.sendMessage(Text.translatable("command.valorant.join.id.game-not-found"), false);
    }

    //gives the player's game info (id, )
    public static void gameQuery(ServerPlayerEntity player) {
        PlayerStats playerStats = new PlayerStats(player);
        if (Objects.equals(playerStats.getPlayerGameID(), "")) {
            player.sendMessage(Text.translatable("command.valorant.query.notInGame"), false);
            return;
        }
        String GameID = playerStats.getPlayerGameID();
        player.sendMessage(Text.translatable("command.valorant.query.success", GameID), false);
    }

    //attempts a leave operation on the player from the current game they are in.
    public static void leaveGame(ServerPlayerEntity player) {
        PlayerStats playerStats = new PlayerStats(player);
        if (Objects.equals(playerStats.getPlayerGameID(), "")) {
            player.sendMessage(Text.translatable("command.valorant.leave.error.notInGame"), false);
            return;
        }
        String GameID = playerStats.getPlayerGameID();
    }

    //generates a random id for the game (excludes already in use ids')
    private static String generateRandomID() {
        final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";
        final Random RANDOM = new Random();
        StringBuilder id = new StringBuilder(5);
        while (true) {
            //generated a 5 character long id.
            for (int i = 0; i < 5; i++) {
                int index = RANDOM.nextInt(CHARACTERS.length());
                id.append(CHARACTERS.charAt(index));
            }
            String generatedID = id.toString();
            //checks if the id is in use, if yes regenerates another id, if not returns the id.
            if (GlobalGameStats.getCurrentIDs() == null) {
                return generatedID;
            }
            for (String a : GlobalGameStats.getCurrentIDs()) {
                if (!Objects.equals(a, generatedID)) {
                    return generatedID;
                }
            }
        }
    }
}
