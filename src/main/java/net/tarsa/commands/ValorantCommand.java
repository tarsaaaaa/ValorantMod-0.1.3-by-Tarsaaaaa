package net.tarsa.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.tarsa.components.GameHandler;
import net.tarsa.interfaces.PlayerGameStatExt;
import net.tarsa.util.ServerRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.tarsa.util.ServerRegistry.PLAYER_GAME_DATA_ID_KEY;


//(/valorant)
public class ValorantCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("valorant")
                .executes(ValorantCommand::valorant)
                .then(CommandManager.literal("query")
                        .executes(ValorantCommand::query))
                .then(CommandManager.literal("join")
                        .executes(ValorantCommand::joinWithoutID)
                        .then(CommandManager.argument("join-id", StringArgumentType.string())
                                .executes(ValorantCommand::joinWithID)))
        );
    }

    private static int joinWithID(CommandContext<ServerCommandSource> context) {
        return 0;
    }

    private static int joinWithoutID(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            source.sendFeedback(() -> Text.translatable("command.valorant.join.error.notPlayer"), false);
            return 1;
        }
        GameHandler.joinRandomGame(player);
        return 0;
    }

    private static int query(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            source.sendFeedback(() -> Text.translatable("command.valorant.join.error.notPlayer"), false);
            return 1;
        }
        NbtCompound playerGameData = ((PlayerGameStatExt) player).getPlayerGameData();
        if (Objects.equals(playerGameData.getString(PLAYER_GAME_DATA_ID_KEY), "")) {
            source.sendFeedback(() -> Text.translatable("command.valorant.query.error.notInGame"), false);
            return 0;
        }
        String GameID = playerGameData.getString(PLAYER_GAME_DATA_ID_KEY);
        source.sendFeedback(() -> Text.translatable("command.valorant.query.success", GameID), false);
        return 0;
    }

    private static int valorant(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.translatable("command.valorant.run"), false);
        return 0;
    }


}
