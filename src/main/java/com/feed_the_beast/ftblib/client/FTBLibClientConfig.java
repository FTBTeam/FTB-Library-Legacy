package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLib;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBLib.MOD_ID, value = Side.CLIENT)
@Config(modid = "ftblib_client", category = "", name = "../local/client/ftblib")
public class FTBLibClientConfig
{
	@Config.LangKey("stat.generalButton")
	public static final General general = new General();

	public static class General
	{
		@Config.Comment("Show item Ore Dictionary names in inventory")
		public boolean item_ore_names = false;

		@Config.Comment("Show item NBT in inventory")
		public boolean item_nbt = false;

		@Config.Comment({
				"DISABLED: Buttons are hidden",
				"TOP_LEFT: Buttons are placed on top-left corner, where NEI has it's buttons",
				"INVENTORY_SIDE: Buttons are placed on the side or top of your inventory, depending on potion effects and crafting book",
				"AUTO: When NEI is installed, INVENTORY_SIDE, else TOP_LEFT"
		})
		public EnumSidebarButtonPlacement action_buttons = EnumSidebarButtonPlacement.AUTO;

		@Config.Comment("Mirror /ftbc commands")
		public boolean mirror_commands = true;

		@Config.Comment("Replace vanilla status message with Notifications, which support colors and timers")
		public boolean replace_vanilla_status_messages = true;

		@Config.Comment("Show help text while holding F3")
		public boolean debug_helper = true;
	}

	public static void sync()
	{
		ConfigManager.sync("ftblib_client", Config.Type.INSTANCE);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals("ftblib_client"))
		{
			sync();
		}
	}
}