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
@Mod.EventBusSubscriber(modid = FTBLib.MOD_ID)
@Config(modid = FTBLib.MOD_ID, category = "", name = "../local/" + FTBLib.MOD_ID)
@Config.LangKey(FTBLib.MOD_ID)
public class FTBLibConfig
{
	@Config.LangKey(GuiLang.LANG_GENERAL)
	public static final General general = new General();

	public static final Teams teams = new Teams();

	@Config.Comment("Don't set any values to true, unless you are debugging the mod.")
	public static final Debugging debugging = new Debugging();

	public static class General
	{
		@Config.Comment("When this mode is enabled, FTBLib assumes that server clients don't have FTBLib and/or other mods installed.")
		public boolean clientless_mode = false;

		@Config.Comment("This will make all '/ftb x' commands work just as '/x'. Example: '/ftb home abc' => '/home abc'.")
		@Config.RequiresWorldRestart
		public boolean mirror_ftb_commands = true;

		@Config.Comment({"Merges player profiles, in case player logged in without internet connection/in offline mode server.", "If set to DEFAULT, it will only merge on singleplayer worlds."})
		public EnumTristate merge_offline_mode_players = EnumTristate.TRUE;
	}

	public static class Teams
	{
		@Config.Comment("Automatically creates a team for player on multiplayer, based on their username and with a random color.")
		public boolean autocreate_mp = false;

		@Config.Comment("Automatically creates (or joins) a team on singleplayer/LAN with ID 'singleplayer'.")
		public boolean autocreate_sp = true;

		@Config.Comment("Disable no team notification entirely.")
		@Config.LangKey("player_config.ftblib.hide_team_notification")
		public boolean hide_team_notification = false;
	}

	public static class Debugging
	{
		@Config.Comment("Print more info.")
		public boolean print_more_info = false;

		@Config.Comment("Print more errors.")
		public boolean print_more_errors = false;

		@Config.Comment("Log incoming and outgoing network messages.")
		public boolean log_network = false;

		@Config.Comment("Log player teleporting.")
		public boolean log_teleport = false;

		@Config.Comment("Log config editing.")
		public boolean log_config_editing = false;

		@Config.Comment("See dev-only sidebar buttons. They probably don't do anything.")
		public boolean dev_sidebar_buttons = false;

		@Config.Comment("See GUI widget bounds when you hold B.")
		public boolean gui_widget_bounds = false;

		@Config.Comment("Log all events that extend EventBase.")
		public boolean log_events = false;
	}

	public static void sync()
	{
		ConfigManager.sync(FTBLib.MOD_ID, Config.Type.INSTANCE);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(FTBLib.MOD_ID))
		{
			sync();
		}
	}
}