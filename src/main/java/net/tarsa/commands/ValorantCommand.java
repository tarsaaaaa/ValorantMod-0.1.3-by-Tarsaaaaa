package net.tarsa.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.tarsa.components.GameHandler;

public class ValorantCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("valorant")
                .executes(ValorantCommand::valorant) //(/valorant)
                .then(CommandManager.literal("query")
                        .executes(ValorantCommand::query)) //(/valorant query)
                .then(CommandManager.literal("join")
                        .executes(ValorantCommand::joinWithoutID) //(/valorant join)
                        .then(CommandManager.argument("join-id", StringArgumentType.string())
                                .executes(ValorantCommand::joinWithID)))//(/valorant join "string id")
                .then(CommandManager.literal("leave")
                        .executes(ValorantCommand::leave))
                .then(CommandManager.literal("create")
                        .executes(ValorantCommand::createWithoutID)
                        .then(CommandManager.argument("create-id", StringArgumentType.string())
                                .executes(ValorantCommand::createWithID)))
        );
    }



    private static int leave(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            source.sendFeedback(() -> Text.translatable("command.valorant.leave.error.notPlayer"), false);
            return 1;
        }
        GameHandler.leaveGame(player);
        return 0;
    }

    private static int joinWithoutID(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            source.sendFeedback(() -> Text.translatable("command.valorant.create.error.not-player"), false);
            return 1;
        }
        GameHandler.joinRandomGame(player, source);
        return 0;
    }

    private static int joinWithID(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();
        String gameId = StringArgumentType.getString(context, "join-id");
        if (player == null) {
            source.sendFeedback(() -> Text.translatable("command.valorant.create.error.not-player"), false);
            return 1;
        }
        GameHandler.joinGameWithId(player, source, gameId);
        return 0;
    }

    private static int createWithoutID(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            source.sendFeedback(() -> Text.translatable("command.valorant.create.error.not-player"), false);
            return 1;
        }
        GameHandler.createGameWithRandomID(player, source);
        return 0;
    }

    private static int createWithID(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();
        String gameId = StringArgumentType.getString(context, "create-id");
        if (player == null) {
            source.sendFeedback(() -> Text.translatable("command.valorant.create.error.not-player"), false);
            return 1;
        }
        GameHandler.createGameWithGivenId(player, source, gameId);
        return 0;
    }

    private static int query(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            source.sendFeedback(() -> Text.translatable("command.valorant.join.error.notPlayer"), false);
            return 1;
        }
        GameHandler.gameQuery(player);
        return 0;
    }

    private static int valorant(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.translatable("command.valorant.run"), false);
        return 0;
    }


}
