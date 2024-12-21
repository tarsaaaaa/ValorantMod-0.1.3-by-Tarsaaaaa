package net.tarsa.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.tarsa.ValorantMod;
import net.tarsa.interfaces.PlayerGameStatExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * mod-id = "game id"
 *
 */

@Mixin(PlayerEntity.class)
public class PlayerGameStatMixin implements PlayerGameStatExt {
    @Unique
    NbtCompound PLAYER_GAME_DATA;
    @Override
    public NbtCompound getPlayerGameData() {
        if (PLAYER_GAME_DATA == null) {
            PLAYER_GAME_DATA = new NbtCompound();
        }
        return PLAYER_GAME_DATA;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeData(NbtCompound nbt, CallbackInfo ci) {
        if (PLAYER_GAME_DATA != null) {
            nbt.put(ValorantMod.MOD_ID + "-player-game-data", PLAYER_GAME_DATA);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(ValorantMod.MOD_ID + "-player-game-data")) {
            PLAYER_GAME_DATA = nbt.getCompound(ValorantMod.MOD_ID + "-player-game-data");
        }
    }
}

