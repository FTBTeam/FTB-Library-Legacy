package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBLibFinals.MOD_ID, value = Side.CLIENT)
@Config(modid = FTBLibFinals.MOD_ID + "_client", category = "config", name = "../local/client/ftblib")
public class FTBLibClientConfig
{
	@Config.LangKey(GuiLang.LANG_GENERAL)
	public static final General general = new General();

	public static class General
	{
		public boolean item_ore_names = false;

		public boolean item_nbt = false;

		@Config.Comment({
				"DISABLED: Buttons are hidden",
				"TOP_LEFT: Buttons are placed on top-left corner, where NEI has it's buttons",
				"INVENTORY_SIDE: Buttons are placed on the side or top of your inventory, depending on potion effects and crafting book",
				"AUTO: When NEI is installed, INVENTORY_SIDE, else TOP_LEFT"
		})
		public EnumSidebarButtonPlacement action_buttons = EnumSidebarButtonPlacement.AUTO;

		public boolean mirror_commands = true;
		public boolean replace_vanilla_status_messages = true;
	}

	public static void sync()
	{
		ConfigManager.sync(FTBLibFinals.MOD_ID + "_client", Config.Type.INSTANCE);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(FTBLibFinals.MOD_ID + "_client"))
		{
			sync();
		}
	}
}