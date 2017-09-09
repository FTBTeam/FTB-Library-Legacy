package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;

/**
 * @author LatvianModder
 */
public class GuiConfigs
{
	public static final int CHUNK_SELECTOR_TILES_TEX = 16;
	public static final int CHUNK_SELECTOR_TILES_GUI = 15;
	public static final int CHUNK_SELECTOR_TILES_GUI2 = 7;
	public static final double CHUNK_SELECTOR_UV = (double) CHUNK_SELECTOR_TILES_GUI / (double) CHUNK_SELECTOR_TILES_TEX;

	public static final Icon TEX_ENTITY = Icon.getIcon(FTBLibFinals.MOD_ID + ":textures/gui/entity.png");
}