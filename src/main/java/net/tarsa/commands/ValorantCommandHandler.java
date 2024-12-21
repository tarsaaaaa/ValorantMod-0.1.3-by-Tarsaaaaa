package net.tarsa.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ValorantCommandHandler {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ValorantCommand.register(dispatcher);
        });
    }
}
