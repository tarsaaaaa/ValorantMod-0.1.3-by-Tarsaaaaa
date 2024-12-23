package net.tarsa.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.tarsa.interfaces.PlayerGameStatExt;

import java.util.Objects;

import static net.tarsa.util.ServerRegistry.*;

//For all the player stats that need to be saved.
public class PlayerStats {
    private final String PlayerGameID;
    private final NbtCompound PlayerDataCompound;
    public PlayerStats(ServerPlayerEntity player) {
        PlayerDataCompound = ((PlayerGameStatExt) player).getPlayerGameData();
        PlayerGameID = PlayerDataCompound.getString(PLAYER_GAME_DATA_ID_KEY);
    }

    public void setPlayerGameID(String id) {
        if (Objects.equals(PlayerGameID, id)) {
            return;
        }
        PlayerDataCompound.putString(PLAYER_GAME_DATA_ID_KEY, id);
    }

    public String getPlayerGameID() {
        if (PlayerGameID == null) {
            return "";
        }
        return PlayerGameID;
    }
}
