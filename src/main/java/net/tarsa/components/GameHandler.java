package net.tarsa.components;


import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.Objects;
import java.util.Random;

//Logic for the game.
public class GameHandler {
    //joins the player to a random game, tries* 10 times if couldn't the each.
    public static void joinRandomGame(ServerPlayerEntity player) {
        PlayerStats playerStats = new PlayerStats(player);
        if (!Objects.equals(playerStats.getPlayerGameID(), "")) {
            player.sendMessage(Text.translatable("command.valorant.join.error.alreadyInGame"), false);
            return;
        }
        String NewGameID = GameHandler.generateRandomID();
        playerStats.setPlayerGameID(NewGameID);
        GlobalGameStats.setCurrentIDs(NewGameID);
        player.sendMessage(Text.translatable("command.valorant.join.successfullyJoined"), false);
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
