package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.lib.config.EnumTristate;
import com.feed_the_beast.ftblib.lib.gui.GuiLang;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBLibFinals.MOD_ID)
@Config(modid = FTBLibFinals.MOD_ID, category = "", name = "../local/ftblib")
@Config.LangKey(FTBLibFinals.MOD_ID)
public class FTBLibConfig
{
	@Config.LangKey(GuiLang.LANG_GENERAL)
	public static final General general = new General();

	public static final Teams teams = new Teams();

	public static class General
	{
		@Config.Comment("When this mode is enabled, FTBLib assumes that server clients don't have FTBLib and/or other mods installed")
		public boolean clientless_mode = false;

		@Config.Comment("This will make all '/ftb x' commands work just as '/x'. Example: '/ftb home abc' => '/home abc'")
		@Config.RequiresWorldRestart
		public boolean mirror_ftb_commands = true;

		@Config.Comment({"Merges player profiles, in case player logged in without internet connection/in offline mode server", "If set to DEFAULT, it will only merge on singleplayer worlds"})
		public EnumTristate merge_offline_mode_players = EnumTristate.TRUE;

		@Config.Comment("Prints incoming and outgoing network messages in console. Don't set to true, unless you are debugging net spam")
		public boolean log_net = false;
	}

	public static class Teams
	{
		@Config.Comment("Automatically creates a team for player on multiplayer, based on their username and with a random color")
		public boolean autocreate_mp = false;

		@Config.Comment("Automatically creates (or joins) a team on singleplayer/LAN with ID 'singleplayer'")
		public boolean autocreate_sp = true;
	}

	public static void sync()
	{
		ConfigManager.sync(FTBLibFinals.MOD_ID, Config.Type.INSTANCE);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(FTBLibFinals.MOD_ID))
		{
			sync();
		}
	}
}