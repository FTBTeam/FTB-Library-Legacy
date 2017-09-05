package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.lib.config.EnumTristate;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBLibFinals.MOD_ID)
@Config(modid = FTBLibFinals.MOD_ID, category = "config", name = "../local/ftbl")
@Config.LangKey(FTBLibFinals.MOD_ID)
public class FTBLibConfig
{
	public static final General general = new General();
	public static final Teams teams = new Teams();

	public static class General
	{
		@Config.Comment("When this mode is enabled, FTBLib assumes that server clients don't have FTBLib and/or other mods installed")
		public boolean clientless_mode = false;

		@Config.Comment("This will make all '/ftb x' commands work just as '/x'. Example: '/ftb home abc' => '/home abc'")
		@Config.RequiresWorldRestart
		public boolean mirror_ftb_commands = true;

		@Config.Comment("Merges player profiles, in case player logged in without internet connection/in offline mode server\\nBy default, only does that in singleplayer worlds")
		public EnumTristate merge_offline_mode_players = EnumTristate.DEFAULT;
	}

	public static class Teams
	{
		@Config.Comment("Automatically creates a team for player, based on their username and with a random color")
		public boolean autocreate_teams = false;

		@Config.RangeInt(min = 0, max = 10000)
		public int max_team_chat_history = 300;
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