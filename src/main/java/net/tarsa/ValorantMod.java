package net.tarsa;

import net.fabricmc.api.ModInitializer;

import net.tarsa.commands.ValorantCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValorantMod implements ModInitializer {
	public static final String MOD_ID = "tarsa-valorant-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loaded Valorant");

		ValorantCommandHandler.register();
	}
}