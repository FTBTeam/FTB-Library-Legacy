package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.events.registry.RegisterClientConfigEvent;
import com.feed_the_beast.ftbl.lib.ConfigRGB;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@EventHandler(Side.CLIENT)
@Config(modid = FTBLibFinals.MOD_ID + "_client", category = "config", name = "../local/client/" + FTBLibFinals.MOD_ID)
public class FTBLibClientConfig
{
	@Config.LangKey("ftbl.config.general")
	public static final General general = new General();

	public static final GuideCategory guide = new GuideCategory();

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

		public boolean replace_vanilla_status_messages = true;
		public boolean mirror_commands = true;
		public boolean enable_chunk_selector_depth = true;
	}

	public static class GuideCategory
	{
		@Config.RangeInt(min = 0, max = 200)
		public int border_width = 15;

		@Config.RangeInt(min = 0, max = 200)
		public int border_height = 15;

		public final ConfigRGB background = new ConfigRGB(0xFFF7F4DA);
		public final ConfigRGB text = new ConfigRGB(0xFF7B6534);
	}

	public static void sync()
	{
		ConfigManager.sync(FTBLibFinals.MOD_ID + "_client", Config.Type.INSTANCE);
		guide.background.updateColor();
		guide.text.updateColor();
		System.out.println("Synced! " + general.item_nbt);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(FTBLibFinals.MOD_ID + "_client"))
		{
			sync();
		}
	}

	@SubscribeEvent
	public static void registerClientConfig(RegisterClientConfigEvent event)
	{
		event.register(FTBLibFinals.MOD_ID + "_client", FTBLibFinals.MOD_NAME, ImageProvider.get(FTBLibFinals.MOD_ID + ":textures/logo_guide.png"));
	}
}