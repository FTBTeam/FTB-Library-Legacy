package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
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

	public static final IDrawableObject TEX_ENTITY = ImageProvider.get(FTBLibFinals.MOD_ID + ":textures/gui/entity.png");
}