package net.tarsa.components;


import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.tarsa.interfaces.PlayerGameStatExt;

import java.util.Objects;
import java.util.Random;

import static net.tarsa.util.ServerRegistry.PLAYER_GAME_DATA_ID_KEY;

//Logic for the game.
public class GameHandler {
    public static void joinRandomGame(ServerPlayerEntity player) {
        NbtCompound playerGameData = ((PlayerGameStatExt) player).getPlayerGameData();
        if (!Objects.equals(playerGameData.getString(PLAYER_GAME_DATA_ID_KEY), "")) {
            player.sendMessage(Text.translatable("command.valorant.join.error.alreadyInGame"), false);
            return;
        }
        String NewGameID = GameHandler.generateRandomID();
        int i = 1;
        while (!Objects.equals(playerGameData.getString(PLAYER_GAME_DATA_ID_KEY), NewGameID) && i < 10) {
            playerGameData.putString(PLAYER_GAME_DATA_ID_KEY, NewGameID);
            i++;
        }
        if (!Objects.equals(playerGameData.getString(PLAYER_GAME_DATA_ID_KEY), NewGameID)) {
            player.sendMessage(Text.translatable("command.valorant.join.error.couldNotJoin"), false);
            return;
        }
        GameStats.setCurrentIDs(NewGameID);
        player.sendMessage(Text.translatable("command.valorant.join.successfullyJoined"), false);
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
            if (GameStats.getCurrentIDs() == null) {
                return generatedID;
            }
            for (String a : GameStats.getCurrentIDs()) {
                if (!Objects.equals(a, generatedID)) {
                    return generatedID;
                }
            }
        }
    }
}
